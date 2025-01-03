/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { UserVO } from './UserVO';
export type PostVO = {
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
     * 标签列表
     */
    tagList?: Array<string>;
    /**
     * 创建人信息
     */
    user?: UserVO;
    /**
     * 是否已点赞
     */
    hasThumb?: boolean;
    /**
     * 是否已收藏
     */
    hasFavour?: boolean;
};

