import { DefaultService } from "@/generated";
import { type QuestionInterface } from "@/config";

/**
 * 根据ID获取题目信息(管理员)
 * @param id
 * @returns
 */
export const AdminGetQuestionById = async (id: string) => {
  return await DefaultService.getQuestionGet(id);
};

/**
 * 提交题目
 * @param form
 * @returns
 */
export const AddQuestion = async (form: QuestionInterface) => {
  return await DefaultService.postQuestionAdd(form);
};

/**
 * 更新题目
 * @param form
 * @returns
 */
export const UpdateQuestion = async (form: QuestionInterface, id: string) => {
  form.id = id;
  return await DefaultService.postQuestionUpdate(form);
};

/**
 * 获取题目列表(管理员)
 * @param current
 * @param pageSize
 * @returns
 */
export const GetQuestionsAdmin = async (current: number, pageSize: number) => {
  return await DefaultService.postQuestionListPage({ current, pageSize });
};

export const GetQuestions = async (current: number, pageSize: number) => {
  return await DefaultService.postQuestionListPageVo({ current, pageSize });
};

export const DeleteQuestionById = async (id: string) => {
  return await DefaultService.postQuestionDelete({ id });
};
