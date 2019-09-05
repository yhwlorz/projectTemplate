package com.yhw.common.webDriver;

import io.appium.java_client.android.AndroidDriver;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * The type App operation.
 *
 * 如果是多线程在测试一个app的不同功能
 * 那在方法A开一个线程得到的是一个operation，在方法B得到的也是这个operation，那得到的就会是同一个driver
 *
 * 因此不支持多线程操作
 *
 * 要支持多线程，就要能同时存在多个driver，就要存在多个operation，就需要operation是new出来的
 * 但，一个case往往都是好几个类，new一个operation，多类之间交互就很麻烦。
 * 先不考虑多线程
 *
 * @author 杨怀伟
 */
public class AppOperation {
    //--------------------------- 静态变量 -----------------------------//

    Logger logger = Logger.getLogger(AppOperation.class);

    private static AppOperation appOperation;
    public AndroidDriver driver;

    //appium server url
    private String serverUrlStr = "http://127.0.0.1:4723/wd/hub";



    //--------------------------- 构造方法 -----------------------------//

    //不允许在外部实例化
    private AppOperation(){
        //初始化一个driver，并不指定app
        setAndroidDriver();
    }

    //--------------------------- 静态方法 -----------------------------//

    //获取静态实例
    public static AppOperation getAppOperation() {

        if (appOperation == null) {
            synchronized (AppOperation.class) {
                if (appOperation == null) {
                    appOperation = new AppOperation();
                }
            }
        }
        return appOperation;
    }

    /**
     * Sets android driver.
     * 设置dirver
     *
     */
    public void setAndroidDriver() {

        URL serverUrl = null;
        try {
            serverUrl = new URL(serverUrlStr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            logger.error("new Url() 有误");
            System.exit(0);
        }
        DesiredCapabilities capabilities = setCapabilities();
         this.driver = new AndroidDriver (serverUrl, capabilities);
    }

    /**
     * Gets device and appmessage.
     * 设置设备和app信息参数,并告知appiumService
     * <p>
     * 其实很多默认的没必要写的，但因为是刚开始，都写上吧
     */
    private DesiredCapabilities setCapabilities() {
        //////////////////////设置设备和app相关的信息////////////////////
        DesiredCapabilities capabilities = new DesiredCapabilities ();

        //Appium使用的测试引擎	 。 必填：否。 Appium（默认）
        capabilities.setCapability("automationName", "Appium");


        //测试设备类型（测试Android时被忽略）。 必填：否 。null（默认）
        capabilities.setCapability("deviceName", "device");
        //被测设备的系统平台。 必填：是。 iOS，Android，Firefox OS，null（默认）
        capabilities.setCapability("platformName", "Android");
        //手机系统版本 。 必填：否。  如6.6.1,null（默认）
        capabilities.setCapability("platformVersion", "5.1.1");



        /*
         app包的路径 (absolute返回绝对路径) 。  必填：否。  null（默认）

        优先级高于app package

        .apk、.ipa或包含apk或ipa的.zip文件的本地绝对路径或远程http URL。
        Appium将尝试在适当的设备上安装这个应用程序的二进制文件。
        和browserName不兼容。
        */
       // capabilities.setCapability("app", new File("F:\\360Downloads\\xuexi_android_10002068.apk").getAbsolutePath());

        //待测试的app的Java package。比如com.example.android.myApp 。必填：是。指向设置页面，一般都是这个包路径。不设置自动打开，就不会自动打开
        capabilities.setCapability("appPackage", "com.android.settings");
        //被测APP启动的Activity名称。比如MainActivity、.Settings。注意，原生app的话要在activity前加个”.“。必填：是
        capabilities.setCapability("appActivity", ".Settings");

        //如果测试的是移动浏览器则使用，需要对应的driver 。 必填：否 。设置为Safari在测iOS和Chrome时,设置为Browser在测Android时
        //capabilities.setCapability("browserName", "chrome");
        //直接切换到WebView上下文	 。 必填：否。 false（默认）
        capabilities.setCapability("autoWebview", false);

        //Appium是否需要自动安装和启动应用。如果为否，则不会自动启动上述应用
        capabilities.setCapability("autoLaunch", false);
        //每次启动时覆盖session，否则第二次后运行会报错不能新建session
        capabilities.setCapability("sessionOverride", true);
        //在一个Session开始前不重置被测程序的状态	。不要重复安装 . 必填：否。 false（默认）
        capabilities.setCapability("noReset", true);
        //完全重置（Android通过卸载程序的方式），Session完成后会卸载程序 . 必填：否。 false（默认）
        capabilities.setCapability("fullReset", false);

        //Appium服务器等待Appium客户端发送新消息的时间，单位为s 。 必填：否。 60（默认）
        capabilities.setCapability("newCommandTimeout", 300);
        //以毫秒为单位，等待 AVD 完成启动动画的超时时间。(默认值 120000)
        capabilities.setCapability("avdReadyTimeout", 120000);


        //是否支持Unicode的键盘，如果输入中文，设置为是。 必填：否。 false（默认）
        capabilities.setCapability("unicodeKeyboard", true);
        //在设定了 `unicodeKeyboard` 关键字的 Unicode 测试结束后，重置输入法到原有状态。如果单独使用，将会被忽略。必填：否
        capabilities.setCapability("resetKeyboard", true);

        //不需要重新签名
        capabilities.setCapability("noSign", true);

        //截图存放的路径。 必填：否。 false（默认）./data/local/tmp（默认）
        //capabilities.setCapability("androidScreenshotPath", "./data/local/tmp");
        return capabilities;
    }

    //关闭driver
    public void quertDriver(int waitTime) {
        try {
            Thread.sleep(waitTime * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        this.driver.quit();

    }

    //相对坐标  根据传入的坐标和当前坐标系，返回一个坐标
//    #设定系数(三星S5)
//    a = 940.0/1080
//    b = 443.0/1920
//
//            # 获取当前手机屏幕大小x,y,并点击此坐标
//            x = self.driver.get_window_size()['width']
//    y = self.driver.get_window_size()['height']
//    x1 = int(x * a)
//    y1 = int(y * b)
//        self.driver.swipe(x1, y1, x1, y1, 1)
//            ---------------------

    public void sleep(int sleepTime) {
        try {
            Thread.sleep (sleepTime);
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }
    }
}