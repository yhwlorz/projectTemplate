package com.yhw.service.demo;

import com.yhw.common.dataSource.DynamicDataSourceHolder;
import com.yhw.dao.demo.DemoDao;
import com.yhw.domain.demo.DemoEntity;
import com.yhw.domain.demo.DemoEntity2;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service //必须有的注解
public class DemoService {

    @Autowired   //必须有的注解，用@Resource也行
    private DemoDao dao;

    private DemoEntity demoEntity = new DemoEntity ();

    public DemoService(){

    }

    Logger logger = Logger.getLogger (this.getClass ());

    public String getReason(int vcode){

        DynamicDataSourceHolder.setDdType (DynamicDataSourceHolder.DATASOURCE_TAKEOUT_PRE);

        System.out.println ("完成数据源设置："+DynamicDataSourceHolder.DATASOURCE_TAKEOUT_PRE);

        System.out.println ("即将调用dao.getReasonByVcode");
        demoEntity = dao.getReasonByVcode (vcode);
        System.out.println ("完成调用dao.getReasonByVcode");

        System.out.println (demoEntity.getVname ());
       // DynamicDataSourceHolder.removeDBType ();

        DynamicDataSourceHolder.removeDBType ();
        DynamicDataSourceHolder.setDdType (DynamicDataSourceHolder.DATASOURCE_ACCOUNT_PRE);

        String name = dao.getUserNameById ("8f67736a5a4e44fe83331e13b5010b84");

        System.out.println (name);

        return demoEntity.getVname ();

    }

    public void updateResonCode(int vcode){

        dao.updateReasonByVcode (vcode);
    }

    public String getUserName(String id){

        DynamicDataSourceHolder.setDdType (DynamicDataSourceHolder.DATASOURCE_ACCOUNT_PRE);

        String username = dao.getUserNameById (id);

        return username;

    }

    public String getReason0(int vcode){

        DynamicDataSourceHolder.setDdType (DynamicDataSourceHolder.DATASOURCE_TAKEOUT_PRE);


        String s = dao.getName (vcode);




        return s;

    }

    public String getReason1(DemoEntity demoEntity){

        DynamicDataSourceHolder.setDdType (DynamicDataSourceHolder.DATASOURCE_TAKEOUT_PRE);


        String s = dao.getName1 (demoEntity);


               DynamicDataSourceHolder.removeDBType ();


        return s;

    }

    public String getReason2(DemoEntity demoEntity, DemoEntity2 demoEntity2){

        DynamicDataSourceHolder.setDdType (DynamicDataSourceHolder.DATASOURCE_TAKEOUT_PRE);


        String s = dao.getName2 (demoEntity,demoEntity2);


        DynamicDataSourceHolder.removeDBType ();


        return s;

    }


    public StringBuffer getName3(Map<Object,Object> params) {
        StringBuffer s =new StringBuffer (dao.getName3 (params));

        return s;
    }
    public String getName4(List<Object> params) {
        String s =dao.getName4 (params);

        return s;
    }

}
