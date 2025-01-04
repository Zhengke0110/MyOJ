import type { RouteRecordRaw } from "vue-router";
import { ACCESSENUM } from "@/access";

export const routes: RouteRecordRaw[] = [
  {
    path: "/user",
    name: "用户",
    component: () => import("@/layouts/UserLayout"),
    children: [
      {
        path: "/user/login",
        name: "用户登录",
        component: () => import("@/views/Login.vue"),
      },
      {
        path: "/user/register",
        name: "用户注册",
        component: () => import("@/views/Register.vue"),
      },
    ],
  },
  {
    path: "/",
    redirect: "/home",
  },
  {
    path: "/home",
    name: "首页",
    component: () => import("@/views/Home.vue"),
  },
  {
    path: "/topics",
    name: "浏览题目",
    component: () => import("@/views/Home.vue"),
  },
  {
    path: "/about",
    name: "关于我的",
    component: () => import("@/views/Home.vue"),
  },
  {
    path: "/admin",
    name: "Admin",
    meta: {
      access: ACCESSENUM.ADMIN,
    },
    component: () => import("@/views/Admin.vue"),
  },
  {
    path: "/noauth",
    name: "NoAuth",
    component: () => import("@/views/NoAuth.vue"),
  },
  {
    path: "/:pathMatch(.*)*",
    name: "Error",
    component: () => import("@/views/Error.vue"),
  },
];
