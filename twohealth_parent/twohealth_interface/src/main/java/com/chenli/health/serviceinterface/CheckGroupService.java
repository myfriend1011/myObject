package com.chenli.health.serviceinterface;

import com.chenli.health.entity.reuntmessageresult.PageResult;
import com.chenli.health.pojo.CheckGroup;

import java.util.List;

public interface CheckGroupService {
    void add_checkGroup(CheckGroup checkGroup, Integer[] checkItemIds);

    PageResult find_checkGroups(Integer currentPage, Integer pageSize, String queryString);

    CheckGroup find_checkGroupById(Integer id);

    List<Integer> find_checkGroup_checkItem_ByIds(Integer id);

    void edit_checkGroup(CheckGroup checkGroup, Integer[] ids);

    void delete_checkGroupById(Integer id);

    List<CheckGroup> find_checkGroupAll();

}
