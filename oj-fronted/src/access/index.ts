export enum ACCESSENUM {
  NOLOGIN = "nologin",
  USER = "user",
  ADMIN = "admin",
}

export const NOLOGIN = "未登录";
/**
 * 检查当前用户权限是否合法
 * @param user
 * @param need
 */
export const CheckACCESS = (UserAccess: ACCESSENUM, NeedAccess: ACCESSENUM) => {
  const LoginUserAccess: ACCESSENUM = UserAccess ?? ACCESSENUM.NOLOGIN;

  // 不需要登录
  if (NeedAccess === ACCESSENUM.NOLOGIN) return true;

  // 需要登录
  if (NeedAccess === ACCESSENUM.USER) {
    if (LoginUserAccess === ACCESSENUM.NOLOGIN) return false;
  }

  if (NeedAccess === ACCESSENUM.ADMIN) {
    if (LoginUserAccess !== ACCESSENUM.ADMIN) return false;
  }
  return true;
};
// TODO: 循环依赖报错，将在router/index.ts中实现该功能
// import router from "@/router";
// import { useUserStore } from "@/store/user";
// router.beforeEach((to, _, next) => {
//   const userStore = useUserStore();
//   const userRole = userStore.getUserRole;

//   if (to.meta?.access === ACCESSENUM.ADMIN) {
//     if (userRole !== ACCESSENUM.ADMIN) {
//       next("/noauth");
//       return;
//     }
//   }
//   next();
// });
