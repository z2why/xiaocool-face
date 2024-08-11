package com.cool.user.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.cool.user.mapper.EchartsMapper;
import com.cool.user.service.IEchartsService;

import entity.UserDetail;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class EchartsServiceImpl extends ServiceImpl<EchartsMapper, UserDetail> implements IEchartsService {
    @Resource
    private EchartsMapper echartsMapper;

    public Integer findAllNum(String meetingId,String value){
        Integer Allnum = echartsMapper.Allnum(value);

        if(!"".equals(meetingId)){
            return Allnum.intValue();
        }
        return 0;
    }


    public Integer findAlrNum(String meetingId,String alrnum){
        Integer ALRnum = echartsMapper.ALRnum(alrnum,meetingId);

        if(!"".equals(meetingId)){
            return ALRnum.intValue();
        }
        return 0;
    }

    public Integer findLATERnum(String meetingId,String later){
        Integer LATERnum = echartsMapper.LATERnum(later,meetingId);

        if(!"".equals(meetingId)){
            return LATERnum.intValue();
        }
        return 0;
    }

    public Integer findEarlynum(String meetingId,String early){
        Integer Earlynum = echartsMapper.Earlynum(early,meetingId);

        if(!"".equals(meetingId)){
            return Earlynum.intValue();
        }
        return 0;
    }


}
