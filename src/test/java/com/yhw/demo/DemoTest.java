package com.yhw.demo;

import com.yhw.service.demo.DemoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;

@Rollback
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-mvc.xml","classpath:spring-mybatis.xml","classpath:spring-redis.xml"})
public class DemoTest {

    @Autowired
    DemoService demoService;

    @Test
    public void get() {
      //  System.out.println (demoService.getReason0 (101));

        //System.out.println (demoService.getUserName ("006e9240ccf64dcbaa72a419120f49a8"));

       /* DemoEntity demoEntity = new DemoEntity ();
        demoEntity.setVcode (101);
        System.out.println (demoService.getReason1 (demoEntity));*/


      /*  DemoEntity demoEntity = new DemoEntity ();
        DemoEntity2 demoEntity2 = new DemoEntity2 ();
        demoEntity.setVcode (22222);
        demoEntity2.setPkGroup ("5ab1087dcef803000fe0518b");
        System.out.println (demoService.getReason2 (demoEntity,demoEntity2));*/

        Map<Object, Object> params = new HashMap<> ();
        params.put ("pkGroup", "5ab1087dcef803000fe0518b");
        params.put ("vcode", 22222);
        System.out.println (demoService.getName3 (params));

      /*  List<Object> params = new ArrayList<> ();
        DemoEntity demoEntity = new DemoEntity ();
        demoEntity.setVcode (22222);
        DemoEntity2 demoEntity2 = new DemoEntity2 ();
        demoEntity2.setPkGroup ("5ab1087dcef803000fe0518b");

        params.add (demoEntity);
        params.add (demoEntity2);

        System.out.println (demoService.getName4 (params));*/
    }



}
