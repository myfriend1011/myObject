package com.chenli.health.dao;

import com.chenli.health.pojo.CheckGroup;
import com.github.pagehelper.Page;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CheckGroupDao {
    void add_checkGroup(CheckGroup checkGroup);

    void update_checkGroupAndCheckItem(@Param("checkgroup_id")Integer checkgroup_id, @Param("checkitem_id")Integer checkitem_id);

    Page<CheckGroup> find_checkGroups(String queryString);

    CheckGroup find_checkGroupId(Integer id);

    List<Integer> find_checkGroup_checkItem_ByIds(Integer id);

    void delete_checkGroup_checkItem_guannian(Integer id);

    void edit_to_save_checkGroup(CheckGroup checkGroup);

    void setCheckGroup_checkItem_guannain(@Param("idCg") Integer idCg,@Param("idCi") Integer idCi);

    Long find_checkGroup_checkItem_count(Integer id);

    void delete_checkGroupById(Integer id);

    List<CheckGroup> find_checkGroupAll();

    public List<CheckGroup> find_checkGroupIds(int id);
}
