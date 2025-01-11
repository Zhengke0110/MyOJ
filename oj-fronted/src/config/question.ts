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
export const QuestionTableColumns: {
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
  { title: "题解", dataIndex: "answer" },
  { title: "测试配置", dataIndex: "judgeConfig" },
  { title: "提交数", dataIndex: "submitNum" },
  { title: "通过数", dataIndex: "acceptedNum" },
  { title: "点赞数", dataIndex: "thumbNum" },
  { title: "喜欢数", dataIndex: "favourNum" },
  { title: "操作", slotName: "action" },
];
