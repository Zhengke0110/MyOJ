import type { RouteRecordRaw } from "vue-router";
import { ACCESSENUM } from "@/access";

export const routes: RouteRecordRaw[] = [
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
