package org.sunshiyi.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.sunshiyi.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
