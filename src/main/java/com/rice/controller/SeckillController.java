package com.rice.controller;

import com.rice.entity.Seckill;
import com.rice.service.SeckillService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * @author wangfan
 * @Date 2018-08-06 19:31
 */
@Controller
@RequestMapping("/seckill")
public class SeckillController {
    @Autowired
    private SeckillService seckillService;
    @RequestMapping("/list")
    public String getSeckillList(Model model){
        List<Seckill> list = seckillService.getSeckillList();
        model.addAttribute("list",list);
       return "list";
    }
    @RequestMapping("/{id}/detail")
    public String getById(@PathVariable Long id, Model model){
        Seckill seckill = seckillService.getById(id);
        model.addAttribute("seckill",seckill);
        return "detail";
    }
}
