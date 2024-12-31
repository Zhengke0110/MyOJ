package fun.timu.doj.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import fun.timu.doj.common.ErrorCode;
import fun.timu.doj.exception.BusinessException;
import fun.timu.doj.mapper.UserMapper;
import fun.timu.doj.model.entity.User;
import fun.timu.doj.model.vo.LoginUserVO;
import fun.timu.doj.model.vo.UserVO;
import fun.timu.doj.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import static fun.timu.doj.constant.UserConstant.USER_LOGIN_STATE;

@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    public static final String SALT = "Doj";// 盐值，混淆密码
    private Lock lock = new ReentrantLock(); // 读写锁

    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户账号过短");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户密码过短");
        }
        // 密码和校验密码相同
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        // 上锁保证原子性
        lock.lock();
        try {
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("userAccount", userAccount);
            long count = this.baseMapper.selectCount(queryWrapper);
            if (count > 0) throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号重复");

            // 2. 加密
            String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

            // 3. 插入数据
            User user = new User();
            user.setUserAccount(userAccount);
            user.setUserPassword(encryptPassword);
            boolean saveResult = this.save(user);
            if (!saveResult) {
                throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败，数据库错误");
            }
            return user.getId();
        } finally {
            lock.unlock();
        }
    }

    @Override
    public LoginUserVO userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        // 1. 校验
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        if (userAccount.length() < 4) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "账号错误");
        }
        if (userPassword.length() < 8) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }

        // 2. 加密
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes());

        // 查询用户是否存在
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = this.baseMapper.selectOne(queryWrapper);

        // 用户不存在
        if (user == null) {
            log.info("user login failed, userAccount cannot match userPassword");
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在或密码错误");
        }

        // 3. 记录用户的登录态（Redis）
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        return this.getLoginUserVO(user);
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        // 1. 先判断是否已登录
        Object userObj = request.getSession().getAttribute(USER_LOGIN_STATE);
        User currentUser = (User) userObj;
        if (currentUser == null || currentUser.getId() == null) throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);

        // 2. 从数据库查询
        long userId = currentUser.getId();
        currentUser = this.getById(userId);
        if (currentUser == null) throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        return currentUser;
    }

    @Override
    public User getLoginUserPermitNull(HttpServletRequest request) {
        return null;
    }

    @Override
    public boolean isAdmin(HttpServletRequest request) {
        return false;
    }

    @Override
    public boolean isAdmin(User user) {
        return false;
    }

    @Override
    public boolean userLogout(HttpServletRequest request) {
        if (request.getSession().getAttribute(USER_LOGIN_STATE) == null)
            throw new BusinessException(ErrorCode.OPERATION_ERROR, "未登录");

        // 移除登录状态
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return true;
    }

    @Override
    public LoginUserVO getLoginUserVO(User user) {
        if (user == null) return null;
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    @Override
    public UserVO getUserVO(User user) {
        if (user == null) return null;

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }

    @Override
    public List<UserVO> getUserVO(List<User> userList) {
        if (CollUtil.isEmpty(userList)) return new ArrayList<>();
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }
}
