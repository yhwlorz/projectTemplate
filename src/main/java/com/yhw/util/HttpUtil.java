package com.yhw.util;


import lombok.Data;
import org.apache.http.Consts;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 这是和spring集成在一起的httpClient处理工具类。由spring注入httpClient和config
 *
 * 使用方法：
 * ①@ContextConfiguration(locations = {"classpath:spring-httpClient.xml"})
 * ②@Autowired  HttpUtil httpUtil;
 *
 * @author 杨怀伟
 * @date 2019 -05-10 16:11:21
 */
@Data
public class HttpUtil {
    Logger logger = Logger.getLogger (this.getClass ());

    /*
    <bean id="httpUtil" class="com.yhw.util.HttpUtil">
    <property name="httpClient" ref="httpClient"/>
    <property name="requestConfig" ref="requestConfig"/>
    </bean>*/
    CloseableHttpClient httpClient;
    //已经在spring-httclient 配置了上述bean。当注入httpUtil时，会自动将此属性与 requestConfigbean 相关联
    RequestConfig requestConfig;


    /**
     * get 请求 ，无参数
     *
     * @return the string
     */
    public String doGet(String url) {

        //1.实例化 get request
        HttpGet request = new HttpGet (url);

        //2.构造请求
        //2.1 连接超时等设置
        request.setConfig (requestConfig);

        //3 建立连接、执行请求、处理响应
        String responseEntity2String = request2Response (request);

        return responseEntity2String;

    }
    /**
     * 有参的 get
     *
     * @return the string
     */
    public String doGet(String url,Map<String,String> paras) {

        String responseEntity2String = "";
        try {
            URIBuilder uriBuilder = new URIBuilder (url);
            for (Map.Entry<String,String> para : paras.entrySet()) {
                uriBuilder.addParameter(para.getKey(), para.getValue());
            }
            responseEntity2String = doGet(uriBuilder.build().toString());
        } catch (URISyntaxException e) {
            e.printStackTrace();
            logger.error("uri拼接时出错");
        }

        return responseEntity2String;

    }


    /**
     * 无参的 POSt.
     *
     * @param url the url
     * @return the string
     */
    public String doPostForm(String url) {

        return doPostForm(url, null);
    }
    public <T> String doPostForm(String url, Map<T,Object> paras) {
        return doPostForm(url, null,paras);
    }
    /**
     * POST请求，content-type = form
     *
     * @param url        the url
     * @param
     * @return the string
     */
    public <T> String doPostForm(String url, String cookie,Map<T,Object> paras) {

        //1 实例化 post request
        HttpPost request = new HttpPost (url);

        //2 构造请求内容
        //2.1 连接超时等设置
        request.setConfig (requestConfig);
        //2.2 请求头
        request.setHeader ("Content-Type","application/x-www-form-urlencoded");
        if (cookie != null) {
            request.setHeader ("Cookie", cookie);
        }
        //2.3 请求实体
        if (paras != null && paras.size()>0) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair> ();
            for (Map.Entry<T,Object> para : paras.entrySet()) {
                nameValuePairs.add(new BasicNameValuePair (String.valueOf (para.getKey()),String.valueOf(para.getValue())));
            }
            HttpEntity requestEntity = new UrlEncodedFormEntity (nameValuePairs, Consts.UTF_8);// 构造一个form表单式的实体
            request.setEntity (requestEntity);
        }

        //3 建立连接、执行请求、处理响应
        String responseEntity2String = request2Response (request);

        return responseEntity2String;
    }


    /**
     * POST请求，content-type = json
     *
     * @param url        the url
     * @param jsonString the json string
     * @return the string
     */
    public String doPostJson(String url,String jsonString) {
        return doPostJson (url, null, jsonString);
    }
    public String doPostJson(String url) {
        return doPostJson (url, null, "");
    }
    public String doPostJson(String url,String cookie,String jsonString) {

        //1 实例化 post request
        HttpPost request = new HttpPost (url);

        //2 构造请求内容
            //2.1 连接超时等设置
        request.setConfig (requestConfig);
            //2.2 请求头
        request.setHeader ("Content-Type","application/json");
        if (cookie!=null) {//已验证，如此设置cookie是可行的 。例cookie = "JSESSIONID=7afcfcfd-6f76-4755-b4b7-541bb5d1d143"
            request.setHeader ("Cookie", cookie);
        }
        //2.3 请求实体
        StringEntity requestEntity = new StringEntity (jsonString, ContentType.APPLICATION_JSON);
        //requestEntity.setContentType ("application/json");
        request.setEntity (requestEntity);

        //3 建立连接、执行请求、处理响应
       String responseEntity2String = request2Response (request);

        return responseEntity2String;
    }




    /**
     * 执行请求并处理返回
     *
     *已验证，无论返回的是String，map还是对象都可被正确处理
     *
     * @return the string
     */
    private String request2Response(HttpRequestBase request) {
        //3 建立连接，执行请求，得到响应
        HttpResponse response = null;
        try { response = httpClient.execute (request);
        } catch (IOException io) {
            logger.error ("执行请求出错");
            return null;
        }

        //4 解析请求 reponse中可以获得code、entity==
        String responseEntity2String = "";
        if (response != null && response.getStatusLine ().getStatusCode () == 200) {
            HttpEntity responseEntity = response.getEntity ();
            long length = responseEntity.getContentLength ();
            try {
              if (length!=-1 && length < 2048) { //EntityUtils的使用是强烈不鼓励的
                    responseEntity2String = EntityUtils.toString (responseEntity);
                } else {
                    InputStream inputStream = responseEntity.getContent ();
                    InputStreamReader inputStreamReader = new InputStreamReader (inputStream);
                    BufferedReader bufferedReader = new BufferedReader (inputStreamReader);

                    StringBuffer stringBuffer = new StringBuffer ();
                    String lineStr;
                    while ((lineStr = bufferedReader.readLine ()) != null) {
                        stringBuffer.append (lineStr);
                    }
                    responseEntity2String = stringBuffer.toString ();
                }
            } catch (IOException e) {
                e.printStackTrace ();
                logger.error ("解析响应出错");
                return null;
            }
        } else {
            System.out.println (response.getStatusLine ().getStatusCode ());
        }

        return responseEntity2String;
    }
}


