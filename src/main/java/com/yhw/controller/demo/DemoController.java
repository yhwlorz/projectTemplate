package com.yhw.controller.demo;

import com.yhw.service.demo.DemoService;
import com.yhw.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * The type Demo controller.
 *
 * @author 杨怀伟
 * @date 2018 -11-13 23:40:30
 */
@Controller //必须有的注解
@RequestMapping(value = "/test")
public class DemoController {

    /**
     * @date 2018 -11-13 23:40:30
     * @author 杨怀伟  The Demo service.
     */

    @Autowired  //必须有的注解
    private DemoService demoService;

    @Autowired
    RedisUtil redisUtil;

    /**
     * Index string.
     *
     * @return the string
     */
    @ResponseBody // //有这个注解，页面才能显示返回的值，否则页面直接报错
    @RequestMapping(value="/tack")
    public void tackToGetReason(){

        String reason = demoService.getReason (102);

        System.out.println (reason);

        System.out.println (1);

    }

    /**
     * Test 2 string.
     *
     * @return the string
     */
     @ResponseBody // //有这个注解，页面才能显示返回的值，否则页面直接报错
    @RequestMapping(value="/account")
    public void accountToGetName(){


         String name = demoService.getUserName ("89e802e049e7433a913c9b99f4434543");

         System.out.println (name);


         }

         @RequestMapping(value = "/redis" )
    public void redisTest() {
            System.out.println (redisUtil.getString ("yhw_name"));
    }


    @RequestMapping(value = "/getName")
    public void getName(){
        System.out.println (demoService.getReason0 (2));
    }
}
