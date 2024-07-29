package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.AutoFillConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.OperationType;
import lombok.extern.slf4j.Slf4j;
import nonapi.io.github.classgraph.utils.Join;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * @author: xuwuyuan
 * @desc: 公共字段填充切面类
 * @create: 2024/7/29 10:31
 **/
@Component
@Aspect
@Slf4j
public class AutoFillAspect {

    /**
     * @author: xuwuyuan
     * @date: 2024/7/29 10:53
     * @desc: 切点表达式，确定目标对象里的目标方法(连接点)
     * @return: void
     */
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)") //这里注意execution最后的(..)
    public void autoFillPointCut() {} //我用了private，但还是依照源码用public，这样的话其他切面类也可以访问这个公共字段连接点方法。

    /**
     * @param joinPoint
     * @author: xuwuyuan
     * @date: 2024/7/29 10:54
     * @desc: 切点+通知=切面。这就是一个切面，它在持久层insert或者update方法之前自动运行。之后使用反射获取传入参数信息，然后调用相关方法，实现公共字段填充
     * @return: void
     */
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) {
        log.info("填充公共字段开始...");

        //分别获取方法签名，方法上AutoFill类型注解,value()属性值
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        OperationType operationType = autoFill.value();//类型为enum类型的UPDATE或者INSERT

        //对传入的第一个参数进行反射操作，获取方法并调用
        Object[] args = joinPoint.getArgs();
        if (args == null || args.length == 0) return;//此时根本没传入任何一个参数，那就没必要做后续业务实现。
        Object entity = args[0]; //取连接点的第一个参数，作为传入对象

        //先获取用户id和当前时间
        Long userId = BaseContext.getCurrentId();
        LocalDateTime time = LocalDateTime.now();

        if (operationType == OperationType.INSERT) {//运行的mapper方法是insert类型，也就是AutoFill注解的value()值为INSERT，这时我们要写入四个域，包括创建和更新
            try {
                //获取Method对象，后续调用实体类中由@Data注解产生的set方法
                Method setCreateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_USER, Long.class);
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                Method setCreateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_CREATE_TIME, LocalDateTime.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);

                //invoke方法来实现四个字段的公共赋值
                setCreateTime.invoke(entity, time);
                setUpdateTime.invoke(entity, time);
                setCreateUser.invoke(entity, userId);
                setUpdateUser.invoke(entity, userId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (operationType == OperationType.UPDATE) {////运行的mapper方法是update类型，也就是AutoFill注解的value()值为UPDATE，这时我们要写入两个域，只涉及更新
            try {
                Method setUpdateUser = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_USER, Long.class);
                Method setUpdateTime = entity.getClass().getDeclaredMethod(AutoFillConstant.SET_UPDATE_TIME, LocalDateTime.class);

                setUpdateTime.invoke(entity, time);
                setUpdateUser.invoke(entity, userId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
