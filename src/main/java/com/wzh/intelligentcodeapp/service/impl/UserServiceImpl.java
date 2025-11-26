package com.wzh.intelligentcodeapp.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.wzh.intelligentcodeapp.model.entity.User;
import com.wzh.intelligentcodeapp.mapper.UserMapper;
import com.wzh.intelligentcodeapp.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户 服务层实现。
 *
 * @author wzh
 * @since 2025-11-26
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>  implements UserService {

}
