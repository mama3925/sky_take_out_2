package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     *
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     * @param employeeDTO
     * @author: xuwuyuan
     * @date: 2024/7/26 17:07
     * @desc: 新增员工
     * @return: void
     */
    void save(EmployeeDTO employeeDTO);

    /**
     * @param employeePageQueryDTO
     * @author: xuwuyuan
     * @date: 2024/7/27 9:38
     * @desc: 员工分页查询
     * @return: com.sky.result.PageResult
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * @param status
     * @param id
     * @author: xuwuyuan
     * @date: 2024/7/27 12:21
     * @desc: 员工启用停用
     * @return: void
     */
    void startOrStop(int status, Long id);

    /**
     * @param id
     * @author: xuwuyuan
     * @date: 2024/7/27 16:31
     * @desc: 根据id获取用户
     * @return: com.sky.entity.Employee
     */
    Employee getUserById(Long id);

    /**
     * @param employeeDTO
     * @author: xuwuyuan
     * @date: 2024/7/27 17:10
     * @desc: 修改员工信息
     * @return: void
     */
    void update(EmployeeDTO employeeDTO);

    /**
     * @param passwordEditDTO
     * @author: xuwuyuan
     * @date: 2024/7/27 17:50
     * @desc: 修改密码
     * @return: void
     */
    void updatePasswd(PasswordEditDTO passwordEditDTO);

}
