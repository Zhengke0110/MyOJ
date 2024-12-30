import { defineStore } from "pinia";
// Link:https://pinia.vuejs.org/zh/

interface State {
  loginInfo: { userName: string };
}

export const useUserStore = defineStore("user", {
  state: (): State => ({
    loginInfo: { userName: "未登录" },
  }),
  getters: {
    getUserName(): string {
      return this.loginInfo.userName;
    },
  },
  actions: {
    setUserName(username: string) {
      // TODO: 后续需要对接登录接口
      this.loginInfo.userName = username;
    },
  },
});
