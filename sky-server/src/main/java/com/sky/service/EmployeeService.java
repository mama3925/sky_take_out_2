package com.sky.service;

import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import com.sky.result.PageResult;

public interface EmployeeService {

    /**
     * 员工登录
     * @param employeeLoginDTO
     * @return
     */
    Employee login(EmployeeLoginDTO employeeLoginDTO);

    /**
     *
     * @author: xuwuyuan
     * @date: 2024/7/26 17:07
     * @desc: 新增员工
     *
     * @param employeeDTO
     * @return: void
     */
    void save(EmployeeDTO employeeDTO);

    /**
     *
     * @author: xuwuyuan
     * @date: 2024/7/27 9:38
     * @desc: 员工分页查询
     *
     * @param employeePageQueryDTO
     * @return: com.sky.result.PageResult
     */
    PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     *
     * @author: xuwuyuan
     * @date: 2024/7/27 12:21
     * @desc: 员工启用停用
     *
     * @param status
     * @param id
     * @return: void
     */
    void startOrStop(int status, Long id);

}
