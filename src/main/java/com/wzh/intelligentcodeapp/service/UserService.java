package com.wzh.intelligentcodeapp.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.wzh.intelligentcodeapp.model.entity.User;
import com.wzh.intelligentcodeapp.model.request.UserListRequest;
import com.wzh.intelligentcodeapp.model.request.UserLoginRequest;
import com.wzh.intelligentcodeapp.model.request.UserRegisterRequest;
import com.wzh.intelligentcodeapp.model.vo.LoginUserVO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * 用户 服务层。
 *
 * @author wzh
 * @since 2025-11-26
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     *
     * @param userRegisterRequest Long  id
     */
    Long userRegister(UserRegisterRequest userRegisterRequest);

    /**
     * 密码加密
     *
     * @param password String encryptedPassword
     */
    String encryptPassword(String password);

    /**
     * 用户登录
     *
     * @param userLoginRequest
     * @param request
     * @return
     */
    User userLogin(UserLoginRequest userLoginRequest, HttpServletRequest request);

    /**
     * 脱敏用户信息
     * @param user User
     * @return LoginUserVO
     */
    LoginUserVO getUserVO(User user);

    /**
     * 根据请求头中的信息获取登录用户信息
     * @param request request
     * @return LoginUserVO
     */
    User getLoginUser(HttpServletRequest request);

    /**
     * 用户注销
     * @param request request
     *
     */
    Boolean userLogout(HttpServletRequest request);

    /**
     * 获取脱敏用户列表
     * @param list
     * @return
     */
    List<LoginUserVO> getUserVoList(List<User> list);

    /**
     * 获取用户列表查询条件
     * @return
     */
    QueryWrapper getUserListQueryWrapper(UserListRequest request);
}
