package com.rice.service;

import com.rice.dao.SeckillDao;
import com.rice.dao.SuccessKilledDao;
import com.rice.dto.Exposer;
import com.rice.dto.SeckillExecution;
import com.rice.entity.Seckill;
import com.rice.entity.SuccessKilled;
import com.rice.enums.SeckillStatEnum;
import com.rice.exception.RepeatKillException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.util.Date;
import java.util.List;

/**
 * @author wangfan
 * @Date 2018-07-26 16:24
 */
@Service
@Scope("singleton")
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    //md盐值字符串，混淆
    private final String salt = "dasdasdafafaukfh.jpi7o2o;3ip;'''''''135";

    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0, 4);
    }

    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    @Override
    public Exposer exposerSeckillUrl(long seckillId) {
        //todo 在缓存超时的基础上维护一致性
        //1.先去缓存中找
        //Seckill seckill = redisDao.getSeckill(seckillId);
        Seckill seckill = null;
        if (seckill == null) {
            //2.缓存中没有则去DB里面找
            seckill = seckillDao.queryById(seckillId);
            if (seckill == null) {
                return new Exposer(false, seckillId);
            } else {
                //3.从数据库取出来之后再放入缓存
                // redisDao.putSeckill(seckill);
            }
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();
        if (nowTime.getTime() < startTime.getTime()
                || nowTime.getTime() > endTime.getTime()) {
            return new Exposer(false, seckillId, nowTime.getTime(), startTime.getTime(), endTime.getTime());
        }
        //不可逆的转化md5字符串
        String md5 = getMD5(seckillId);//TODO
        return new Exposer(true, md5, seckillId);
    }

    private String getMD5(long seckillId) {
        String base = seckillId + "/" + salt;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    @Override
    @Transactional
    //注解方式声明该方法为一个事务，要不全部执行完提交，要不就都不执行（声明事务还可以用xml方式）
    public SeckillExecution executeSeckill(Long seckillId, Long userPhone, String md5) throws Exception {
        if (md5 == null || !md5.equals(getMD5(seckillId))) {
            return new SeckillExecution(seckillId,SeckillStatEnum.DATE_REWRITE);
        }
        Date nowDate = new Date();
        //减库存+增加明细记录
        int updateCount = seckillDao.reduceNumber(seckillId, nowDate);
        if (updateCount > 0) {
            int insertCount = successKilledDao.insertSuccessKilled(seckillId, userPhone);
            if (insertCount > 0) {
                //秒杀成功的话，返回秒杀成功信息
                SuccessKilled successKilled = successKilledDao.queryByIdAndUser(seckillId, userPhone);
                return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, successKilled);
            } else {
                throw new RepeatKillException("重复秒杀");
            }
        } else {
            throw new Exception("秒杀结束");
        }
    }
}
