package com.wzh.intelligentcodeapp.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.paginate.Page;
import com.wzh.intelligentcodeapp.annotation.AuthCheck;
import com.wzh.intelligentcodeapp.common.BaseResponse;
import com.wzh.intelligentcodeapp.common.ResultUtils;
import com.wzh.intelligentcodeapp.exception.ErrorCode;
import com.wzh.intelligentcodeapp.exception.ThrowUtils;
import com.wzh.intelligentcodeapp.model.request.UserListRequest;
import com.wzh.intelligentcodeapp.model.request.UserLoginRequest;
import com.wzh.intelligentcodeapp.model.request.UserRegisterRequest;
import com.wzh.intelligentcodeapp.model.vo.LoginUserVO;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.wzh.intelligentcodeapp.model.entity.User;
import com.wzh.intelligentcodeapp.service.UserService;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户 控制层。
 *
 * @author wzh
 * @since 2025-11-26
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("register")
    public BaseResponse<Long> userRegister(@RequestBody UserRegisterRequest userRegisterRequest) {
        ThrowUtils.throwIf(ObjectUtil.isEmpty(userRegisterRequest), ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(userService.userRegister(userRegisterRequest));
    }

    @PostMapping("login")
    public BaseResponse<LoginUserVO> userLogin(@RequestBody UserLoginRequest userLoginRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(ObjectUtil.isEmpty(userLoginRequest), ErrorCode.PARAMS_ERROR);
        User user = userService.userLogin(userLoginRequest, request);
        return ResultUtils.success(userService.getUserVO(user));
    }

    @GetMapping("getLoginUser")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request) {
        User user = userService.getLoginUser(request);
        return ResultUtils.success(userService.getUserVO(user));
    }

    @PostMapping("logout")
    public BaseResponse<Boolean> userLogout(HttpServletRequest request) {
        return ResultUtils.success(userService.userLogout(request));
    }

    /**
     * 保存用户。
     *
     * @param user 用户
     * @return {@code true} 保存成功，{@code false} 保存失败
     */
    @PostMapping("save")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> save(@RequestBody User user) {
        ThrowUtils.throwIf(ObjectUtil.isEmpty(user), ErrorCode.PARAMS_ERROR);
        // TODO 参数校验
        return ResultUtils.success(userService.save(user));
    }

    /**
     * 根据主键删除用户。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<Boolean> remove(@PathVariable Long id) {
        ThrowUtils.throwIf(ObjectUtil.isEmpty(id), ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(userService.removeById(id));
    }

    /**
     * 根据主键更新用户。
     *
     * @param user 用户
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public BaseResponse<Boolean> update(@RequestBody User user) {
        ThrowUtils.throwIf(ObjectUtil.isEmpty(user), ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(userService.updateById(user));
    }

    /**
     * 查询所有用户。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    @AuthCheck(mustRole = "admin")
    public BaseResponse<List<LoginUserVO>> list() {
        List<User> list = userService.list();
        return ResultUtils.success(userService.getUserVoList(list));
    }

    /**
     * 根据主键获取用户。
     *
     * @param id 用户主键
     * @return 用户详情
     */
    @GetMapping("getInfo/{id}")
    public BaseResponse<LoginUserVO> getInfo(@PathVariable Long id) {
        ThrowUtils.throwIf(ObjectUtil.isEmpty(id), ErrorCode.PARAMS_ERROR);
        User user = userService.getById(id);
        return ResultUtils.success(userService.getUserVO(user));
    }

    /**
     * 分页查询用户。
     *
     * @param userListRequest userListRequest
     * @return 分页对象
     */
    @PostMapping("page")
    public BaseResponse<Page<LoginUserVO>>page(@RequestBody UserListRequest userListRequest) {
        Page<User> userPage = userService.page(Page.of(userListRequest.getCurrent(), userListRequest.getPageSize()), userService.getUserListQueryWrapper(userListRequest));
        List<User> records = userPage.getRecords();
        List<LoginUserVO> loginUserVOList = userService.getUserVoList(records);
        // 创建新的 Page<LoginUserVO> 对象
        Page<LoginUserVO> voPage = new Page<>(userPage.getPageNumber(), userPage.getPageSize(), userPage.getTotalRow());
        voPage.setRecords(loginUserVOList);
        return ResultUtils.success(voPage);
    }

}
