package com.wzh.intelligentcodeapp.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.wzh.intelligentcodeapp.ai.AiGeneratorService;
import com.wzh.intelligentcodeapp.constant.AppPriorityConstant;
import com.wzh.intelligentcodeapp.exception.BusinessException;
import com.wzh.intelligentcodeapp.exception.ErrorCode;
import com.wzh.intelligentcodeapp.model.entity.App;
import com.wzh.intelligentcodeapp.mapper.AppMapper;
import com.wzh.intelligentcodeapp.model.entity.User;
import com.wzh.intelligentcodeapp.model.enums.CodeGenTypeEnum;
import com.wzh.intelligentcodeapp.model.request.app.AppAddRequest;
import com.wzh.intelligentcodeapp.model.request.app.AppAdminUpdateRequest;
import com.wzh.intelligentcodeapp.model.request.app.AppListRequest;
import com.wzh.intelligentcodeapp.model.request.app.AppUpdateRequest;
import com.wzh.intelligentcodeapp.model.vo.AppVO;
import com.wzh.intelligentcodeapp.model.vo.LoginUserVO;
import com.wzh.intelligentcodeapp.service.AppService;
import com.wzh.intelligentcodeapp.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 应用 服务层实现。
 *
 * @author wzh
 * @since 2025-12-04
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App> implements AppService {

    @Autowired
    private UserService userService;

    @Autowired
    private AiGeneratorService aiGeneratorService;


    @Override
    public Long addApp(AppAddRequest appAddRequest, HttpServletRequest request) {
        // 1. 校验参数
        if (appAddRequest == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        String appName = appAddRequest.getAppName();
        String initPrompt = appAddRequest.getInitPrompt();
        if (StrUtil.isEmpty(initPrompt)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "initPrompt 不能为空");
        }

        // 2. 获取当前登录用户
        User loginUser = userService.getLoginUser(request);

        // 3. 创建应用
        App app = new App();
        String generateAppName = aiGeneratorService.generateAppName(initPrompt);
        app.setAppName(generateAppName);
        app.setInitPrompt(initPrompt);
        app.setUserId(loginUser.getId());
        app.setCodeGenType(CodeGenTypeEnum.MULTI_FILE.getValue());
        boolean save = this.save(app);
        if (!save) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "创建应用失败");
        }
        return app.getId();
    }

    @Override
    public Boolean updateApp(AppUpdateRequest appUpdateRequest, HttpServletRequest request) {
        // 1. 校验参数
        if (appUpdateRequest == null || appUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 2. 获取当前登录用户
        User loginUser = userService.getLoginUser(request);

        // 3. 查询应用是否存在
        App app = this.getById(appUpdateRequest.getId());
        if (app == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        }

        // 4. 校验权限（只能修改自己的应用）
        if (!app.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限修改该应用");
        }

        // 5. 更新名称
        app.setAppName(appUpdateRequest.getAppName());
        // 6. 更新修改时间
        app.setEditTime(LocalDateTime.now());
        return this.updateById(app);
    }

    @Override
    public Boolean deleteApp(Long id, HttpServletRequest request) {
        // 1. 校验参数
        if (id == null || id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 2. 获取当前登录用户
        User loginUser = userService.getLoginUser(request);

        // 3. 查询应用是否存在
        App app = this.getById(id);
        if (app == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        }

        // 4. 校验权限（只能删除自己的应用）
        if (!app.getUserId().equals(loginUser.getId())) {
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR, "无权限删除该应用");
        }

        // 5. 删除应用
        return this.removeById(id);
    }

    @Override
    public Boolean adminUpdateApp(AppAdminUpdateRequest appAdminUpdateRequest) {
        // 1. 校验参数
        if (appAdminUpdateRequest == null || appAdminUpdateRequest.getId() == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }

        // 2. 查询应用是否存在
        App app = this.getById(appAdminUpdateRequest.getId());
        if (app == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "应用不存在");
        }

        // 3. 更新应用
        if (StrUtil.isNotEmpty(appAdminUpdateRequest.getAppName())) {
            app.setAppName(appAdminUpdateRequest.getAppName());
        }
        if (StrUtil.isNotEmpty(appAdminUpdateRequest.getCover())) {
            app.setCover(appAdminUpdateRequest.getCover());
        }
        if (appAdminUpdateRequest.getPriority() != null) {
            app.setPriority(appAdminUpdateRequest.getPriority());
        }
        return this.updateById(app);
    }

    @Override
    public AppVO getAppVO(App app) {
        if (app == null) {
            return null;
        }
        AppVO appVO = new AppVO();
        BeanUtil.copyProperties(app, appVO);
        if (appVO.getUserId() != null) {
            User user = userService.getById(appVO.getUserId());
            appVO.setUser(userService.getUserVO(user));
        }
        return appVO;
    }

    @Override
    public List<AppVO> getAppVOList(List<App> list) {
        if (CollUtil.isEmpty(list)) {
            return new ArrayList<>();
        }
        Set<Long> collect = list.stream().map(App::getId).collect(Collectors.toSet());
        List<User> userList = userService.listByIds(collect);
        List<LoginUserVO> userVoList = userService.getUserVoList(userList);
        Map<Long, LoginUserVO> userMap = userVoList.stream().collect(Collectors.toMap(LoginUserVO::getId, loginUserVO -> loginUserVO));
        return list.stream().map(app -> {
            AppVO appVO = new AppVO();
            BeanUtil.copyProperties(app, appVO);
            appVO.setUser(userMap.get(app.getUserId()));
            return appVO;
        }).toList();
    }

    @Override
    public QueryWrapper getAppListQueryWrapper(AppListRequest request) {
        if (request == null) {
            return new QueryWrapper();
        }
        QueryWrapper queryWrapper = new QueryWrapper();
        // 支持根据id查询
        if (request.getId() != null) {
            queryWrapper.eq(App::getId, request.getId());
        }
        // 支持根据名称模糊查询
        if (StrUtil.isNotEmpty(request.getAppName())) {
            queryWrapper.like(App::getAppName, request.getAppName());
        }
        // 支持根据其他字段查询
        if (StrUtil.isNotEmpty(request.getCover())) {
            queryWrapper.eq(App::getCover, request.getCover());
        }
        if (StrUtil.isNotEmpty(request.getInitPrompt())) {
            queryWrapper.like(App::getInitPrompt, request.getInitPrompt());
        }
        if (StrUtil.isNotEmpty(request.getCodeGenType())) {
            queryWrapper.eq(App::getCodeGenType, request.getCodeGenType());
        }
        if (StrUtil.isNotEmpty(request.getDeployKey())) {
            queryWrapper.eq(App::getDeployKey, request.getDeployKey());
        }
        if (request.getPriority() != null) {
            queryWrapper.eq(App::getPriority, request.getPriority());
        }
        if (request.getUserId() != null) {
            queryWrapper.eq(App::getUserId, request.getUserId());
        }
        queryWrapper.orderBy(App::getCreateTime);
        return queryWrapper;
    }

    @Override
    public QueryWrapper getFeaturedAppQueryWrapper(AppListRequest request) {
        QueryWrapper queryWrapper = new QueryWrapper();
        // 精选应用：优先级不为空且大于0
        queryWrapper.isNotNull(App::getPriority)
                .gt(App::getPriority, AppPriorityConstant.DEFAULT_PRIORITY);
        // 支持根据名称模糊查询
        if (request != null && StrUtil.isNotEmpty(request.getAppName())) {
            queryWrapper.like(App::getAppName, request.getAppName());
        }
        // 按优先级降序排序
        queryWrapper.orderBy(App::getPriority, false);
        return queryWrapper;
    }
}
