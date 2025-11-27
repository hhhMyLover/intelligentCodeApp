package com.wzh.intelligentcodeapp.aop;

import com.wzh.intelligentcodeapp.annotation.AuthCheck;
import com.wzh.intelligentcodeapp.exception.BusinessException;
import com.wzh.intelligentcodeapp.exception.ErrorCode;
import com.wzh.intelligentcodeapp.model.entity.User;
import com.wzh.intelligentcodeapp.model.enums.UserRoleEnum;
import com.wzh.intelligentcodeapp.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Component
public class AuthInterceptor {

    @Resource
    private UserService userService;

    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String mustRole = authCheck.mustRole();
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes == null){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR);
        }
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
        User loginUser = userService.getLoginUser(request);
        UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByRole(mustRole);
        if (mustRoleEnum == null) {
           return joinPoint.proceed();
        }
        if (mustRoleEnum.getRole().equals(loginUser.getUserRole())) {
            return joinPoint.proceed();
        }
        throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
    }

}
