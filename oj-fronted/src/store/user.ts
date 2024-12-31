import { defineStore } from "pinia";
// Link:https://pinia.vuejs.org/zh/
import { ACCESSENUM } from "@/access";

interface UserInfo {
  userName: string;
  userRole: ACCESSENUM;
}
interface State {
  loginInfo: UserInfo;
}

export const useUserStore = defineStore("user", {
  state: (): State => ({
    loginInfo: { userName: "未登录", userRole: ACCESSENUM.NOLOGIN },
  }),
  getters: {
    getUserName(): string {
      return this.loginInfo.userName;
    },
    getUserRole(): ACCESSENUM {
      return this.loginInfo.userRole;
    },
    getLoginInfo(): UserInfo {
      return this.loginInfo;
    },
  },
  actions: {
    setUserName(username: string) {
      // TODO: 后续需要对接登录接口
      this.loginInfo.userName = username;
    },
    setLoginInfo(userName: string, userRole: ACCESSENUM) {
      this.loginInfo.userName = userName;
      this.loginInfo.userRole = userRole;
    },
  },
});
