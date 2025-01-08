/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { JudgeInfo } from './JudgeInfo';
import type { QuestionVO } from './QuestionVO';
import type { UserVO } from './UserVO';
export type QuestionSubmitVO = {
    /**
     * id
     */
    id?: number;
    /**
     * 编程语言
     */
    language?: string;
    /**
     * 用户代码
     */
    code?: string;
    /**
     * 判题信息
     */
    judgeInfo?: JudgeInfo;
    /**
     * 判题状态（0 - 待判题、1 - 判题中、2 - 成功、3 - 失败）
     */
    status?: number;
    /**
     * 题目 id
     */
    questionId?: number;
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
    /**
     * 对应题目信息
     */
    questionVO?: QuestionVO;
};

