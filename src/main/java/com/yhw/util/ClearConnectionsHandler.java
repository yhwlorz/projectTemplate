package com.yhw.util;

import org.apache.http.conn.HttpClientConnectionManager;

/**
 * 自定义定时清理无效链接(httpclient)
 * @autho 董杨炀
 * @time 2017年5月8日 下午3:16:59
 */
public class ClearConnectionsHandler extends Thread{

    private final HttpClientConnectionManager connMgr;

    private volatile boolean shutdown;

    public ClearConnectionsHandler(HttpClientConnectionManager connMgr) {
        this.connMgr = connMgr;
        // 启动当前线程
        this.start();
    }

    @Override
    public void run() {
        try {
            while (!shutdown) {
                synchronized (this) {
                    wait(5000);
                    // 关闭失效的连接
                    connMgr.closeExpiredConnections();
                }
            }
        } catch (InterruptedException ex) {
            // 结束
        }
    }

    public void shutdown() {
        shutdown = true;
        synchronized (this) {
            notifyAll();
        }
    }

}
