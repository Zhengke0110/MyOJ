package fun.timu.doj.controller;

import fun.timu.doj.common.BaseResponse;
import fun.timu.doj.common.ErrorCode;
import fun.timu.doj.common.ResultUtils;
import fun.timu.doj.exception.BusinessException;
import fun.timu.doj.model.dto.user.UserLoginRequest;
import fun.timu.doj.model.dto.user.UserRegisterRequest;
import fun.timu.doj.model.entity.User;
import fun.timu.doj.model.vo.LoginUserVO;
import fun.timu.doj.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     *
     * @param userRegisterRequest
     * @return
     */
    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) throw new BusinessException(ErrorCode.PARAMS_ERROR);
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) return null;
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        if (userLoginRequest == null) throw new BusinessException(ErrorCode.PARAMS_ERROR);

        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword)) throw new BusinessException(ErrorCode.PARAMS_ERROR);
        LoginUserVO loginUserVO = userService.userLogin(userAccount, userPassword, request);
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 用户登出
     *
     * @param request
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        System.out.println("request=>" + request.toString());
        if (request == null) throw new BusinessException(ErrorCode.PARAMS_ERROR);
        boolean result = userService.userLogout(request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前登录用户
     *
     * @param request
     * @return
     */
    @GetMapping("/get/login")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        System.out.println("getLoginUser user=>"+user.toString());
        return ResultUtils.success(userService.getLoginUserVO(user));
    }

    /*
@PostMapping("/add")
@AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
public BaseResponse<Long> addUser(@RequestBody UserAddRequest userAddRequest, HttpServletRequest request) {
*/

//    @PostMapping("/delete")
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
//    public BaseResponse<Boolean> deleteUser(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request) {

//    @PostMapping("/update")
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
//    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest userUpdateRequest,

//    @GetMapping("/get")
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
//    public BaseResponse<User> getUserById(long id, HttpServletRequest request) {

//    @GetMapping("/get/vo")
//    public BaseResponse<UserVO> getUserVOById(long id, HttpServletRequest request) {

//    @PostMapping("/list/page")
//    @AuthCheck(mustRole = UserConstant.ADMIN_ROLE)
//    public BaseResponse<Page<User>> listUserByPage(@RequestBody UserQueryRequest userQueryRequest,
//                                                   HttpServletRequest request) {

//    @PostMapping("/list/page/vo")
//    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest userQueryRequest,
//                                                       HttpServletRequest request) {

//    @PostMapping("/update/my")
//    public BaseResponse<Boolean> updateMyUser(@RequestBody UserUpdateMyRequest userUpdateMyRequest,
//                                              HttpServletRequest request) {
}

