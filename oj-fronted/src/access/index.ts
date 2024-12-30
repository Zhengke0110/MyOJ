export enum ACCESSENUM {
  NOLOGIN = "nologin",
  USER = "user",
  ADMIN = "admin",
}

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
    if (LoginUserAccess != ACCESSENUM.ADMIN) return false;
  }
  return true;
};
