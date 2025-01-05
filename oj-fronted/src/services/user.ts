import { DefaultService } from "@/generated";

/**
 * 获取当前登录的用户信息
 * @returns
 */
export const GetLoginInfoUser = async () => {
  return await DefaultService.getUserGetLogin();
};

/**
 * 用户登录
 * @param form
 * @returns
 */
export const UserLogin = async (form: {
  userAccount: string;
  userPassword: string;
}) => {
  return await DefaultService.postUserLogin({
    userAccount: form.userAccount,
    userPassword: form.userPassword,
  });
};

/**
 * 用户注册
 * @param form
 * @returns
 */
export const UserRegister = async (form: {
  userAccount: string;
  userPassword: string;
  checkPassword: string;
}) => {
  return await DefaultService.postUserRegister({
    userAccount: form.userAccount,
    userPassword: form.userPassword,
    checkPassword: form.checkPassword,
  });
};
