import { defineStore } from "pinia";
// Link:https://pinia.vuejs.org/zh/

interface State {
  loginInfo: { userName: string; role: string };
}

export const useUserStore = defineStore("user", {
  state: (): State => ({
    loginInfo: { userName: "未登录", role: "admit" },
  }),
  getters: {
    getUserName(): string {
      return this.loginInfo.userName;
    },
    getRole(): string {
      return this.loginInfo.role;
    },
  },
  actions: {
    setUserName(username: string) {
      // TODO: 后续需要对接登录接口
      this.loginInfo.userName = username;
    },
  },
});
