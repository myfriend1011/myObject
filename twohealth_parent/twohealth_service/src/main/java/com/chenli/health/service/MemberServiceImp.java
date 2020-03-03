package com.chenli.health.service;

import com.alibaba.dubbo.config.annotation.Service;
import com.chenli.health.dao.MemberDao;
import com.chenli.health.pojo.Member;
import com.chenli.health.serviceinterface.MemberService;
import com.chenli.health.uintl.MD5Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MemberServiceImp implements MemberService {

    @Autowired
    private MemberDao memberDao;
    @Override
    public Member find_memberByTelephone(String telephone) {
        return memberDao.find_memeberByTelephone(telephone);
    }

    @Override
    public void add_member(Member member1) {
        if(member1.getPassword() != null){
            member1.setPassword(MD5Utils.md5(member1.getPassword()));
        }
        memberDao.add_member(member1);
    }

    @Override
    public List<Integer> find_memberCountByMonth(List<String> months) {
        List<Integer> list = new ArrayList<>();
        for(String m : months){
            m = m + "-31";//格式：2019-04-31
            Integer count = memberDao.find_memberCountByMonth(m);
            list.add(count);
        }
        return list;
    }
}
