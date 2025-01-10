export const LogoPath = "/src/assets/dog.svg";

export enum LayoutMenu {
  BasicLayout = "BasicLayout",
  UserLayout = "UserLayout",
}

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
