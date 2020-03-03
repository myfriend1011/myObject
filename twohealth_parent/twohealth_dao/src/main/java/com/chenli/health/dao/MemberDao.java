package com.chenli.health.dao;

import com.chenli.health.pojo.Member;

public interface MemberDao {
    Member find_memeberByTelephone(String telephone);

    void add_member(Member memeber);

    Integer find_memberCountByMonth(String m);

    Integer findMemberCountByDate(String today);

    Integer findMemberTotalCount();

    Integer findMemberCountAfterDate(String thisWeekMonday);
}
