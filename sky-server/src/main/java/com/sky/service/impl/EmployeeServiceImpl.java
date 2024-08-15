package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.PasswordConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.exception.AccountLockedException;
import com.sky.exception.AccountNotFoundException;
import com.sky.exception.FieldMissingException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.EmployeeMapper;
import com.sky.result.PageResult;
import com.sky.service.EmployeeService;
import org.aspectj.bridge.Message;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

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
        else if (!password.equals(employee.getPassword()))
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);
        else if (employee.getStatus() == 0) throw new AccountLockedException(MessageConstant.ACCOUNT_LOCKED);

        return employee;
    }

    /**
     * @param employeeDTO
     * @author: xuwuyuan
     * @date: 2024/7/26 17:09
     * @desc: 新增员工业务层
     * @return: void
     */
    @Override
    public void save(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        BeanUtils.copyProperties(employeeDTO, employee);//复制DTO对象里有的属性

        //但对于DTO对象里没有的属性，需要自己赋值
        employee.setStatus(StatusConstant.ENABLE);//新创建用户默认处于激活状态
        employee.setPassword(DigestUtils.md5DigestAsHex(PasswordConstant.DEFAULT_PASSWORD.getBytes()));

        //有了aop功能后，已经编写切面类，实现公共字段自动填充，所以不需要下面这四行代码了
        /*employee.setCreateTime(LocalDateTime.now());//创建时间字段
        employee.setUpdateTime(LocalDateTime.now());//修改时间字段，xuwuyuan 2024/07/27 19:25 之前这里搞错了，应该是setUpdateTime,不是setCreateTime
        employee.setCreateUser(BaseContext.getCurrentId());//xuwuyuan 2024/07/26 21:19 取出localthread里的当前线程员工id局部变量，以此知道是哪个用户动了employee表
        employee.setUpdateUser(BaseContext.getCurrentId());//同上*/

        employeeMapper.insert(employee);
    }

    /**
     * @param employeePageQueryDTO
     * @author: xuwuyuan
     * @date: 2024/7/27 9:40
     * @desc: 员工分页查询实现类
     * @return: com.sky.result.PageResult
     */
    @Override
    public PageResult pageQuery(EmployeePageQueryDTO employeePageQueryDTO) {
        PageHelper.startPage(employeePageQueryDTO.getPage(), employeePageQueryDTO.getPageSize()); //调用持久层前先调取PageHelper的startPage方法，以此实现分页功能
        Page<Employee> page = employeeMapper.pageQuery(employeePageQueryDTO);
        Long total = page.getTotal();//总条数
        List<Employee> object = page.getResult();//返回查询到的员工列表集合
        return new PageResult(total, object);
    }

    /**
     * @param status
     * @param id
     * @author: xuwuyuan
     * @date: 2024/7/27 12:22
     * @desc: 员工启用和停用的实现类
     * @return: void
     */
    @Override
    public void startOrStop(int status, Long id) {
        Employee employee = Employee.builder()
                .id(id)
                .status(status) //这里只动状态字段
                .build();
        employeeMapper.update(employee);
    }

    /**
     * @param id
     * @author: xuwuyuan
     * @date: 2024/7/27 16:32
     * @desc: 使用用户id获取
     * @return: com.sky.entity.Employee
     */
    @Override
    public Employee getUserById(Long id) {
        Employee employee = employeeMapper.getUserById(id);
        employee.setPassword("****");//回显密码加密，防止个人信息泄露
        return employee;
    }

    /**
     * @param employeeDTO
     * @author: xuwuyuan
     * @date: 2024/7/27 17:10
     * @desc: 修改员工信息
     * @return: void
     */
    @Override
    public void update(EmployeeDTO employeeDTO) {
        Employee employee = new Employee();
        if (employeeDTO.getId() == null) throw new FieldMissingException(MessageConstant.FIELD_MISSING);//如果DTO对象缺了id域，直接抛错
        BeanUtils.copyProperties(employeeDTO, employee);

        //有了aop功能后，已经编写切面类，实现公共字段自动填充，所以不需要下面这两行代码了
        /*//及时更新修改时间和修改人
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(BaseContext.getCurrentId());*/

        employeeMapper.update(employee);
    }

    /**
     * @param passwordEditDTO
     * @author: xuwuyuan
     * @date: 2024/7/27 18:51
     * @desc: 修改密码
     * @return: void
     */
    @Override
    public void updatePasswd(PasswordEditDTO passwordEditDTO) {
// 这一行不需要了，换用建造者模式        Employee employee = new Employee();

        //从传入DTO对象提取出旧密码和新密码
        String newPasswd = passwordEditDTO.getNewPassword();
        String oldPasswd = passwordEditDTO.getOldPassword();

        Employee employeeDB = employeeMapper.getUserById(passwordEditDTO.getEmpId());//数据库中查找DTO对象对应的记录
        oldPasswd = DigestUtils.md5DigestAsHex(oldPasswd.getBytes());//DTO对象的密码需要经过md5加密后，才能对应数据库里的密码字段。原因是数据库里的密码在初始插入时都做了md5加密处理。
        if (employeeDB.getPassword().equals(oldPasswd)) {
            // 2024/08/13发现，当时没有源码，我的实现方法不够好
//            employee.setId(passwordEditDTO.getEmpId());//必须给employee传值对象一个id值，不然之后mapper的update方法无法工作。因为update方法利用id来确定需要更改的记录
//            employee.setPassword(DigestUtils.md5DigestAsHex(newPasswd.getBytes())); //将临时密码传值对象的密码属性改成新密码，并md5加密
//            //有了aop功能后，已经编写切面类，实现公共字段自动填充，所以不需要下面这两行代码了
//            /*employee.setUpdateTime(LocalDateTime.now());//修改时间字段
//            employee.setUpdateUser(BaseContext.getCurrentId());//从LocalThread局部变量里面得到修改人的用户id，以此作为"修改人"字段*/

            // 2024/08/13 22:02给出新写法，使用建造者模式构建数据封装对象
            Employee employee = Employee.builder()
                    .id(passwordEditDTO.getEmpId())
                    .password(DigestUtils.md5DigestAsHex(newPasswd.getBytes()))
                    .build();
            employeeMapper.update(employee);
        } else {
            throw new PasswordErrorException(MessageConstant.PASSWORD_ERROR);//老密码错误，系统拒绝更改密码。所以抛出错误给全局异常工具类
        }
    }

}
