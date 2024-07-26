package com.sky.service.impl;

import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.service.EmployeeService;
import org.aspectj.bridge.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

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
}
