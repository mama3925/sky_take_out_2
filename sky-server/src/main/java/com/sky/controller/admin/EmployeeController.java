package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
import com.sky.dto.PasswordEditDTO;
import com.sky.entity.Employee;
import com.sky.properties.JwtProperties;
import com.sky.result.PageResult;
import com.sky.result.Result;
import com.sky.service.EmployeeService;
import com.sky.utils.JwtUtil;
import com.sky.vo.EmployeeLoginVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 员工管理
 */
@RestController
@RequestMapping("/admin/employee")
@Slf4j
@Api(tags = "员工相关接口")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JwtProperties jwtProperties;

    /**
     * 登录
     *
     * @param employeeLoginDTO
     * @return
     */
    @PostMapping("/login")
    @ApiOperation("员工登录")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("管理端员工登录:{}", employeeLoginDTO);
        Employee employee = employeeService.login(employeeLoginDTO);//通过数据模型查询数据库

        //jwt功能，这里完成的是创建token令牌并返回给前端
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        //作为视图对象返回，这里的VO对象多了token属性，用于实现后续jwt令牌校验功能
        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .userName(employee.getUsername())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }


    /**
     * @author: xuwuyuan
     * @date: 2024/7/26 16:46
     * @desc: 员工退出登录
     * @return: com.sky.result.Result<java.lang.String>
     */
    @PostMapping("/logout")
    @ApiOperation("退出登录")
    public Result<String> logout() {
        log.info("员工退出登录");
        return Result.success();
    }

    /**
     * @param employeeDTO
     * @author: xuwuyuan
     * @date: 2024/7/26 17:06
     * @desc: 新增员工
     * @return: com.sky.result.Result
     */
    @PostMapping//这里没加参数，因为类上面的requestMapping已经制定了正确路径，这里不用多加
    @ApiOperation("新增员工")
    public Result addEmployee(@RequestBody EmployeeDTO employeeDTO) {
        log.info("新增员工:{}", employeeDTO);
        employeeService.save(employeeDTO);
        return Result.success();
    }

    /**
     * @param employeePageQueryDTO
     * @author: xuwuyuan
     * @date: 2024/7/27 9:02
     * @desc: 员工分页查询
     * @return: com.sky.result.Result<com.sky.result.PageResult>
     */
    @GetMapping("/page")
    @ApiOperation("员工分页查询")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("员工分页查询开始:{}", employeePageQueryDTO);
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);//返回分页结果对象
    }

    /**
     * @param status
     * @param id
     * @author: xuwuyuan
     * @date: 2024/7/27 12:18
     * @desc: 员工启用停用
     * @return: com.sky.result.Result
     */
    @PostMapping("/status/{status}")
    @ApiOperation("启用停用员工账号")
    public Result startOrStop(@PathVariable int status, Long id) {
        log.info("员工状态设置启用或停用: {}", id);
        employeeService.startOrStop(status, id);
        return Result.success();
    }

    /**
     * @param id
     * @author: xuwuyuan
     * @date: 2024/7/27 16:27
     * @desc: 员工回显，可理解为编辑员工的子功能。先回显，之后再修改编辑
     * @return: com.sky.result.Result<com.sky.entity.Employee>
     */
    @GetMapping("/{id}")
    @ApiOperation("根据id查询员工")
    public Result<Employee> getUserById(@PathVariable Long id) {
        log.info("根据id查询员工:{}", id);
        Employee employee = employeeService.getUserById(id);
        return Result.success(employee);
    }

    /**
     * @author: xuwuyuan
     * @date: 2024/7/27 17:06
     * @desc: 编辑员工信息
     * @param employeeDTO
     * @return: com.sky.result.Result
     */
    @PutMapping
    @ApiOperation("编辑员工信息")
    public Result updateUser(@RequestBody EmployeeDTO employeeDTO) {
        log.info("编辑员工信息开始:{}", employeeDTO);
        employeeService.update(employeeDTO);
        return Result.success();
    }

    /**
     * @author: xuwuyuan
     * @date: 2024/7/27 17:47
     * @desc: 修改用户密码
     * @param passwordEditDTO
     * @return: com.sky.result.Result
     */
    @PutMapping("/editPassword")
    @ApiOperation("修改密码")
    public Result updatepasswd(@RequestBody PasswordEditDTO passwordEditDTO) {
        log.info("修改密码开始:{}", passwordEditDTO);
        employeeService.updatePasswd(passwordEditDTO);
        return Result.success();
    }
}
