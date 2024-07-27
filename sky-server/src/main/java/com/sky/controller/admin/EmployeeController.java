package com.sky.controller.admin;

import com.sky.constant.JwtClaimsConstant;
import com.sky.dto.EmployeeDTO;
import com.sky.dto.EmployeeLoginDTO;
import com.sky.dto.EmployeePageQueryDTO;
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
@Api(tags = "员工控制类")
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
    @ApiOperation("员工登录操作")
    public Result<EmployeeLoginVO> login(@RequestBody EmployeeLoginDTO employeeLoginDTO) {
        log.info("管理端员工登录");
        Employee employee = employeeService.login(employeeLoginDTO);//通过数据模型查询数据库

        //jwt功能
        Map<String, Object> claims = new HashMap<>();
        claims.put(JwtClaimsConstant.EMP_ID, employee.getId());
        String token = JwtUtil.createJWT(
                jwtProperties.getAdminSecretKey(),
                jwtProperties.getAdminTtl(),
                claims);

        //作为视图对象返回
        EmployeeLoginVO employeeLoginVO = EmployeeLoginVO.builder()
                .id(employee.getId())
                .name(employee.getName())
                .userName(employee.getUsername())
                .token(token)
                .build();

        return Result.success(employeeLoginVO);
    }


    /**
     *
     * @author: xuwuyuan
     * @date: 2024/7/26 16:46
     * @desc: 员工退出登录
     *
     * @return: com.sky.result.Result<java.lang.String>
     */
    @PostMapping("/logout")
    @ApiOperation("退出登录")
    public Result<String> logout() {
        log.info("员工退出登录");
        return Result.success();
    }

    /**
     *
     * @author: xuwuyuan
     * @date: 2024/7/26 17:06
     * @desc: 新增员工
     *
     * @param employeeDTO
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
     *
     * @author: xuwuyuan
     * @date: 2024/7/27 9:02
     * @desc: 员工分页查询
     *
     * @param employeePageQueryDTO
     * @return: com.sky.result.Result<com.sky.result.PageResult>
     */
    @GetMapping("/page")
    @ApiOperation("员工分页查询")
    public Result<PageResult> page(EmployeePageQueryDTO employeePageQueryDTO) {
        log.info("员工分页查询开始");
        PageResult pageResult = employeeService.pageQuery(employeePageQueryDTO);
        return Result.success(pageResult);
    }

    /**
     *
     * @author: xuwuyuan
     * @date: 2024/7/27 12:18
     * @desc: 员工启用停用
     *
     * @param status
     * @param id
     * @return: com.sky.result.Result
     */
    @PostMapping("/status/{status}")
    @ApiOperation("员工停用启用")
    public Result startOrStop(@PathVariable int status, Long id) {
        log.info("员工状态设置");
        employeeService.startOrStop(status, id);
        return Result.success();
    }

}
