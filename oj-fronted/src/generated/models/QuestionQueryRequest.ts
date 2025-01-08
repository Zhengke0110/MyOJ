/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type QuestionQueryRequest = {
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
     * 题目答案
     */
    answer?: string;
    /**
     * 创建用户 id
     */
    userId?: number;
};

