import type { RouteRecordRaw } from "vue-router";
import { ACCESSENUM } from "@/access";
import { LayoutMenu } from "@/config";

export const routes: RouteRecordRaw[] = [
  {
    path: "/",
    redirect: "/home",
  },
  {
    path: "/login",
    redirect: "/user/login",
  },
  {
    path: "/register",
    redirect: "/user/register",
  },
  {
    path: "/user",
    name: "用户",
    component: () => import("@/layouts/UserLayout"),
    meta: { layout: LayoutMenu.UserLayout },
    children: [
      {
        path: "/user/login",
        name: "用户登录",
        component: () => import("@/views/user/Login.vue"),
      },
      {
        path: "/user/register",
        name: "用户注册",
        component: () => import("@/views/user/Register.vue"),
      },
    ],
  },
  {
    path: "/home",
    name: "首页",
    meta: { layout: LayoutMenu.BasicLayout, isShow: true },
    component: () => import("@/views/Home.vue"),
  },
  {
    path: "/topics",
    name: "浏览题目",
    meta: { layout: LayoutMenu.BasicLayout, isShow: true },
    component: () => import("@/views/question/Questions.vue"),
  },
  {
    path: "/solution",
    name: "解题",
    meta: { layout: LayoutMenu.BasicLayout, isShow: false },
    component: () => import("@/views/question/QuestionSolution.vue"),
  },
  {
    path: "/about",
    name: "关于我的",
    meta: { layout: LayoutMenu.BasicLayout, isShow: true },
    component: () => import("@/views/Home.vue"),
  },

  {
    path: "/add/question",
    name: "创建题目",
    component: () => import("@/views/question/QuestionView.vue"),
    meta: {
      access: ACCESSENUM.ADMIN,
      layout: LayoutMenu.BasicLayout,
      isShow: true,
    },
  },
  {
    path: "/update/question",
    name: "更新题目",
    component: () => import("@/views/question/QuestionView.vue"),
    meta: {
      access: ACCESSENUM.ADMIN,
      layout: LayoutMenu.BasicLayout,
      isShow: false,
    },
  },
  {
    path: "/manage/question/",
    name: "管理题目",
    component: () => import("@/views/question/ManageQuestionView.vue"),
    meta: {
      access: ACCESSENUM.ADMIN,
      layout: LayoutMenu.BasicLayout,
      isShow: true,
    },
  },
  {
    path: "/detail/question/",
    name: "题目详情",
    component: () => import("@/views/question/QuestionDetails.vue"),
    meta: {
      access: ACCESSENUM.ADMIN,
      layout: LayoutMenu.BasicLayout,
      isShow: false,
    },
  },
  {
    path: "/admin",
    name: "Admin",
    meta: {
      access: ACCESSENUM.ADMIN,
      layout: LayoutMenu.BasicLayout,
      isShow: true,
    },
    component: () => import("@/views/Admin.vue"),
  },
  {
    path: "/noauth",
    name: "NoAuth",
    meta: { layout: LayoutMenu.UserLayout, isShow: false },
    component: () => import("@/views/NoAuth.vue"),
  },
  {
    path: "/:pathMatch(.*)*",
    name: "Error",
    meta: { layout: LayoutMenu.UserLayout },
    component: () => import("@/views/Error.vue"),
  },
];
