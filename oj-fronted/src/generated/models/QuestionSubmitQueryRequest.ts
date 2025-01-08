/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
export type QuestionSubmitQueryRequest = {
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
     * 编程语言
     */
    language?: string;
    /**
     * 提交状态
     */
    status?: number;
    /**
     * 题目 id
     */
    questionId?: number;
    /**
     * 用户 id
     */
    userId?: number;
};

