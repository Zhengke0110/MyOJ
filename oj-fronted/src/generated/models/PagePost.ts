/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { OrderItem } from "./OrderItem";
import type { Post } from "./Post";
export type PagePost = {
  records?: Array<Post>;
  total?: number;
  size?: number;
  current?: number;
  orders?: Array<OrderItem>;
  optimizeCountSql?: boolean;
  searchCount?: boolean;
  optimizeJoinOfCountSql?: boolean;
  maxLimit?: number;
  countId?: string;
};
