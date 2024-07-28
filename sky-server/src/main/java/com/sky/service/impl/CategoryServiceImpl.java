package com.sky.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.sky.constant.MessageConstant;
import com.sky.constant.StatusConstant;
import com.sky.context.BaseContext;
import com.sky.dto.CategoryDTO;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import com.sky.exception.AccountLockedException;
import com.sky.exception.DeletionNotAllowedException;
import com.sky.exception.FieldMissingException;
import com.sky.exception.PasswordErrorException;
import com.sky.mapper.CategoryMapper;
import com.sky.mapper.DishMapper;
import com.sky.mapper.SetmealMapper;
import com.sky.result.PageResult;
import com.sky.service.CategoryService;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author: xuwuyuan
 * @desc: 分类业务实现类
 * @create: 2024/7/28 10:16
 **/
@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    private CategoryMapper categoryMapper;
    @Autowired
    private DishMapper dishMapper;//用于删除分类时确保该分类没有关联任何菜品
    @Autowired
    private SetmealMapper setmealMapper;//用于删除分类时确保该分类没有关联任何套餐

    /**
     * @param categoryDTO
     * @author: xuwuyuan
     * @date: 2024/7/28 12:10
     * @desc: 实现添加分类功能
     * @return: void
     */
    @Override
    public void save(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
//        category.setStatus(0);//这里我犯错了，在有常量类的情况下，应该优先考虑常量类里的静态常量。不应该自己靠经验填入基本类型，因为有可能这是个包装类型。可能会装箱拆箱失败，然后报错
        category.setStatus(StatusConstant.DISABLE);//这行为苍穹外卖源码，属于正确答案。新增加的分类状态默认为禁用

        //设置创建人，创建时间，修改人，修改时间
        category.setCreateTime(LocalDateTime.now());
        category.setUpdateTime(LocalDateTime.now());
        category.setCreateUser(BaseContext.getCurrentId());
        category.setUpdateUser(BaseContext.getCurrentId());//这里犯了小错误，当时写成了setCreateUser，和上一行重复了

        categoryMapper.insert(category);
    }

    /**
     * @param categoryPageQueryDTO
     * @author: xuwuyuan
     * @date: 2024/7/28 12:14
     * @desc: 实现分页查询功能
     * @return: com.sky.result.PageResult
     */
    @Override
    public PageResult pageQuery(CategoryPageQueryDTO categoryPageQueryDTO) {
        PageHelper.startPage(categoryPageQueryDTO.getPage(), categoryPageQueryDTO.getPageSize()); //在调用dao层之前必须先使用startPage方法
        Page<Category> page = (Page) categoryMapper.pageQuery(categoryPageQueryDTO);
        return new PageResult(page.getTotal(), page.getResult());
    }

    /**
     * @author: xuwuyuan
     * @date: 2024/7/28 12:21
     * @desc: 删除分类
     * @param id
     * @return: void
     */
    @Override
    public void deleteById(Long id) {
        //当前分类关联了菜品，不能删除
        if (dishMapper.countByCategoryId(id) > 0) //我犯错了，只能写>0，不能写!equals(0)
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_DISH);
        //当前分类关联了套餐，不能删除
        if (setmealMapper.countByCategoryId(id) > 0)//我犯错了，只能写>0，不能写!equals(0)
            throw new DeletionNotAllowedException(MessageConstant.CATEGORY_BE_RELATED_BY_SETMEAL);
        categoryMapper.deleteById(id);//这个方法名适合用deleteById，而非delete。因为要突出靠id这个请求参数来删除分类
    }

    /**
     * @author: xuwuyuan
     * @date: 2024/7/28 14:36
     * @desc: 修改分类
     * @param categoryDTO
     * @return: void
     */
    @Override
    public void update(CategoryDTO categoryDTO) {
        Category category = new Category();
        BeanUtils.copyProperties(categoryDTO, category);
        category.setUpdateTime(LocalDateTime.now());//设置当前时间作为更改时间
        category.setUpdateUser(BaseContext.getCurrentId());//设置修改人id
        categoryMapper.update(category);
    }

    /**
     * @author: xuwuyuan
     * @date: 2024/7/28 14:41
     * @desc: 启用停用分类
     * @param status
     * @param id
     * @return: void
     */
    @Override
    public void startOrStop(Integer status, Long id) {
        Category category = Category.builder()
                .id(id)
                .status(status)
                //这里苍穹外卖源码加上了修改时间和修改人，但是我认为不应该加。应该和员工启用停用一样。所以这里没加
//                .updateTime(LocalDateTime.now())
//                .updateUser(BaseContext.getCurrentId())
                .build();
        categoryMapper.update(category); //类似上面的方法（修改分类），都是调用update，只不过这里只改动数据库status字段
    }

    /**
     * @author: xuwuyuan
     * @date: 2024/7/28 16:35
     * @desc: 根据类型查询分类，得到一个Category列表
     * @param type
     * @return: java.util.List<com.sky.entity.Category>
     */
    @Override
    public List<Category> listByType(Integer type) {
        List<Category> listCategory = categoryMapper.getListByType(type); //从数据库里找对应的Category列表，使得type字段等于形式参数type值
        return listCategory;
    }

}
