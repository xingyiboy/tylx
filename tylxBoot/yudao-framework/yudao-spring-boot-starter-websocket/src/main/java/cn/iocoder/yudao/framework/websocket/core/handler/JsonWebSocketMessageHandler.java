package cn.iocoder.yudao.framework.websocket.core.handler;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.framework.tenant.core.util.TenantUtils;
import cn.iocoder.yudao.framework.websocket.core.listener.WebSocketMessageListener;
import cn.iocoder.yudao.framework.websocket.core.message.JsonWebSocketMessage;
import cn.iocoder.yudao.framework.websocket.core.util.WebSocketFrameworkUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;
import org.springframework.web.socket.CloseStatus;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Set;
import java.util.concurrent.*;

/**
 * JSON 格式 {@link WebSocketHandler} 实现类
 *
 * 基于 {@link JsonWebSocketMessage#getType()} 消息类型，调度到对应的 {@link WebSocketMessageListener} 监听器。
 *
 * @author 芋道源码
 */
@Slf4j
public class JsonWebSocketMessageHandler extends TextWebSocketHandler {

    /**
     * type 与 WebSocketMessageListener 的映射
     */
    private final Map<String, WebSocketMessageListener<Object>> listeners = new HashMap<>();

    /**
     * 在线用户的 WebSocket 会话列表，key为用户ID，value为最后心跳时间
     */
    private static final Map<Long, Long> userLastPingMap = new ConcurrentHashMap<>();
    
    /**
     * 定时任务，用于清理超时的连接
     */
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
    
    /**
     * 心跳超时时间，单位：毫秒
     */
    private static final long HEARTBEAT_TIMEOUT = 10000; // 10秒

    @SuppressWarnings({"rawtypes", "unchecked"})
    public JsonWebSocketMessageHandler(List<? extends WebSocketMessageListener> listenersList) {
        listenersList.forEach((Consumer<WebSocketMessageListener>)
                listener -> listeners.put(listener.getType(), listener));
        
        // 启动定时任务，每秒检查一次心跳超时
        scheduler.scheduleAtFixedRate(this::cleanupExpiredSessions, 1, 1, TimeUnit.SECONDS);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // 1.1 空消息，跳过
        if (message.getPayloadLength() == 0) {
            return;
        }
        
        // 1.2 ping 心跳消息处理
        if (message.getPayloadLength() == 4 && Objects.equals(message.getPayload(), "ping")) {
            Long userId = WebSocketFrameworkUtils.getLoginUserId(session);
            if (userId != null) {
                // 更新用户最后心跳时间
                userLastPingMap.put(userId, System.currentTimeMillis());
                
                // 构建包含在线用户数的 pong 响应
                Map<String, Object> pongResponse = new HashMap<>();
                pongResponse.put("type", "pong");
                pongResponse.put("onlineCount", userLastPingMap.size());
                session.sendMessage(new TextMessage(JsonUtils.toJsonString(pongResponse)));
            }
            return;
        }

        // 2.1 解析消息
        try {
            JsonWebSocketMessage jsonMessage = JsonUtils.parseObject(message.getPayload(), JsonWebSocketMessage.class);
            if (jsonMessage == null) {
                log.error("[handleTextMessage][session({}) message({}) 解析为空]", session.getId(), message.getPayload());
                return;
            }
            if (StrUtil.isEmpty(jsonMessage.getType())) {
                log.error("[handleTextMessage][session({}) message({}) 类型为空]", session.getId(), message.getPayload());
                return;
            }
            // 2.2 获得对应的 WebSocketMessageListener
            WebSocketMessageListener<Object> messageListener = listeners.get(jsonMessage.getType());
            if (messageListener == null) {
                log.error("[handleTextMessage][session({}) message({}) 监听器为空]", session.getId(), message.getPayload());
                return;
            }
            // 2.3 处理消息
            Type type = TypeUtil.getTypeArgument(messageListener.getClass(), 0);
            Object messageObj = JsonUtils.parseObject(jsonMessage.getContent(), type);
            Long tenantId = WebSocketFrameworkUtils.getTenantId(session);
            TenantUtils.execute(tenantId, () -> messageListener.onMessage(session, messageObj));
        } catch (Throwable ex) {
            log.error("[handleTextMessage][session({}) message({}) 处理异常]", session.getId(), message.getPayload());
        }
    }

    /**
     * 清理超时的会话
     */
    private void cleanupExpiredSessions() {
        long now = System.currentTimeMillis();
        userLastPingMap.entrySet().removeIf(entry -> 
            now - entry.getValue() > HEARTBEAT_TIMEOUT
        );
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long userId = WebSocketFrameworkUtils.getLoginUserId(session);
        if (userId != null) {
            userLastPingMap.put(userId, System.currentTimeMillis());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Long userId = WebSocketFrameworkUtils.getLoginUserId(session);
        if (userId != null) {
            userLastPingMap.remove(userId);
        }
    }

    /**
     * 获取当前在线用户数
     */
    public static int getOnlineUserCount() {
        return userLastPingMap.size();
    }

    /**
     * 获取当前在线用户列表
     */
    public static Set<Long> getOnlineUserList() {
        return userLastPingMap.keySet();
    }

}
