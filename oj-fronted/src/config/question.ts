import type { TableFilterable, TableSortable } from "@arco-design/web-vue";

// Question Interface
export interface QuestionInterface {
  id?: string;
  title: string;
  tags: string[];
  answer: string;
  content: string;
  judgeConfig: {
    memoryLimit: number;
    stackLimit: number;
    timeLimit: number;
  };
  judgeCase: {
    input: string;
    output: string;
  }[];
}

export const JudgeCaseItem = {
  input: "",
  output: "",
};

// 表格
export const QuestionAdminTableColumns: {
  title: string;
  dataIndex?: string;
  slotName?: string;
  sortable?: TableSortable;
  filterable?: TableFilterable;
}[] = [
  {
    title: "ID",
    dataIndex: "id",
    sortable: {
      sortDirections: ["ascend", "descend"],
    },
  },
  { title: "题目", dataIndex: "title" },
  { title: "标签", slotName: "tags" },
  { title: "通过率", slotName: "passing" },
  { title: "操作", slotName: "action" },
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
  { title: "通过率", slotName: "passing" },
  { title: "前去做题", slotName: "action" },
];
