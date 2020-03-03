package com.chenli.health.serviceinterface;

import com.chenli.health.entity.reuntmessageresult.PageResult;
import com.chenli.health.pojo.CheckItem;

import java.util.List;

public interface CheckItemServiceInterface {

    //添加数据
    void add_checkItem(CheckItem checkItem);

    //
    PageResult find_CheckItem(Integer currentPage, Integer pageSize, String queryString);

    void delete_checkItem(Integer id);

    CheckItem find_checkItemById(Integer id);

    void edit_checkItem(CheckItem checkItem);

    List<CheckItem> find_checkItems();

}
