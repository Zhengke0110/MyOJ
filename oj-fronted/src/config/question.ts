import type { TableFilterable, TableSortable } from "@arco-design/web-vue";

// Question Interface
export interface QuestionInterface {
  id?: string;
  title: string;
  tags: string[];
  answer?: string;
  acceptedNum: number;
  submitNum: number;
  content: string;
  judgeConfig: {
    memoryLimit: number;
    stackLimit: number;
    timeLimit: number;
  };
  judgeCase?: {
    input: string;
    output: string;
  }[];
}

export interface QuestionSolutionInterface {
  language: string;
  code: string;
  questionId: string;
}
export const DefaultLanguage = "java";

export const JudgeCaseItemInfo = {
  input: "",
  output: "",
};

// // 表格
// export const QuestionAdminTableColumns: {
//   title: string;
//   dataIndex?: string;
//   slotName?: string;
//   sortable?: TableSortable;
//   filterable?: TableFilterable;
// }[] = [
//   {
//     title: "ID",
//     dataIndex: "id",
//     sortable: {
//       sortDirections: ["ascend", "descend"],
//     },
//   },
//   { title: "题目", dataIndex: "title" },
//   { title: "标签", slotName: "tags" },
//   { title: "通过率", slotName: "acceptedRate" },
//   { title: "操作", slotName: "action" },
// ];

export const QuestionAdminTableColumns: {
  title: string;
}[] = [
  {
    title: "ID",
  },
  { title: "Title" },
  { title: "Tags" },
  { title: "Passing Rate" },
  { title: "Detail" },
  { title: "Edit" },
  { title: "Delete" },
];

// 表格
export const QuestionTableColumns: {
  title: string;
  dataIndex?: string;
  slotName?: string;
  sortable?: TableSortable;
  filterable?: TableFilterable;
}[] = [
  { title: "题目", dataIndex: "title" },
  { title: "标签", slotName: "tags" },
  { title: "通过率", slotName: "acceptedRate" },
  { title: "前去做题", slotName: "action" },
];

export interface SubmitRecordInterface {
  id: string;
  language: string;
  // code?: string;
  question: {
    id: string;
    title: string;
    tags: string[];
  };
  judgeInfo: {
    memory: number;
    time: number;
    message: string;
  };
  submitter: {
    userId?: string;
    userName: string;
    userAvatar: string;
  };
  createTime: string;
}
