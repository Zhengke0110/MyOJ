/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type UserQueryRequest = {
    /**
     * 当前页号
     */
    current?: number;
    /**
     * 页面大小
     */
    pageSize?: number;
    /**
     * 排序字段
     */
    sortField?: string;
    /**
     * 排序顺序（默认升序）
     */
    sortOrder?: string;
    /**
     * id
     */
    id?: number;
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
     * 简介
     */
    userProfile?: string;
    /**
     * 用户角色：user/admin/ban
     */
    userRole?: string;
};

