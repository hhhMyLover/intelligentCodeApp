package com.wzh.intelligentcodeapp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.wzh.intelligentcodeapp.constant.UserConstant;
import com.wzh.intelligentcodeapp.exception.BusinessException;
import com.wzh.intelligentcodeapp.exception.ErrorCode;
import com.wzh.intelligentcodeapp.model.entity.User;
import com.wzh.intelligentcodeapp.mapper.UserMapper;
import com.wzh.intelligentcodeapp.model.enums.UserRoleEnum;
import com.wzh.intelligentcodeapp.model.request.UserListRequest;
import com.wzh.intelligentcodeapp.model.request.UserLoginRequest;
import com.wzh.intelligentcodeapp.model.request.UserRegisterRequest;
import com.wzh.intelligentcodeapp.model.vo.LoginUserVO;
import com.wzh.intelligentcodeapp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户 服务层实现。
 *
 * @author wzh
 * @since 2025-11-26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Override
    public Long userRegister(UserRegisterRequest userRegisterRequest) {
        // 1. 校验参数
        String userAccount = userRegisterRequest.getUserAccount();
        String userPassword = userRegisterRequest.getUserPassword();
        String checkPassword = userRegisterRequest.getCheckPassword();
        if (StrUtil.hasEmpty(userAccount, userPassword, checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        if (!userPassword.equals(checkPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "两次输入的密码不一致");
        }
        // 2. 用户是否存在
        QueryWrapper userAccountWrapper = new QueryWrapper().eq(User::getUserAccount, userAccount);
        long count = this.mapper.selectCountByQuery(userAccountWrapper);
        if (count > 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户已存在");
        }

        // 3. 加密密码
        String encryptPassword = encryptPassword(userPassword);

        // 4. 保存用户
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        user.setUserName(userAccount);
        user.setUserRole(UserRoleEnum.USER.getRole());
        boolean insert = this.save(user);
        if (!insert) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "注册失败");
        }
        return user.getId();
    }

    @Override
    public String encryptPassword(String password) {
        final String SALT = "wzh";
        return DigestUtils.md5DigestAsHex((SALT + password).getBytes(StandardCharsets.UTF_8));
    }

    @Override
    public User userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request) {
        // 1. 校验参数
        String userAccount = userLoginRequest.getUserAccount();
        String userPassword = userLoginRequest.getUserPassword();
        if (StrUtil.hasEmpty(userAccount, userPassword)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "参数为空");
        }
        // 2. 查找是否存在该用户
        QueryWrapper isExitUser = new QueryWrapper().eq(User::getUserAccount, userAccount);
        User user = this.mapper.selectOneByQuery(isExitUser);
        if (user == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "用户不存在");
        }
        // 3. 密码匹配
        String encryptedPassword = encryptPassword(userPassword);
        if (!encryptedPassword.equals(user.getUserPassword())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "密码错误");
        }
        // 4. 返回用户信息
        request.getSession().setAttribute(UserConstant.USER_LOGIN_STATUS.getValue(), user);
        return user;
    }

    @Override
    public LoginUserVO getUserVO(User user) {
        LoginUserVO loginUserVO = new LoginUserVO();
        if (user == null) {
            return null;
        }
        BeanUtil.copyProperties(user, loginUserVO);
        return loginUserVO;
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATUS.getValue());
        if (ObjectUtil.isEmpty(user) || user.getId() == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        User userById = this.mapper.selectOneById(user.getId());
        if (ObjectUtil.isEmpty(userById)) {
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return userById;
    }

    @Override
    public Boolean userLogout(HttpServletRequest request) {
        try {
            User user = (User) request.getSession().getAttribute(UserConstant.USER_LOGIN_STATUS.getValue());
            if (ObjectUtil.isEmpty(user) || user.getId() == null){
                throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
            }
            request.getSession().removeAttribute(UserConstant.USER_LOGIN_STATUS.getValue());
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public List<LoginUserVO> getUserVoList(List<User> list) {
        if (CollUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        return list.stream().map(this::getUserVO).toList();
    }

    @Override
    public QueryWrapper getUserListQueryWrapper(UserListRequest request) {
        if (request == null) {
            return new QueryWrapper();
        }
        User user = new User();
        BeanUtil.copyProperties(request, user);
        return QueryWrapper.create(user);
    }
}
