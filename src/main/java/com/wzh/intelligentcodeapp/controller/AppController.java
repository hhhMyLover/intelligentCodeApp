package com.wzh.intelligentcodeapp.controller;

import cn.hutool.core.util.ObjectUtil;
import com.mybatisflex.core.paginate.Page;
import com.wzh.intelligentcodeapp.annotation.AuthCheck;
import com.wzh.intelligentcodeapp.common.BaseResponse;
import com.wzh.intelligentcodeapp.common.ResultUtils;
import com.wzh.intelligentcodeapp.exception.ErrorCode;
import com.wzh.intelligentcodeapp.exception.ThrowUtils;
import com.wzh.intelligentcodeapp.model.entity.User;
import com.wzh.intelligentcodeapp.model.request.app.AppAddRequest;
import com.wzh.intelligentcodeapp.model.request.app.AppAdminUpdateRequest;
import com.wzh.intelligentcodeapp.model.request.app.AppListRequest;
import com.wzh.intelligentcodeapp.model.request.app.AppUpdateRequest;
import com.wzh.intelligentcodeapp.model.vo.AppVO;
import com.wzh.intelligentcodeapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;
import com.wzh.intelligentcodeapp.model.entity.App;
import com.wzh.intelligentcodeapp.service.AppService;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 应用 控制层。
 *
 * @author wzh
 * @since 2025-12-04
 */
@RestController
@RequestMapping("/app")
public class AppController {

    @Autowired
    private AppService appService;

    @Autowired
    private UserService userService;

    /**
     * 创建应用
     *
     * @param appAddRequest 创建应用请求
     * @param request       HttpServletRequest
     * @return 应用id
     */
    @PostMapping("add")
    @Operation(description = "创建应用")
    public BaseResponse<Long> addApp(@RequestBody AppAddRequest appAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(ObjectUtil.isEmpty(appAddRequest), ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(appService.addApp(appAddRequest, request));
    }

    /**
     * 用户更新自己的应用
     *
     * @param appUpdateRequest 更新应用请求
     * @param request          HttpServletRequest
     * @return 是否成功
     */
    @PutMapping("update")
    @Operation(description = "用户更新自己的应用")
    public BaseResponse<Boolean> updateApp(@RequestBody AppUpdateRequest appUpdateRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(ObjectUtil.isEmpty(appUpdateRequest), ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(appService.updateApp(appUpdateRequest, request));
    }

    /**
     * 用户删除自己的应用
     *
     * @param id      应用id
     * @param request HttpServletRequest
     * @return 是否成功
     */
    @DeleteMapping("delete/{id}")
    @Operation(description = "用户删除自己的应用")
    public BaseResponse<Boolean> deleteApp(@PathVariable Long id, HttpServletRequest request) {
        ThrowUtils.throwIf(ObjectUtil.isEmpty(id), ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(appService.deleteApp(id, request));
    }

    /**
     * 根据id查看应用详情
     *
     * @param id 应用id
     * @return 应用详情
     */
    @GetMapping("getInfo/{id}")
    @Operation(description = "根据id查看应用详情")
    public BaseResponse<AppVO> getInfo(@PathVariable Long id) {
        ThrowUtils.throwIf(ObjectUtil.isEmpty(id), ErrorCode.PARAMS_ERROR);
        App app = appService.getById(id);
        ThrowUtils.throwIf(ObjectUtil.isEmpty(app), ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(appService.getAppVO(app));
    }

    /**
     * 分页查询自己的应用列表
     *
     * @param appListRequest 查询请求
     * @param request        HttpServletRequest
     * @return 分页对象
     */
    @PostMapping("page/my")
    @Operation(description = "分页查询自己的应用列表")
    public BaseResponse<Page<AppVO>> pageMyApp(@RequestBody AppListRequest appListRequest, HttpServletRequest request) {
        // 获取当前登录用户
        User loginUser = userService.getLoginUser(request);
        // 设置查询条件为当前用户
        appListRequest.setUserId(loginUser.getId());
        // 每页最多20个
        int pageSize = Math.min(appListRequest.getPageSize(), 20);
        Page<App> appPage = appService.page(
                Page.of(appListRequest.getCurrent(), pageSize),
                appService.getAppListQueryWrapper(appListRequest)
        );
        List<App> records = appPage.getRecords();
        List<AppVO> appVOList = appService.getAppVOList(records);
        // 创建新的 Page<AppVO> 对象
        Page<AppVO> voPage = new Page<>(appPage.getPageNumber(), appPage.getPageSize(), appPage.getTotalRow());
        voPage.setRecords(appVOList);
        return ResultUtils.success(voPage);
    }

    /**
     * 分页查询精选应用列表
     *
     * @param appListRequest 查询请求
     * @return 分页对象
     */
    @PostMapping("page/featured")
    @Operation(description = "分页查询精选应用列表")
    public BaseResponse<Page<AppVO>> pageFeaturedApp(@RequestBody AppListRequest appListRequest) {
        // 每页最多20个
        int pageSize = Math.min(appListRequest.getPageSize(), 20);
        Page<App> appPage = appService.page(
                Page.of(appListRequest.getCurrent(), pageSize),
                appService.getFeaturedAppQueryWrapper(appListRequest)
        );
        List<App> records = appPage.getRecords();
        List<AppVO> appVOList = appService.getAppVOList(records);
        // 创建新的 Page<AppVO> 对象
        Page<AppVO> voPage = new Page<>(appPage.getPageNumber(), appPage.getPageSize(), appPage.getTotalRow());
        voPage.setRecords(appVOList);
        return ResultUtils.success(voPage);
    }

    /**
     * 管理员根据id删除任意应用
     *
     * @param id 应用id
     * @return 是否成功
     */
    @DeleteMapping("admin/delete/{id}")
    @AuthCheck(mustRole = "admin")
    @Operation(description = "管理员根据id删除任意应用")
    public BaseResponse<Boolean> adminDeleteApp(@PathVariable Long id) {
        ThrowUtils.throwIf(ObjectUtil.isEmpty(id), ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(appService.removeById(id));
    }

    /**
     * 管理员更新应用
     *
     * @param appAdminUpdateRequest 管理员更新应用请求
     * @return 是否成功
     */
    @PutMapping("admin/update")
    @AuthCheck(mustRole = "admin")
    @Operation(description = "管理员更新应用")
    public BaseResponse<Boolean> adminUpdateApp(@RequestBody AppAdminUpdateRequest appAdminUpdateRequest) {
        ThrowUtils.throwIf(ObjectUtil.isEmpty(appAdminUpdateRequest), ErrorCode.PARAMS_ERROR);
        return ResultUtils.success(appService.adminUpdateApp(appAdminUpdateRequest));
    }

    /**
     * 管理员分页查询应用列表
     *
     * @param appListRequest 查询请求
     * @return 分页对象
     */
    @PostMapping("admin/page")
    @AuthCheck(mustRole = "admin")
    @Operation(description = "管理员分页查询应用列表")
    public BaseResponse<Page<AppVO>> adminPageApp(@RequestBody AppListRequest appListRequest) {
        Page<App> appPage = appService.page(
                Page.of(appListRequest.getCurrent(), appListRequest.getPageSize()),
                appService.getAppListQueryWrapper(appListRequest)
        );
        List<App> records = appPage.getRecords();
        List<AppVO> appVOList = appService.getAppVOList(records);
        // 创建新的 Page<AppVO> 对象
        Page<AppVO> voPage = new Page<>(appPage.getPageNumber(), appPage.getPageSize(), appPage.getTotalRow());
        voPage.setRecords(appVOList);
        return ResultUtils.success(voPage);
    }

    /**
     * 管理员根据id查看应用详情
     *
     * @param id 应用id
     * @return 应用详情
     */
    @GetMapping("admin/getInfo/{id}")
    @AuthCheck(mustRole = "admin")
    @Operation(description = "管理员根据id查看应用详情")
    public BaseResponse<AppVO> adminGetInfo(@PathVariable Long id) {
        ThrowUtils.throwIf(ObjectUtil.isEmpty(id), ErrorCode.PARAMS_ERROR);
        App app = appService.getById(id);
        ThrowUtils.throwIf(ObjectUtil.isEmpty(app), ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(appService.getAppVO(app));
    }
}
