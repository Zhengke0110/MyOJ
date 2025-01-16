export const LogoPath = "/src/assets/dog.svg";
export const DefaultUserAvatar = "/src/assets/defaultUserAvatar.png";

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
