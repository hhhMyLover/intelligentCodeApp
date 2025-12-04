package com.wzh.intelligentcodeapp.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.wzh.intelligentcodeapp.model.entity.App;
import com.wzh.intelligentcodeapp.model.request.app.AppAddRequest;
import com.wzh.intelligentcodeapp.model.request.app.AppAdminUpdateRequest;
import com.wzh.intelligentcodeapp.model.request.app.AppListRequest;
import com.wzh.intelligentcodeapp.model.request.app.AppUpdateRequest;
import com.wzh.intelligentcodeapp.model.vo.AppVO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
 * 应用 服务层。
 *
 * @author wzh
 * @since 2025-12-04
 */
public interface AppService extends IService<App> {

    /**
     * 创建应用
     *
     * @param appAddRequest 创建应用请求
     * @param request       HttpServletRequest
     * @return 应用id
     */
    Long addApp(AppAddRequest appAddRequest, HttpServletRequest request);

    /**
     * 用户更新自己的应用
     *
     * @param appUpdateRequest 更新应用请求
     * @param request          HttpServletRequest
     * @return 是否成功
     */
    Boolean updateApp(AppUpdateRequest appUpdateRequest, HttpServletRequest request);

    /**
     * 用户删除自己的应用
     *
     * @param id      应用id
     * @param request HttpServletRequest
     * @return 是否成功
     */
    Boolean deleteApp(Long id, HttpServletRequest request);

    /**
     * 管理员更新应用
     *
     * @param appAdminUpdateRequest 管理员更新应用请求
     * @return 是否成功
     */
    Boolean adminUpdateApp(AppAdminUpdateRequest appAdminUpdateRequest);

    /**
     * 获取应用VO
     *
     * @param app 应用
     * @return AppVO
     */
    AppVO getAppVO(App app);

    /**
     * 获取应用VO列表
     *
     * @param list 应用列表
     * @return AppVO列表
     */
    List<AppVO> getAppVOList(List<App> list);

    /**
     * 获取应用列表查询条件
     *
     * @param request 查询请求
     * @return QueryWrapper
     */
    QueryWrapper getAppListQueryWrapper(AppListRequest request);

    /**
     * 获取精选应用列表查询条件
     *
     * @param request 查询请求
     * @return QueryWrapper
     */
    QueryWrapper getFeaturedAppQueryWrapper(AppListRequest request);
}
