package com.chenli.health.dao;

import com.chenli.health.pojo.Setmeal;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SetMealDao {
    void add_setMeal(Setmeal setmeal);

    void set_setMeal_checkgroup_guannian(@Param("id") Integer id, @Param("checkgroupId") Integer checkgroupId);

    Page<Setmeal> find_setMealPage(String queryString);

    Setmeal find_setMealById(Integer id);

    List<Integer> find_setMealAndCheckGroupByIds(Integer id);

    void deleteAssociation(Integer id);

    void edit_setMeal(Setmeal setmeal);

    Long find_setMealAndCheckGroup_count(Integer id);

    void delete_setMealById(Integer id);

    List<Setmeal> fine_setMealAll();

    Setmeal find_setmealById(Integer id);

    List<Map<String, Object>> findSetmealCount();
}
