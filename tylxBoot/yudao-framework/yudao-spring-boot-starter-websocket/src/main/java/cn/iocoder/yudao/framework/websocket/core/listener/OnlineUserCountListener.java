package cn.iocoder.yudao.framework.websocket.core.listener;

import cn.iocoder.yudao.framework.common.util.json.JsonUtils;
import cn.iocoder.yudao.framework.websocket.core.handler.JsonWebSocketMessageHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * 在线用户数量的 WebSocket 消息处理器
 */
@Component
public class OnlineUserCountListener implements WebSocketMessageListener<Map<String, Object>> {

    @Override
    public String getType() {
        return "get-online-users";
    }

    @Override
    public void onMessage(WebSocketSession session, Map<String, Object> message) {
        // 构建返回消息
        Map<String, Object> result = new HashMap<>();
        result.put("count", JsonWebSocketMessageHandler.getOnlineUserCount());
        result.put("users", JsonWebSocketMessageHandler.getOnlineUserList());

        // 构建 WebSocket 消息
        Map<String, Object> response = new HashMap<>();
        response.put("type", "online-users-count");
        response.put("content", JsonUtils.toJsonString(result));

        // 发送消息
        try {
            session.sendMessage(new TextMessage(JsonUtils.toJsonString(response)));
        }catch (IOException e) {

        }
    }

}
