/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { PostQueryRequest } from './PostQueryRequest';
export type PostFavourQueryRequest = {
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
     * 帖子查询请求
     */
    postQueryRequest?: PostQueryRequest;
    /**
     * 用户 id
     */
    userId?: number;
};

