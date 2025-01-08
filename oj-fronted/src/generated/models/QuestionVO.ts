/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { JudgeConfig } from './JudgeConfig';
import type { UserVO } from './UserVO';
export type QuestionVO = {
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
     * 题目提交数
     */
    submitNum?: number;
    /**
     * 题目通过数
     */
    acceptedNum?: number;
    /**
     * 判题配置
     */
    judgeConfig?: JudgeConfig;
    /**
     * 点赞数
     */
    thumbNum?: number;
    /**
     * 收藏数
     */
    favourNum?: number;
    /**
     * 创建用户 id
     */
    userId?: number;
    /**
     * 创建时间
     */
    createTime?: string;
    /**
     * 更新时间
     */
    updateTime?: string;
    /**
     * 创建人信息
     */
    userVO?: UserVO;
};

