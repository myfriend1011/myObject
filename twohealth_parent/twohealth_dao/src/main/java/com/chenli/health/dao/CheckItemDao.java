package com.chenli.health.dao;

import com.chenli.health.pojo.CheckItem;
import com.github.pagehelper.Page;

import java.util.List;

public interface CheckItemDao {


    public Page<CheckItem> find_CheckItem(String queryString);

    void add_checkItem(CheckItem checkItem);

    void delete_checkItem(Integer id);

    long find_checkItem_checkgroud_there(Integer id);

    CheckItem find_checkItemByid(Integer id);

    void edit_checkItem(CheckItem checkItem);

    List<CheckItem> find_checkItems();

    List<CheckItem> find_checkItemByids(Integer id);
}
