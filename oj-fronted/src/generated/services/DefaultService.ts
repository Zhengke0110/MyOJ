/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { BaseResponseBoolean } from "../models/BaseResponseBoolean";
import type { BaseResponseInteger } from "../models/BaseResponseInteger";
import type { BaseResponseLoginUserVO } from "../models/BaseResponseLoginUserVO";
import type { BaseResponseLong } from "../models/BaseResponseLong";
import type { BaseResponsePagePost } from "../models/BaseResponsePagePost";
import type { BaseResponsePagePostVO } from "../models/BaseResponsePagePostVO";
import type { BaseResponsePageQuestion } from "../models/BaseResponsePageQuestion";
import type { BaseResponsePageQuestionSubmitVO } from "../models/BaseResponsePageQuestionSubmitVO";
import type { BaseResponsePageQuestionVO } from "../models/BaseResponsePageQuestionVO";
import type { BaseResponsePageUser } from "../models/BaseResponsePageUser";
import type { BaseResponsePageUserVO } from "../models/BaseResponsePageUserVO";
import type { BaseResponsePostVO } from "../models/BaseResponsePostVO";
import type { BaseResponseQuestionAdminVO } from "../models/BaseResponseQuestionAdminVO";
import type { BaseResponseQuestionVO } from "../models/BaseResponseQuestionVO";
import type { BaseResponseUser } from "../models/BaseResponseUser";
import type { BaseResponseUserVO } from "../models/BaseResponseUserVO";
import type { DeleteRequest } from "../models/DeleteRequest";
import type { PostAddRequest } from "../models/PostAddRequest";
import type { PostEditRequest } from "../models/PostEditRequest";
import type { PostFavourAddRequest } from "../models/PostFavourAddRequest";
import type { PostFavourQueryRequest } from "../models/PostFavourQueryRequest";
import type { PostQueryRequest } from "../models/PostQueryRequest";
import type { PostThumbAddRequest } from "../models/PostThumbAddRequest";
import type { PostUpdateRequest } from "../models/PostUpdateRequest";
import type { QuestionAddRequest } from "../models/QuestionAddRequest";
import type { QuestionEditRequest } from "../models/QuestionEditRequest";
import type { QuestionQueryRequest } from "../models/QuestionQueryRequest";
import type { QuestionSubmitAddRequest } from "../models/QuestionSubmitAddRequest";
import type { QuestionSubmitQueryRequest } from "../models/QuestionSubmitQueryRequest";
import type { QuestionUpdateRequest } from "../models/QuestionUpdateRequest";
import type { UserAddRequest } from "../models/UserAddRequest";
import type { UserLoginRequest } from "../models/UserLoginRequest";
import type { UserQueryRequest } from "../models/UserQueryRequest";
import type { UserRegisterRequest } from "../models/UserRegisterRequest";
import type { UserUpdateMyRequest } from "../models/UserUpdateMyRequest";
import type { UserUpdateRequest } from "../models/UserUpdateRequest";
import type { CancelablePromise } from "../core/CancelablePromise";
import { OpenAPI } from "../core/OpenAPI";
import { request as __request } from "../core/request";
export class DefaultService {
  /**
   * 用户注册
   * @param requestBody
   * @returns BaseResponseLong
   * @throws ApiError
   */
  public static postUserRegister(
    requestBody?: UserRegisterRequest
  ): CancelablePromise<BaseResponseLong> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/user/register",
      body: requestBody,
      mediaType: "application/json",
    });
  }
  /**
   * 用户登录
   * 用户级账号
   * {
   * "userAccount": "cillum",
   * "userPassword": "123456789"
   * }
   * @param requestBody
   * @returns BaseResponseLoginUserVO
   * @throws ApiError
   */
  public static postUserLogin(
    requestBody?: UserLoginRequest
  ): CancelablePromise<BaseResponseLoginUserVO> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/user/login",
      body: requestBody,
      mediaType: "application/json",
    });
  }
  /**
   * 用户登出
   * @returns BaseResponseBoolean
   * @throws ApiError
   */
  public static postUserLogout(): CancelablePromise<BaseResponseBoolean> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/user/logout",
    });
  }
  /**
   * 获取当前登录用户
   * @returns BaseResponseLoginUserVO
   * @throws ApiError
   */
  public static getUserGetLogin(): CancelablePromise<BaseResponseLoginUserVO> {
    return __request(OpenAPI, {
      method: "GET",
      url: "/user/get/login",
    });
  }
  /**
   * 创建用户
   * @param requestBody
   * @returns BaseResponseLong
   * @throws ApiError
   */
  public static postUserAdd(
    requestBody?: UserAddRequest
  ): CancelablePromise<BaseResponseLong> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/user/add",
      body: requestBody,
      mediaType: "application/json",
    });
  }
  /**
   * 删除用户
   * @param requestBody
   * @returns BaseResponseBoolean
   * @throws ApiError
   */
  public static postUserDelete(
    requestBody?: DeleteRequest
  ): CancelablePromise<BaseResponseBoolean> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/user/delete",
      body: requestBody,
      mediaType: "application/json",
    });
  }
  /**
   * 更新用户
   * @param requestBody
   * @returns BaseResponseBoolean
   * @throws ApiError
   */
  public static postUserUpdate(
    requestBody?: UserUpdateRequest
  ): CancelablePromise<BaseResponseBoolean> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/user/update",
      body: requestBody,
      mediaType: "application/json",
    });
  }
  /**
   * 根据 id 获取用户（仅管理员）
   * @param id
   * @returns BaseResponseUser
   * @throws ApiError
   */
  public static getUserGet(id?: number): CancelablePromise<BaseResponseUser> {
    return __request(OpenAPI, {
      method: "GET",
      url: "/user/get",
      query: {
        id: id,
      },
    });
  }
  /**
   * 根据 id 获取包装类
   * @param id
   * @returns BaseResponseUserVO
   * @throws ApiError
   */
  public static getUserGetVo(
    id?: number
  ): CancelablePromise<BaseResponseUserVO> {
    return __request(OpenAPI, {
      method: "GET",
      url: "/user/get/vo",
      query: {
        id: id,
      },
    });
  }
  /**
   * 分页获取用户列表（仅管理员）
   * @param requestBody
   * @returns BaseResponsePageUser
   * @throws ApiError
   */
  public static postUserListPage(
    requestBody?: UserQueryRequest
  ): CancelablePromise<BaseResponsePageUser> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/user/list/page",
      body: requestBody,
      mediaType: "application/json",
    });
  }
  /**
   * 分页获取用户封装列表
   * @param requestBody
   * @returns BaseResponsePageUserVO
   * @throws ApiError
   */
  public static postUserListPageVo(
    requestBody?: UserQueryRequest
  ): CancelablePromise<BaseResponsePageUserVO> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/user/list/page/vo",
      body: requestBody,
      mediaType: "application/json",
    });
  }
  /**
   * 更新个人信息
   * @param requestBody
   * @returns BaseResponseBoolean
   * @throws ApiError
   */
  public static postUserUpdateMy(
    requestBody?: UserUpdateMyRequest
  ): CancelablePromise<BaseResponseBoolean> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/user/update/my",
      body: requestBody,
      mediaType: "application/json",
    });
  }
  /**
   * 创建
   * @param requestBody
   * @returns BaseResponseLong
   * @throws ApiError
   */
  public static postPostAdd(
    requestBody?: PostAddRequest
  ): CancelablePromise<BaseResponseLong> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/post/add",
      body: requestBody,
      mediaType: "application/json",
    });
  }
  /**
   * 删除
   * @param requestBody
   * @returns BaseResponseBoolean
   * @throws ApiError
   */
  public static postPostDelete(
    requestBody?: DeleteRequest
  ): CancelablePromise<BaseResponseBoolean> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/post/delete",
      body: requestBody,
      mediaType: "application/json",
    });
  }
  /**
   * 更新（仅管理员）
   * @param requestBody
   * @returns BaseResponseBoolean
   * @throws ApiError
   */
  public static postPostUpdate(
    requestBody?: PostUpdateRequest
  ): CancelablePromise<BaseResponseBoolean> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/post/update",
      body: requestBody,
      mediaType: "application/json",
    });
  }
  /**
   * 根据 id 获取
   * @param id
   * @returns BaseResponsePostVO
   * @throws ApiError
   */
  public static getPostGetVo(
    id?: number
  ): CancelablePromise<BaseResponsePostVO> {
    return __request(OpenAPI, {
      method: "GET",
      url: "/post/get/vo",
      query: {
        id: id,
      },
    });
  }
  /**
   * 分页获取列表（仅管理员）
   * @param requestBody
   * @returns BaseResponsePagePost
   * @throws ApiError
   */
  public static postPostListPage(
    requestBody?: PostQueryRequest
  ): CancelablePromise<BaseResponsePagePost> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/post/list/page",
      body: requestBody,
      mediaType: "application/json",
    });
  }
  /**
   * 分页获取列表（封装类）
   * @param requestBody
   * @returns BaseResponsePagePostVO
   * @throws ApiError
   */
  public static postPostListPageVo(
    requestBody?: PostQueryRequest
  ): CancelablePromise<BaseResponsePagePostVO> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/post/list/page/vo",
      body: requestBody,
      mediaType: "application/json",
    });
  }
  /**
   * 分页获取当前用户创建的资源列表
   * @param requestBody
   * @returns BaseResponsePagePostVO
   * @throws ApiError
   */
  public static postPostMyListPageVo(
    requestBody?: PostQueryRequest
  ): CancelablePromise<BaseResponsePagePostVO> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/post/my/list/page/vo",
      body: requestBody,
      mediaType: "application/json",
    });
  }
  /**
   * 分页搜索（从 ES 查询，封装类）未开发
   * @param requestBody
   * @returns BaseResponsePagePostVO
   * @throws ApiError
   */
  public static postPostSearchPageVo(
    requestBody?: PostQueryRequest
  ): CancelablePromise<BaseResponsePagePostVO> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/post/search/page/vo",
      body: requestBody,
      mediaType: "application/json",
    });
  }
  /**
   * 编辑（用户）
   * @param requestBody
   * @returns BaseResponseBoolean
   * @throws ApiError
   */
  public static postPostEdit(
    requestBody?: PostEditRequest
  ): CancelablePromise<BaseResponseBoolean> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/post/edit",
      body: requestBody,
      mediaType: "application/json",
    });
  }
  /**
   * 点赞 / 取消点赞
   * @param requestBody
   * @returns BaseResponseInteger
   * @throws ApiError
   */
  public static postPostThumb(
    requestBody?: PostThumbAddRequest
  ): CancelablePromise<BaseResponseInteger> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/post_thumb/",
      body: requestBody,
      mediaType: "application/json",
    });
  }
  /**
   * 收藏 / 取消收藏
   * @param requestBody
   * @returns BaseResponseInteger
   * @throws ApiError
   */
  public static postPostFavour(
    requestBody?: PostFavourAddRequest
  ): CancelablePromise<BaseResponseInteger> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/post_favour/",
      body: requestBody,
      mediaType: "application/json",
    });
  }
  /**
   * 获取我收藏的列表
   * @param requestBody
   * @returns BaseResponsePagePostVO
   * @throws ApiError
   */
  public static postPostFavourMyListPage(
    requestBody?: PostQueryRequest
  ): CancelablePromise<BaseResponsePagePostVO> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/post_favour/my/list/page",
      body: requestBody,
      mediaType: "application/json",
    });
  }
  /**
   * 获取用户收藏的列表
   * @param requestBody
   * @returns BaseResponsePagePostVO
   * @throws ApiError
   */
  public static postPostFavourListPage(
    requestBody?: PostFavourQueryRequest
  ): CancelablePromise<BaseResponsePagePostVO> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/post_favour/list/page",
      body: requestBody,
      mediaType: "application/json",
    });
  }
  /**
   * 创建
   * @param requestBody
   * @returns BaseResponseLong
   * @throws ApiError
   */
  public static postQuestionAdd(
    requestBody?: QuestionAddRequest
  ): CancelablePromise<BaseResponseLong> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/question/add",
      body: requestBody,
      mediaType: "application/json",
    });
  }
  /**
   * 删除
   * @param requestBody
   * @returns BaseResponseBoolean
   * @throws ApiError
   */
  public static postQuestionDelete(
    requestBody?: DeleteRequest
  ): CancelablePromise<BaseResponseBoolean> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/question/delete",
      body: requestBody,
      mediaType: "application/json",
    });
  }
  /**
   * 更新（仅管理员)
   * @param requestBody
   * @returns BaseResponseBoolean
   * @throws ApiError
   */
  public static postQuestionUpdate(
    requestBody?: QuestionUpdateRequest
  ): CancelablePromise<BaseResponseBoolean> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/question/update",
      body: requestBody,
      mediaType: "application/json",
    });
  }
  /**
   * 根据 id 获取(ADMIN)
   * @param id
   * @returns BaseResponseQuestionAdminVO
   * @throws ApiError
   */
  public static getQuestionGet(
    id?: string
  ): CancelablePromise<BaseResponseQuestionAdminVO> {
    return __request(OpenAPI, {
      method: "GET",
      url: "/question/get",
      query: {
        id: id,
      },
    });
  }
  /**
   * 根据 id 获取(脱敏)
   * @param id
   * @returns BaseResponseQuestionVO
   * @throws ApiError
   */
  public static getQuestionGetVo(
    id?: string
  ): CancelablePromise<BaseResponseQuestionVO> {
    return __request(OpenAPI, {
      method: "GET",
      url: "/question/get/vo",
      query: {
        id: id,
      },
    });
  }
  /**
   * 分页获取列表（封装类）
   * @param requestBody
   * @returns BaseResponsePageQuestionVO
   * @throws ApiError
   */
  public static postQuestionListPageVo(
    requestBody?: QuestionQueryRequest
  ): CancelablePromise<BaseResponsePageQuestionVO> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/question/list/page/vo",
      body: requestBody,
      mediaType: "application/json",
    });
  }
  /**
   * 分页获取当前用户创建的资源列表
   * @param requestBody
   * @returns BaseResponsePageQuestionVO
   * @throws ApiError
   */
  public static postQuestionMyListPageVo(
    requestBody?: QuestionQueryRequest
  ): CancelablePromise<BaseResponsePageQuestionVO> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/question/my/list/page/vo",
      body: requestBody,
      mediaType: "application/json",
    });
  }
  /**
   * 分页获取题目列表（仅管理员）
   * @param requestBody
   * @returns BaseResponsePageQuestion
   * @throws ApiError
   */
  public static postQuestionListPage(
    requestBody?: QuestionQueryRequest
  ): CancelablePromise<BaseResponsePageQuestion> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/question/list/page",
      body: requestBody,
      mediaType: "application/json",
    });
  }
  /**
   * 编辑（用户）
   * @param requestBody
   * @returns BaseResponseBoolean
   * @throws ApiError
   */
  public static postQuestionEdit(
    requestBody?: QuestionEditRequest
  ): CancelablePromise<BaseResponseBoolean> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/question/edit",
      body: requestBody,
      mediaType: "application/json",
    });
  }
  /**
   * 提交题目
   * @param requestBody
   * @returns BaseResponseLong
   * @throws ApiError
   */
  public static postQuestionSubmit(
    requestBody?: QuestionSubmitAddRequest
  ): CancelablePromise<BaseResponseLong> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/question_submit/",
      body: requestBody,
      mediaType: "application/json",
    });
  }
  /**
   * 分页获取题目提交列表（除了管理员外，普通用户只能看到非答案、提交代码等公开信息）
   * @param requestBody
   * @returns BaseResponsePageQuestionSubmitVO
   * @throws ApiError
   */
  public static postQuestionSubmitListPage(
    requestBody?: QuestionSubmitQueryRequest
  ): CancelablePromise<BaseResponsePageQuestionSubmitVO> {
    return __request(OpenAPI, {
      method: "POST",
      url: "/question_submit/list/page",
      body: requestBody,
      mediaType: "application/json",
    });
  }
}
