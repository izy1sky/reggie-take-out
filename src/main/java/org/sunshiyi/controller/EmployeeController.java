package org.sunshiyi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.sunshiyi.common.R;
import org.sunshiyi.constant.PasswordConstant;
import org.sunshiyi.entity.Employee;
import org.sunshiyi.service.EmployeeService;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @PostMapping("/login")
    public R<Employee> employeeLogin(HttpServletRequest request, @RequestBody Employee employee) {
        String password = employee.getPassword();
        String digest = DigestUtils.md5DigestAsHex(password.getBytes());

        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee one = employeeService.getOne(queryWrapper);

        if (one == null) {
            return R.error("登录失败!");
        }
        if (!one.getPassword().equals(digest)) {
            return R.error("登录失败!");
        }
        if (one.getStatus() == 0) {
            return R.error("账号已禁用!");
        }
        request.getSession().setAttribute("employee", one.getId());
        return R.success(one);
    }

    @PostMapping("/logout")
    public R<String> employeeLogout(HttpServletRequest request) {
        request.getSession().removeAttribute("employee");
        return R.success("退出登录成功!");
    }

    @GetMapping("/page")
    public R<Page<Employee>> pageQuery(int page, int pageSize, String name) {
        log.info("page: {}, pageSize: {}, name: {}", page, pageSize, name);
        Page<Employee> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.hasText(name), Employee::getName, name);
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        employeeService.page(pageInfo, queryWrapper);
        pageInfo.getRecords().forEach(System.out::println);
        return R.success(pageInfo);
    }

    @PostMapping
    public R<String> addEmployee(@RequestBody Employee employee) {
        log.info("要添加的员工信息是：{}", employee);
        String password = DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes());
        employee.setPassword(password);
//        employee.setCreateTime(LocalDateTime.now());
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setCreateUser((Long) request.getSession().getAttribute("employee"));
//        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
        employeeService.save(employee);
        return R.success("添加成功！");
    }

    @PutMapping
    public R<String> update(@RequestBody Employee employee) {
//        employee.setUpdateTime(LocalDateTime.now());
//        employee.setUpdateUser((Long) request.getSession().getAttribute("employee"));
        employeeService.updateById(employee);
        return R.success("修改成功！");
    }

    @GetMapping("/{id}")
    public R<Employee> getEmployee(@PathVariable Long id) {
        Employee employee = employeeService.getById(id);
        if (employee != null) {
            return R.success(employee);
        }
        return R.error("员工信息获取失败！");
    }
}
