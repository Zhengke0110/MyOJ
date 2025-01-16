import { DefaultService } from "@/generated";
import {
  type QuestionInterface,
  type QuestionSolutionInterface,
} from "@/config";

/**
 * 根据ID获取题目信息(管理员)
 * @param id
 * @returns
 */
export const AdminGetQuestionById = async (id: string) => {
  return await DefaultService.getQuestionGet(id);
};

/**
 * 根据ID获取题目信息(脱敏)
 * @param id
 * @returns
 */
export const GetQuestionByID = async (id: string) => {
  return await DefaultService.getQuestionGetVo(id);
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

/**
 * 根据ID删除题目
 * @param id
 * @returns
 */
export const DeleteQuestionById = async (id: string) => {
  return await DefaultService.postQuestionDelete({ id });
};

// Question Submit
/**
 * 提交题目
 * @param form
 * @returns
 */
export const SetQuestionSubmit = async (form: QuestionSolutionInterface) => {
  return await DefaultService.postQuestionQuestionSubmitDo({
    language: form.language,
    code: form.code,
    questionId: form.questionId,
  });
};

/**
 * 获取问题提交列表
 *
 * 该函数通过调用DefaultService中的postQuestionQuestionSubmitListPage方法，来获取问题提交的分页列表
 * 主要用于在前端展示问题提交的列表，可以根据当前页码和每页大小来获取数据
 *
 * @param current 当前页码，用于指定从哪一页开始获取数据
 * @param pageSize 每页大小，用于指定每页显示的数据条数
 * @returns 返回一个Promise对象，解析后包含问题提交列表的响应数据
 */
export const GetQuestionSubmitList = async (
  current: number,
  pageSize: number
) => {
  return await DefaultService.postQuestionQuestionSubmitListPage({
    current,
    pageSize,
  });
};
