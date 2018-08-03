package com.rice.service;

import com.rice.dao.SeckillDao;
import com.rice.dao.SuccessKilledDao;
import com.rice.dto.SeckillExecution;
import com.rice.entity.Seckill;
import com.rice.entity.SuccessKilled;
import com.rice.enums.SeckillStatEnum;
import com.rice.exception.RepeatKillException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author wangfan
 * @Date 2018-07-26 16:24
 */
@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger= LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,4);
    }
    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    @Transactional
    //声明该方法为一个事务，要不全部执行完提交，要不就都不执行
    public SeckillExecution executeSeckill(long seckillId, long userPhone) throws Exception {
        Date nowDate = new Date();
        //减库存+增加明细记录
        int updateCount = seckillDao.reduceNumber(seckillId,nowDate);
        if(updateCount > 0){
            int insertCount = successKilledDao.insertSuccessKilled(seckillId,userPhone);
            if (insertCount > 0) {
                //秒杀成功的话，返回秒杀成功信息
                SuccessKilled successKilled = successKilledDao.queryByIdAndUser(seckillId,userPhone);
                return new SeckillExecution(seckillId,SeckillStatEnum.SUCCESS,successKilled);
            }else {
                throw new RepeatKillException("重复秒杀");
            }
        }else{
            throw new Exception("秒杀结束");
        }
    }
}
