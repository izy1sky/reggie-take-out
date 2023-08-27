package org.sunshiyi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.sunshiyi.entity.User;
import org.sunshiyi.mapper.UserMapper;
import org.sunshiyi.service.UserService;
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
