/* generated using openapi-typescript-codegen -- do not edit */
/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { OrderItem } from "./OrderItem";
import type { User } from "./User";
export type PageUser = {
  records?: Array<User>;
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
