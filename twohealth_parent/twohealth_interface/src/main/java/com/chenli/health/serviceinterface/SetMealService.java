package com.chenli.health.serviceinterface;

import com.chenli.health.entity.reuntmessageresult.PageResult;
import com.chenli.health.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetMealService {
    void add_setMeal(Setmeal setmeal, Integer[] checkgroupIds);

    PageResult find_setMealPage(Integer currentPage, Integer pageSize, String queryString);

    Setmeal find_setMealById(Integer id);

    List<Integer> find_setMealAndCheckGroupByIds(Integer id);

    void edit_setMeal(Setmeal setmeal, Integer[] checkgroupIds);

    void delete_setMealById(Integer id);

    List<Setmeal> find_setMealAll();

    Setmeal find_setmealById(Integer id);

    List<Map<String, Object>> findSetmealCount();
}
