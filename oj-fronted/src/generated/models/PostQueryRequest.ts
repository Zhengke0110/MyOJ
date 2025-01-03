/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type PostQueryRequest = {
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
     * id
     */
    notId?: number;
    /**
     * 搜索词
     */
    searchText?: string;
    /**
     * 标题
     */
    title?: string;
    /**
     * 内容
     */
    content?: string;
    /**
     * 标签列表
     */
    tags?: Array<string>;
    /**
     * 至少有一个标签
     */
    orTags?: Array<string>;
    /**
     * 创建用户 id
     */
    userId?: number;
    /**
     * 收藏用户 id
     */
    favourUserId?: number;
};

