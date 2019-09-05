package com.yhw.util;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * 配置服务端地址和缓存策略
 * 从连接池中获取jedis.
 *
 * @author 杨怀伟
 * @date 2018 -12-29 11:07:32
 */
public class JedisPoolUtils {

    private static JedisPool pool;

    //静态代码块,随着类的加载而执行
    static{
        initPool ();
    }
    //配置连接池
    private static void createJedisPool() {

        // 建立连接池配置参数
        JedisPoolConfig poolConfig = new JedisPoolConfig ();
        // 设置最大连接数.-1，则表示不限制
        poolConfig.setMaxTotal (50);
        // 设置最大空闲连接数
        poolConfig.setMaxIdle (200);
        // 设置最大阻塞时间，如果超过等待时间，则直接抛出JedisConnectionException；记住是毫秒数milliseconds.-1,表示永不超时
        poolConfig.setMaxWaitMillis (3000);

        //服务端信息
        String host = "r-bp1a75abffc67374.redis.rds.aliyuncs.com";
        int port = 6379;
        //应该是和setMaxWaitMillis一样的
        int timeout = 3000;
        String password = "2lUP6Z1zzUjUUJQQrbXoR0gx2Q";
        pool = new JedisPool (poolConfig, host, port, timeout, password);

    }

    //初始化连接池(同步)
    private static synchronized void initPool() {
        if (pool == null) {
            createJedisPool ();
        }
    }

    //获得jedis对象
    public static Jedis getJedis() {
        Jedis jedis = pool.getResource ();
        return jedis;
    }

    //归还一个连接
    public static void returnResource(Jedis jedis) {
        pool.returnResource (jedis);
    }


}
