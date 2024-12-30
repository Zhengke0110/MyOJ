import type { RouteRecordRaw } from "vue-router";

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
    path: "/admit",
    name: "Admit",
    meta: {
      access: "CanAdmit",
    },
    component: () => import("@/views/Admit.vue"),
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
