export const LogoPath = "/src/assets/dog.svg";
export const DefaultUserAvatar = "/src/assets/defaultUserAvatar.png";
export const DockerLogo = "/src/assets/docker-logo.png";
export const CodeAddress = "https://github.com/ZhengKe996/MyOJ";
export const BlogAddress = "http://timu.fun";
export const HotPath = "/src/assets/hot-air-balloon.svg";

export enum LayoutMenu {
  BasicLayout = "BasicLayout",
  UserLayout = "UserLayout",
}

export interface UserInterface {
  id?: string;
  userName: string;
  userAvatar: string;
  userRole: string;
  userProfile: string;
}

// Questions
import {
  type QuestionInterface,
  QuestionAdminTableColumns,
  QuestionTableColumns,
  JudgeCaseItemInfo,
  DefaultLanguage,
  type QuestionSolutionInterface,
  type SubmitRecordInterface,
} from "./question";

export {
  QuestionAdminTableColumns,
  QuestionTableColumns,
  JudgeCaseItemInfo,
  DefaultLanguage,
  type QuestionInterface,
  type QuestionSolutionInterface,
  type SubmitRecordInterface,
};
