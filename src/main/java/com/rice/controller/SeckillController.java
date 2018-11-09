package com.rice.controller;

import com.rice.dto.Exposer;
import com.rice.dto.SeckillExecution;
import com.rice.dto.SeckillResult;
import com.rice.entity.Seckill;
import com.rice.enums.SeckillStatEnum;
import com.rice.exception.RepeatKillException;
import com.rice.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.List;

/**
 * @author wangfan
 * @Date 2018-08-06 19:31
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController {
    private Logger logger = LoggerFactory.getLogger(SeckillController.class);
    @Autowired
    private SeckillService seckillService;

    @RequestMapping("/list")
    public String getSeckillList(Model model){
        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list",list);
       return "list";
    }
    @RequestMapping("/{id}/detail")
    public String getById(@PathVariable("id") Long id, Model model){
        if (id == null) {
            return "redirect:/seckill/list";
        }
        Seckill seckill = seckillService.getById(id);
        if (seckill == null) {
            return "forward:/seckill/list";   //为了测试redirect和forward区别
        }
        model.addAttribute("seckill", seckill);
        return "detail";
    }

    /**
     * 暴露秒杀接口
     * @param seckillId
     * @return
     */
    @RequestMapping(value = "/{seckillId}/exposer")
    @ResponseBody
    public SeckillResult exposerSeckillUrl(long seckillId){
        SeckillResult<Exposer> seckillResult;
        try{
            Exposer exposer = seckillService.exposerSeckillUrl(seckillId);
            seckillResult = new SeckillResult(true,exposer);
        }catch (Exception e){
            logger.error(e.getMessage(),e);
            seckillResult = new SeckillResult(false,e.getMessage());
        }
        return seckillResult;
    }

    /**
     * 执行秒杀操作
     * @param seckillId
     * @param md5
     * @param userPhone
     * @return
     */
    @RequestMapping(value = "/{seckillId}/{md5}/execution")
    @ResponseBody
    public SeckillResult execute(@PathVariable("seckillId") Long seckillId, @PathVariable("md5") String md5, @CookieValue("userPhone") Long userPhone){
        if(userPhone == null){
            return new SeckillResult(false,"用户未登录") ;
        }
        try {
           SeckillExecution execution =  seckillService.executeSeckill(seckillId,userPhone,md5);
           return new SeckillResult(true,execution);
        } catch (RepeatKillException e) {
            SeckillExecution seckillExecution = new SeckillExecution(seckillId,SeckillStatEnum.REPEAT_KILL);
            return  new SeckillResult(false,seckillExecution);
        }catch (Exception e) {
            logger.error(e.getMessage(),e);
            SeckillExecution seckillExecution = new SeckillExecution(seckillId,SeckillStatEnum.INNER_ERROR);
            return  new SeckillResult(false,seckillExecution);
        }
    }

    /**
     * 获取当前系统时间
     * @return
     */
    @RequestMapping(value="/time/now")
    @ResponseBody
    public SeckillResult<Long> time(){
        Date now = new Date();
        return  new SeckillResult<Long>(true,now.getTime());
    }

}
