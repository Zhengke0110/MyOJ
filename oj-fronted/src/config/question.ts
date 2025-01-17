// Question Interface
export interface QuestionInterface {
  id?: string;
  title: string;
  tags: string[];
  answer?: string;
  acceptedNum: number;
  submitNum: number;
  content: string;
  passing?: number;
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
}[] = [
  { title: "Title" },
  { title: "Tags" },
  { title: "Passing Rate" },
  { title: "Solution" },
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
