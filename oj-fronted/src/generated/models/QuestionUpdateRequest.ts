/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { JudgeCase } from './JudgeCase';
import type { JudgeConfig } from './JudgeConfig';
export type QuestionUpdateRequest = {
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
     * 判题用例
     */
    judgeCase?: Array<JudgeCase>;
    /**
     * 判题配置
     */
    judgeConfig?: JudgeConfig;
};

