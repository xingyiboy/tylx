const getters = {
  token: (state) => state.user.token,
  avatar: (state) => state.user.avatar,
  name: (state) => state.user.name,
  roles: (state) => state.user.roles,
  permissions: (state) => state.user.permissions,
  money: (state) => state.user.money,
  id: (state) => state.user.id
};
export default getters;
