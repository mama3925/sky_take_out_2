package com.sky.mapper;

import com.github.pagehelper.Page;
import com.sky.dto.CategoryPageQueryDTO;
import com.sky.entity.Category;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author: xuwuyuan
 * @desc: 分类管理持久层
 * @create: 2024/7/28 10:47
 **/
@Mapper
public interface CategoryMapper {

    /**
     * @author: xuwuyuan
     * @date: 2024/7/28 16:52
     * @desc: 插入分类
     * @param category
     * @return: void
     */
    @Insert("insert into category values (#{id}, #{type}, #{name}, #{sort}, #{status},"+
            "#{createTime}, #{updateTime}, #{createUser}, #{updateUser})")//注意这个语法要和update区分，后面values()括号里不需要等号赋值
    void insert(Category category);

    /**
     * @author: xuwuyuan
     * @date: 2024/7/28 15:46
     * @desc: 分页查询。具体实现到ressources里的CategoryMapper.xml去看
     * @param categoryPageQueryDTO
     * @return: com.github.pagehelper.Page<com.sky.entity.Category>
     */
    Page<Category> pageQuery(CategoryPageQueryDTO categoryPageQueryDTO);

    /**
     * @author: xuwuyuan
     * @date: 2024/7/28 15:54
     * @desc: 根据id删除分类记录
     * @param id
     * @return: void
     */
    @Delete("delete from category where id = #{id}")
    void deleteById(Long id);//这个方法名适合用deleteById，而非delete。因为要突出靠id这个请求参数来删除分类

    /**
     * @author: xuwuyuan
     * @date: 2024/7/28 16:01
     * @desc: 根据传入实体对象category来更新各项属性的值。具体实现到ressources里的CategoryMapper.xml去看
     * @param category
     * @return: void
     */
    void update(Category category);

    /**
     * @author: xuwuyuan
     * @date: 2024/7/28 16:11
     * @desc: 通过int类型的type形参来得到该类型的Category列表。用于前端下拉菜单显示
     * @param type
     * @return: java.util.List<com.sky.entity.Category>
     */
    @Select("select * from category where type = #{type}")
    List<Category> getListByType(Integer type);
}
