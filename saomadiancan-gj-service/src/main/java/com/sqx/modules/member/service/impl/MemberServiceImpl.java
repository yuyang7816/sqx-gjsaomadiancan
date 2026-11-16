package com.sqx.modules.member.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sqx.common.utils.PageUtils;
import com.sqx.common.utils.Result;
import com.sqx.modules.languageType.service.LanguageTypeService;
import com.sqx.modules.member.dao.MemberDao;
import com.sqx.modules.member.entity.Member;
import com.sqx.modules.member.service.MemberService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class MemberServiceImpl extends ServiceImpl<MemberDao, Member> implements MemberService {

    @Resource
    private LanguageTypeService languageTypeService;

    @Override
    public Result insertMember(Member member){
        baseMapper.insert(member);
        return Result.success();
    }

    @Override
    public Result updateMember(Member member){
        baseMapper.updateById(member);
        return Result.success();
    }

    @Override
    public Result deleteMemberById(Long memberId){
        baseMapper.deleteById(memberId);
        return Result.success();
    }

    @Override
    public Result selectMemberList(String languageType){
        List<Member> memberList = baseMapper.selectList(new QueryWrapper<Member>().eq(StringUtils.isNotBlank(languageType), "language_type", languageType).orderByAsc("sort"));
        languageTypeService.fillLanguageName(memberList);
        return Result.success().put("data", memberList);
    }

    @Override
    public Result selectMemberPage(Integer page, Integer limit, String languageType){
        IPage<Member> pages=new Page<>(page,limit);
        IPage<Member> memberIPage = baseMapper.selectPage(pages, new QueryWrapper<Member>().orderByAsc("sort"));
        languageTypeService.fillLanguageName(memberIPage.getRecords());
        return Result.success().put("data",new PageUtils(memberIPage));
    }



}







