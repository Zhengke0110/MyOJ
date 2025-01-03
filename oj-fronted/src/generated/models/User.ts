/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type User = {
    /**
     * id
     */
    id?: number;
    /**
     * 用户账号
     */
    userAccount?: string;
    /**
     * 用户密码
     */
    userPassword?: string;
    /**
     * 开放平台id
     */
    unionId?: string;
    /**
     * 公众号openId
     */
    mpOpenId?: string;
    /**
     * 用户昵称
     */
    userName?: string;
    /**
     * 用户头像
     */
    userAvatar?: string;
    /**
     * 用户简介
     */
    userProfile?: string;
    /**
     * 用户角色：user/admin/ban
     */
    userRole?: string;
    /**
     * 创建时间
     */
    createTime?: string;
    /**
     * 更新时间
     */
    updateTime?: string;
    /**
     * 是否删除
     */
    isDelete?: number;
};

