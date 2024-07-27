package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.entity.Employee;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface EmployeeMapper {

    /**
     * 根据用户名查询员工
     *
     * @param username
     * @return
     */
    @Select("select * from employee where username = #{username}")
    Employee getByUsername(String username);

    /**
     * 插入员工实体
     *
     * @param employee
     * @return
     */
    @Insert("insert into employee "
            + "values " +
            "(#{id}, #{name}, #{username}, #{password}, #{phone}, #{sex}, #{idNumber}, #{status}, #{createTime}, #{updateTime}, #{createUser}, #{updateUser})")
    void insert(Employee employee);

    /**
     * @param employeePageQueryDTO
     * @author: xuwuyuan
     * @date: 2024/7/27 9:53
     * @desc: 分页查询并返回Employee类型的Page对象
     * @return: com.github.pagehelper.Page<com.sky.entity.Employee>
     */
    Page<Employee> pageQuery(EmployeePageQueryDTO employeePageQueryDTO);

    /**
     * @param employee
     * @author: xuwuyuan
     * @date: 2024/7/27 12:24
     * @desc: 改员工表
     * @return: void
     */
    void update(Employee employee);

    /**
     * @param id
     * @author: xuwuyuan
     * @date: 2024/7/27 16:45
     * @desc: 根据id进行用户表查表
     * @return: com.sky.entity.Employee
     */
    @Select("select * from employee where id = #{id}")
    Employee getUserById(Long id);

}
