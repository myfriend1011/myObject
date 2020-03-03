package com.chenli.health.serviceinterface;

import com.chenli.health.pojo.Member;

import java.util.List;

public interface MemberService {
    Member find_memberByTelephone(String telephone);

    void add_member(Member member1);

    List<Integer> find_memberCountByMonth(List<String> list);

}
