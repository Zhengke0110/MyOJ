import { createRouter, createWebHistory } from "vue-router";
import { routes } from "./routes";

const router = createRouter({
  routes,
  history: createWebHistory(),
});

export default router;

// TODO 以下代码本应该在access/index.ts 中进行使用
import { useUserStore } from "@/store/user";
import { ACCESSENUM, NOLOGIN, CheckACCESS } from "@/access";
router.beforeEach(async (to, _, next) => {
  const userStore = useUserStore();
  const userRole = userStore.getUserRole;
  const userName = userStore.getUserName;

  // 未登录时，自动登录
  if (userName === NOLOGIN || userRole === ACCESSENUM.NOLOGIN) {
    await userStore.setLoginInfo;
  }

  const needAccess: ACCESSENUM =
    (to.meta?.access as ACCESSENUM) ?? ACCESSENUM.NOLOGIN;

  // 已登录
  if (needAccess !== ACCESSENUM.NOLOGIN) {
    // 如果没登陆，跳转到登录页面
    if (userName === NOLOGIN || userRole === ACCESSENUM.NOLOGIN) {
      // TODO 加弹窗
      next(`/user/login?redirect=${to.fullPath}`);
      return;
    }

    // 如果已经登陆了，但是权限不足，那么跳转到无权限页面
    if (!CheckACCESS(userRole, needAccess)) {
      // TODO 加弹窗
      next("/noAuth");
      return;
    }
  }
  next();
});
