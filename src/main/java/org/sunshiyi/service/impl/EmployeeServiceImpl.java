package org.sunshiyi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.sunshiyi.entity.Employee;
import org.sunshiyi.mapper.EmployeeMapper;
import org.sunshiyi.service.EmployeeService;
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

}
