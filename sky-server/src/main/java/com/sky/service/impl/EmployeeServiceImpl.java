package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.service.EmployeeService;
import org.aspectj.bridge.Message;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeMapper employeeMapper;

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @Override
    public Employee login(EmployeeLoginDTO employeeLoginDTO) {
        //获取输入的用户名和密码
        String password = employeeLoginDTO.getPassword();
        String username = employeeLoginDTO.getUsername();
        password = DigestUtils.md5DigestAsHex(password.getBytes());//对密码进行md5加密，使其与数据库里的记录一致

        //调取员工
        Employee employee = employeeMapper.getByUsername(username);

        //员工不存在，或者密码错误，或者数据库里员工状态为锁定，此时拒绝登录
        if (employee == null) throw new AccountNotFoundException(MessageConstant.ACCOUNT_NOT_FOUND);
        else if (!password.equals(employee.getPassword())) throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        else if (employee.getStatus() == 0) throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);

        return employee;
    }

    /**
     *
     * @author: xuwuyuan
     * @date: 2024/7/26 17:09
     * @desc: 新增员工业务层
     *
     * @param employeeDTO
     * @return: void
     */
    @Override
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);//复制DTO对象里有的属性

        //但对于DTO对象里没有的属性，需要自己赋值
        employee.setStatus(StatusConstant.ENABLE);//新创建用户默认处于激活状态
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));
        employee.setCreateTime(LocalDateTime.now());
        employee.setCreateTime(LocalDateTime.now());
        employee.setCreateUser(BaseContext.getCurrentId());//xuwuyuan 2024/07/26 21:19 取出localthread里的当前线程员工id局部变量，以此知道是哪个用户动了employee表
        employee.setUpdateUser(BaseContext.getCurrentId());//同上

        employeeMapper.insert(employee);
    }
}
