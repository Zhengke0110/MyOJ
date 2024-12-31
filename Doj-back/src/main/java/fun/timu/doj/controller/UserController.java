package fun.timu.doj.controller;

import fun.timu.doj.common.BaseResponse;
import fun.timu.doj.common.ErrorCode;
import fun.timu.doj.common.ResultUtils;
import fun.timu.doj.exception.BusinessException;
import fun.timu.doj.model.dto.user.UserRegisterRequest;
import fun.timu.doj.service.UserService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    @PostMapping("/register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        if (userRegisterRequest == null) throw new BusinessException(ErrorCode.PARAMS_ERROR);
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return null;
        }
        long result = userService.userRegister(userAccount, userPassword, checkPassword);
        return ResultUtils.success(result);
    }
//    @PostMapping("/login")
//    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {

//    @PostMapping("/logout")
//    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {}

//    @GetMapping("/get/login")
//    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {

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
