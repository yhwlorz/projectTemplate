package com.yhw.common.webDriver;

import com.yhw.constant.Enum.selenium.GeneralKeyEnum;
import com.yhw.constant.enumeration.normal.ColorEnum;
import com.yhw.constant.enumeration.normal.TagNameEnum;
import com.yhw.constant.enumeration.selenium.MouseOperationEnum;
import com.yhw.manage.env.webConfig.PlatformEnv;
import com.yhw.util.StackTraceUnit;
import com.yhw.util.StringUtil;
import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 页面基础类么
 * 二次封装
 *
 * @author 杨怀伟
 * @date 2019 -03-15 23:34:10
 */
public class WebOperation {

    private ChromeDriverService service;
    public WebDriver driver;

    private static final int WAITTIME = 15;

    //日志及截图存放地址
    public Logger logger = Logger.getLogger (this.getClass ());
    private String errorLogFile = "F:\\autoTestErrorLog\\screenShot\\";

    //静态对象
    public static WebOperation webOperation;

    //构造函数,不私有化，允许通过new方法获取新的service和driver
    public WebOperation() {
        setChromeDriver ();
    }

    //获得chromeDriver
    public void setChromeDriver() {
        //拼接环境变量中Chromedriver的路径
        String chromedriverpath = System.getenv ("SELENIUM_CHROMEDRIVER_HOME") + "\\chromedriver.exe";
        File chromedriverFile = new File (chromedriverpath);

        service = new ChromeDriverService.Builder ().usingDriverExecutable (chromedriverFile).usingAnyFreePort ()
                .build ();

        //开启serveice和driver
        try {
            service.start ();
            if (PlatformEnv.openChrome) {
                driver = new ChromeDriver (); //TODO 由于初始化bean时，会初始bean的属性webOperation对象。执行到此处，会打开浏览器窗口。（临时置为null，则不会再打开浏览器）
            }
            } catch (IOException e) {
            logger.warn (e + "\n" + "注释，service和driver没能开启");
            //0正常退出，非0非正常退出
            System.exit (0);
        }
    }

    //对外提供YhwOperatingElement的单例,懒加载其多线程安全
    public static WebOperation getSingleWebOperating() {
        //只要当yhwOperating为空时，才走syn，效率高
        if (webOperation == null) {

            //同步代码块，以当前类的class对象作为锁
            synchronized (WebOperation.class) {
                //再判断一次，是为了避免刚开始没有对象时有多个线程在这等着new对象
                if (webOperation == null) {
                    webOperation = new WebOperation ();
                }
            }
        }
        return webOperation;
    }

    //循环直到元素属性中的值不可见
    public void loadingUntilElAttDiff(Object xpathOrElement, String attributeKey, String attributeValue) {

        WebElement element = null;
        //如果传的是xpath,需要手动获取element
        if (xpathOrElement instanceof String) {
            //找到元素(无论有没有ctrl acv,最后传的才是elementxpath)
            element = findElementByXpath ((String) xpathOrElement);
        } else {
            element = (WebElement) xpathOrElement;
        }


        long start = System.currentTimeMillis ();

        while (true) {
            if (!element.getAttribute (attributeKey).equals (attributeValue)) {
                return;

            }
            long end = System.currentTimeMillis ();
            if (end - start > 180000) {
                logger.warn (""+this.driver.getCurrentUrl ()+"三分钟了，" + element.getTagName () + "元素属性" + attributeKey + "的值还没变化，依然是" + attributeValue +  StackTraceUnit.getStackTrace ());
                System.exit (0);
            }
        }

    }

    /**
     * Loading until attribute contains.
     * 等待直到某个元素的某个属性包含了某个值
     *
     * @param xpathOrElement the pageLocation or element
     * @param attributeKey   the attribute key
     * @param attributeValue the attribute value
     */
    public void loadingUntilElAttContains(Object xpathOrElement, String attributeKey, String attributeValue) {
        WebElement element = null;
        //如果传的是xpath,需要手动获取element
        if (xpathOrElement instanceof String) {
            //找到元素(无论有没有ctrl acv,最后传的才是elementxpath)
            element = findElementByXpath ((String) xpathOrElement);
        } else {
            element = (WebElement) xpathOrElement;
        }


        long start = System.currentTimeMillis ();

        while (true) {
            if (element.getAttribute (attributeKey).contains (attributeValue)) {
                return;

            }
            long end = System.currentTimeMillis ();
            if (end - start > 180000) {
                logger.warn (""+this.driver.getCurrentUrl ()+"三分钟了，" + element.getTagName () + "元素属性" + attributeKey + "的值还不等于" + attributeValue + StackTraceUnit.getStackTrace ());
                System.exit (0);
            }
        }
    }

    public void loadingUntilElAttEqual(Object xpathOrElement, String attributeKey, String attributeValue) {
        WebElement element = null;
        //如果传的是xpath,需要手动获取element
        if (xpathOrElement instanceof String) {
            //找到元素(无论有没有ctrl acv,最后传的才是elementxpath)
            element = findElementByXpath ((String) xpathOrElement);
        } else {
            element = (WebElement) xpathOrElement;
        }

        long start = System.currentTimeMillis ();

        while (true) {
            if ( element.getAttribute (attributeKey).equals (attributeValue)) {
                return;

            }
            long end = System.currentTimeMillis ();
            if (end - start > 180000) {
                logger.warn (""+this.driver.getCurrentUrl ()+"三分钟了，" + element.getTagName () + "元素属性" + attributeKey + "的值还不等于" + attributeValue + StackTraceUnit.getStackTrace ());

                System.exit (0);
            }
        }
    }

    public void loadingUntilElAttHide(Object xpathOrElement, String attributeKey) {
        WebElement element = null;
        //如果传的是xpath,需要手动获取element
        if (xpathOrElement instanceof String) {
            //找到元素(无论有没有ctrl acv,最后传的才是elementxpath)
            element = findElementByXpath ((String) xpathOrElement);
        } else {
            element = (WebElement) xpathOrElement;
        }
        long start = System.currentTimeMillis ();

        while (true) {
            try {
                String attributeValue = element.getAttribute (attributeKey);
                if (attributeValue == null) {
                    return;
                }
                long end = System.currentTimeMillis ();
                if (end - start > 180000) {
                    logger.warn (""+this.driver.getCurrentUrl ()+"三分钟了，" + element.getTagName () + "元素属性" + attributeKey + " 还没有消失"+ StackTraceUnit.getStackTrace ());
                    System.exit (0);
                }
            } catch (Exception e) {
                //找不到指定属性
                return;
            }
        }

    }

    public void loadingUntilElAttDisplay(Object xpathOrElement, String attributeKey) {

        WebElement element = null;
        //如果传的是xpath,需要手动获取element
        if (xpathOrElement instanceof String) {
            //找到元素(无论有没有ctrl acv,最后传的才是elementxpath)
            element = findElementByXpath ((String) xpathOrElement);
        } else {
            element = (WebElement) xpathOrElement;
        }
        long start = System.currentTimeMillis ();

        while (true) {
            try {
                String attributeValue = element.getAttribute (attributeKey);
                //直到找了了这个属性
                if (attributeValue != null) {
                    return;
                }
            } catch (Exception e) {
                long end = System.currentTimeMillis ();
                if (end - start > 180000) {
                    logger.warn (""+this.driver.getCurrentUrl ()+"三分钟了，" + element.getTagName () + "元素属性" + attributeKey + " 还没有出现"+ StackTraceUnit.getStackTrace ());
                    System.exit (0);
                }
                //找不到指定属性

            }
        }
    }

    public boolean loadingUntilElTextContains(Object xpathOrElement, String... textStrs) {

        WebElement element = null;
        //如果传的是xpath,需要手动获取element
        if (xpathOrElement instanceof String) {
            //找到元素(无论有没有ctrl acv,最后传的才是elementxpath)
            element = findElementByXpath ((String) xpathOrElement);
        } else {
            element = (WebElement) xpathOrElement;
        }
        long start = System.currentTimeMillis ();

        while (true) {
            try {
                String elText = element.getText ();
                //直到找了了这个属性
                if (StringUtil.containOne (elText, textStrs)) {
                    return true;
                }
                long end = System.currentTimeMillis ();
                if (end - start > 20000) {
                    logger.warn (""+this.driver.getCurrentUrl ()+"20s了，" + element.getTagName () + "元素text" + textStrs[0] + " 还没有出现" + StackTraceUnit.getStackTrace ());
                    return false;
                    //System.exit (0);
                }
            } catch (Exception e) {
                long end = System.currentTimeMillis ();
                if (end - start > 20000) {
                    logger.warn (""+this.driver.getCurrentUrl ()+"20s了，" + element.getTagName () + "元素text" + textStrs[0] + " 还没有出现" + StackTraceUnit.getStackTrace ());
                    return false;

                    // System.exit (0);
                }
                //找不到指定属性

            }
        }
    }
    public boolean loadingUntilElTextEqual(Object xpathOrElement,String elText) {
        WebElement element = null;
        //如果传的是xpath,需要手动获取element
        if (xpathOrElement instanceof String) {
            //找到元素(无论有没有ctrl acv,最后传的才是elementxpath)
            element = findElementByXpath ((String) xpathOrElement);
        } else {
            element = (WebElement) xpathOrElement;
        }

        long start = System.currentTimeMillis ();

        while (true) {
            if (element.getText ().equals (elText)) {
                return true;

            }
            long end = System.currentTimeMillis ();
            if (end - start > 20000) {
                logger.warn (""+this.driver.getCurrentUrl ()+" 20s了，" + element.getTagName () + "元素text的值还不等于" + elText + StackTraceUnit.getStackTrace ());
                return false;
            }
        }
    }

    //知道元素下没有某种tag
    public boolean loadingUntilElTagHide(Object xpathOrElement, String tagName) {

        WebElement element = null;
        //如果传的是xpath,需要手动获取element
        if (xpathOrElement instanceof String) {
            //找到元素(无论有没有ctrl acv,最后传的才是elementxpath)
            element = findElementByXpath ((String) xpathOrElement);
        } else {
            element = (WebElement) xpathOrElement;
        }
        long start = System.currentTimeMillis ();

        while (true) {

            List<WebElement> tagEls = element.findElements (By.tagName (tagName));
            //直到element下没有了tag
            if (tagEls.size () == 0) {
                return true;
            }

            long end = System.currentTimeMillis ();
            if (end - start > 20000) {
                logger.warn (""+this.driver.getCurrentUrl ()+"20s，" + element.getTagName () + "元素之下还有" + tagName + "元素" +  StackTraceUnit.getStackTrace ());
                return false;
                //System.exit (0);
                //找不到指定属性

            }
        }
    }

    public boolean loadingUntilElHideByClassName(Object xpathOrElement, String className) {

        WebElement element = null;
        //如果传的是xpath,需要手动获取element
        if (xpathOrElement instanceof String) {
            //找到元素(无论有没有ctrl acv,最后传的才是elementxpath)
            element = findElementByXpath ((String) xpathOrElement);
        } else {
            element = (WebElement) xpathOrElement;
        }
        long start = System.currentTimeMillis ();

        while (true) {

            List<WebElement> tagEls = element.findElements (By.className (className));
            //直到element下没有了tag
            if (tagEls.size () == 0) {
                return true;
            }

            long end = System.currentTimeMillis ();
            if (end - start > 20000) {
                logger.warn (""+this.driver.getCurrentUrl ()+"20s，" + element.getTagName () + "元素之下还有class名为" + className + "的元素" +  StackTraceUnit.getStackTrace ());
                return false;
                //System.exit (0);
                //找不到指定属性

            }
        }
    }

    public boolean loadingUntilElDisplayByClassName(Object xpathOrElement, String className) {

        WebElement element = null;
        //如果传的是xpath,需要手动获取element
        if (xpathOrElement instanceof String) {
            //找到元素(无论有没有ctrl acv,最后传的才是elementxpath)
            element = findElementByXpath ((String) xpathOrElement);
        } else {
            element = (WebElement) xpathOrElement;
        }
        long start = System.currentTimeMillis ();

        while (true) {

            List<WebElement> tagEls = element.findElements (By.className (className));
            //直到element下没有了tag
            if (tagEls.size () > 0) {
                return true;
            }

            long end = System.currentTimeMillis ();
            if (end - start > 20000) {
                logger.warn (""+this.driver.getCurrentUrl ()+"20s了，" + element.getTagName () + "元素之下还有class名为" + className + "的元素" + StackTraceUnit.getStackTrace ());
                return false;
                //System.exit (0);
                //找不到指定属性

            }
        }
    }

    public boolean loadingUntilElementHide(String xpath,int... waitTime) {
        boolean isHide = false;
        int wait =-1;

        if (waitTime == null || waitTime.length == 0) {
            wait = 4000;//不指定，则等4s
        } else {
            wait = waitTime[0];
        }
        long start = System.currentTimeMillis ();

        while (true) {
            try {
                driver.findElement (By.xpath (xpath));
                long end = System.currentTimeMillis ();
                if (end - start > wait) {
                    isHide = false;
                    logger.warn (this.driver.getCurrentUrl ()+wait/1000+"s了，" + xpath + " 还没有消失" +  StackTraceUnit.getStackTrace ());
                    return false;
                   // System.exit (0);
                }

            } catch (Exception e) {
                //找不到
                return true;
            }
        }
    }

    public boolean loadingUntilElementDisplay(String xpath,int... waitTime) {
        boolean display = false;
        int wait =-1;
        if (waitTime == null || waitTime.length == 0) {
            wait = 4000;//不指定，则等4s
        } else {
            wait = waitTime[0];
        }

        long start = System.currentTimeMillis ();
        while (true) {
            try {
                driver.findElement (By.xpath (xpath));
                return true;
            } catch (Exception e) {
                long end = System.currentTimeMillis ();
                if (end - start > wait) {
                    logger.warn (this.driver.getCurrentUrl ()+wait/1000+"s，" + xpath + " 还没有出现" + StackTraceUnit.getStackTrace ());
                    return false;
                    // System.exit (0);
                }
                //找不到
                //反射，拿到要反复执行的方法。比如点击某个按钮，直到找到另一个
            }
        }
    }

    //等待20秒，直到某个元素出现，如果出现返回1 ，不出现返回-1。用于等不到返回时，进行一些补偿操作
    public int loadingUntilElementDisplayReturnNum(String xpath) {
        long start = System.currentTimeMillis ();
        while (true) {
            try {
                driver.findElement (By.xpath (xpath));
                return 1;
            } catch (Exception e) {
                long end = System.currentTimeMillis ();
                if (end - start > 20000) {
                    logger.warn (this.driver.getCurrentUrl ()+"二十秒了，" + xpath + " 还没有出现" + StackTraceUnit.getStackTrace ());
                    return -1;
                }
                //找不到
                //反射，拿到要反复执行的方法。比如点击某个按钮，直到找到另一个
            }
        }
    }


    //循环直到找到元素
    public WebElement findElementByXpath(final String elementXpath, int... waitTime) {
       return findElementByXpath (elementXpath, true, waitTime);
    }
    public WebElement findElementByXpath(final String elementXpath, boolean waitToDisplayDefaultTrue, int... waitTime) {

        final WebElement[] element = new WebElement[1];

        final int[] i = {0};

        int waitSecond;
        if (waitTime != null && waitTime.length > 0) {
            waitSecond = waitTime[0];
        } else {
            waitSecond = WAITTIME;
        }
        WebDriverWait wait = new WebDriverWait (this.driver, waitSecond);

        try {
            wait.until (new ExpectedCondition<Boolean> () {

                @Override
                public Boolean apply(WebDriver webDriver) {
                    i[0]++;

                    //500毫秒执行一次
                    element[0] = webDriver.findElement (By.xpath (elementXpath));


                    return true;
                }
            });
        } catch (Exception e) {
            logger.warn (e + "\n" + "注释，"+this.driver.getCurrentUrl ()+"没有找到的元素的xpath是：\n" + elementXpath+ StackTraceUnit.getStackTrace ());
            getScreenShot ();
            return null;
            //0正常退出，非0非正常退出
           // System.exit (0);
        }

        //如果这个元素明确表明了，不用dispaly，那找到元素，直接就可以扔出去
        String s = element[0].getAttribute ("style"); //不存在，则为null.不会直接抛异常。只有s.length才抛异常
        //2019年4月28日11:38:53更新，因为发现云平台input(type=radio或checkbox)的元素，isDiable始终为false
        String tagName0 = element[0].getTagName ();
        String type0 = element[0].getAttribute ("type");
        if (!waitToDisplayDefaultTrue){
            return element[0];
        }
        else if (s != null && s.indexOf ("display") != -1 && s.indexOf ("none") != -1) {
            return element[0];
        } else if (tagName0.equals ("input") && type0 != null && (type0.equals ("radio") || type0.equals ("checkbox")
        )) {
            return element[0];
        } /*else if(StringUtil.contains (elementXpath,"th")){//2019年7月23日22:56:57 带th的始终不可见。不确定是否只是当前页又问题，待观察
            return element[0]; 2019年7月24日09:23:13 验证并非不可见。注释之

        }*/else {
            //isDiable为true时才放出去，给大家用
            int loopCount = 0;
            int loopCount2 = 0;
            while (true) {
                try {
                    while (!element[0].isDisplayed ()) {
                        if (++loopCount == 500) {
                            logger.warn ("循环了500次，"+this.driver.getCurrentUrl ()+"元素依然不可见，不可点击:" + elementXpath + StackTraceUnit.getStackTrace ());

                            System.exit (0);
                        }
                    }
                    break;
                } catch (Exception e) {
                    if (++loopCount2 == 500) {
                        logger.warn ("循环了500次，"+this.driver.getCurrentUrl ()+"元素依然不可见，不可点击:" + elementXpath + "\n" + e.getMessage ()+  StackTraceUnit.getStackTrace () );
                        System.exit (0);
                    } else {
                        continue;
                    }
                }
            }
        }


        // System.out.println("内部循序找元素用了" + i[0] + "次");


        return element[0];
    }

    //找一组元素
    public List<WebElement> findElementsByXpath(final String xpath, boolean ...isLoadingDisplayDefaultTrue) {

        WebDriverWait wait = new WebDriverWait (this.driver, WAITTIME);

        List<WebElement> listElements = null;

        for (int i = 0; i < 3; i++) {
            try {//这个wait是有效的。如果找不到就会耗费35秒，如果找到了就很快执行for循环。所以这个循环意义不大
                listElements = wait.until (new ExpectedCondition<List<WebElement>> () {

                    @Override
                    public List<WebElement> apply(WebDriver webDriver) {

                        return webDriver.findElements (By.xpath (xpath));
                    }
                });
            } catch (Exception e) {

            }
            if (listElements != null && listElements.size () > 0) {
                break;
            }
        }

        //判断一个，全部判断等太久
        //isDiable为true时才放出去，给大家用

        if (listElements == null || listElements.size () == 0) {
            logger.warn (""+this.driver.getCurrentUrl ()+"没有能根据这个xpath,找到一组元素:" + xpath+  StackTraceUnit.getStackTrace ());
            System.exit (0);
        }

        if (isLoadingDisplayDefaultTrue.length>0 && !isLoadingDisplayDefaultTrue[0]) {
            return listElements;
        }

        if (listElements.size () > 0) {
            untilIsDisPlayed (listElements.get (0));
        }
        return listElements;
    }

    //循环直到找到元素
    public WebElement findElementByTagName(final WebElement startElement, final String tagName, int... waitTime) {

        final WebElement[] element = new WebElement[1];

        final int[] i = {0};

        int waitSecond;
        if (waitTime != null && waitTime.length > 0) {
            waitSecond = waitTime[0];
        } else {
            waitSecond = WAITTIME;
        }
        WebDriverWait wait = new WebDriverWait (this.driver, waitSecond);

        try {
            wait.until (new ExpectedCondition<Boolean> () {

                @Override
                public Boolean apply(WebDriver webDriver) {
                    i[0]++;

                    //500毫秒执行一次
                    element[0] = startElement.findElement (By.tagName (tagName));


                    return true;
                }
            });
        } catch (Exception e) {
            logger.warn (e + "\n" + "注释，"+this.driver.getCurrentUrl ()+"没有找到的元素的tagName是：" + tagName+  StackTraceUnit.getStackTrace ());
            getScreenShot ();
            //0正常退出，非0非正常退出
            System.exit (0);
        }

        if (element.length > 0) {
            untilIsDisPlayed (element[0]);
        }


        // System.out.println("内部循序找元素用了" + i[0] + "次");


        return element[0];
    }

    private void untilIsDisPlayed(WebElement element) {
        //如果这个元素明确表明了，不用dispaly，那找到元素，直接就可以扔出去
        String s = element.getAttribute ("style"); //不存在，则为null.不会直接抛异常。只有s.length才抛异常
        //2019年4月28日11:38:53更新，因为发现云平台input(type=radio或checkbox)的元素，isDiable始终为false
        String tagName0 = element.getTagName ();
        String type0 = element.getAttribute ("type");
        if (s != null && s.indexOf ("display") != -1 && s.indexOf ("none") != -1) {

        } else if (tagName0.equals ("input") && type0 != null && (type0.equals ("radio") || type0.equals ("checkbox")
        )) {

        } else {
            //isDiable为true时才放出去，给大家用
            int loopCount = 0;
            while (!element.isDisplayed ()) {
                if (++loopCount == 5000) {
                    break;
                }
            }
        }
    }

    //////////////////////////////////////table--START///////////////////////////////////////////////////////

    //比较单元格全部或某一列的内容，返回匹配单元格的坐标(坐标系从0开始)
    public int[] getCellCoordinateByEqualString(String tableXpath, TagNameEnum tagNameEnum, String tdAttribute,
                                                String tdEqualSting, int... td$columnFrom0) {

        //先找一下table，进去循环这是要死啊

        WebElement tableElement = findElementByXpath (tableXpath);

        //由于页面刷新会导致之前找到的元素消息，再用元素找元素时报StaleElementReferenceException的异常，所以轮询一下
        loop:
        for (int loop = 0; loop < 200; loop++) {
            try {
                //先获取table
                tableElement = findElementByXpath (tableXpath);

                //根据table找到tr_List
                List<WebElement> trs = findElementsByTagname (tableElement, tagNameEnum.getTagName1 ());
                //轮询每一个tr,找每一行的td_list
                trLoop:
                for (int i = 0; i <= trs.size () - 1; i++) {
                    WebElement tr = trs.get (i);
                    List<WebElement> tds = findElementsByTagname (tr, tagNameEnum.getTagName2 ());

                    int tdStart = 0;
                    int tdEnd = tds.size () - 1;

                    if (td$columnFrom0 != null && td$columnFrom0.length > 0 && td$columnFrom0[0] >= 0) {
                        tdStart = tdEnd = td$columnFrom0[0];
                    }

                    //轮询td_list找到每一个td
                    tdLoop:
                    for (int j = tdStart; j <= tdEnd; j++) {
                        WebElement td = tds.get (j);
                        //如果找到了相符的活动编码
                        if (tdAttribute == null) {
                            if (td.getText ().trim ().equals (tdEqualSting.trim ())) {
                                //tr,td都是从1开始
                                return new int[]{i, j};
                            }
                        } else {
                            if (td.getAttribute (tdAttribute).trim ().equals (tdEqualSting.trim ())) {
                                return new int[]{i, j};
                            }
                        }

                    }

                }
            } catch (StaleElementReferenceException e) {

                if (e.getMessage ().indexOf ("element is not attached to the page document") > 0) {
                    //   System.out.println("元素" + tableXpath + "在页面刷新后不可用了，重新循环获取");
                    continue;
                }
            }
        }

        logger.warn ("循环了200次，"+this.driver.getCurrentUrl ()+"还是没找到刷新后的元素：tablexpath:" + tableXpath + ",要完全匹配的td是：" + tdEqualSting + "应该需要新建"+  StackTraceUnit.getStackTrace () );

        return null;

    }

    //比较单元格开始的内容，返回匹配单元格的坐标(坐标系从0开始)
    public int[] getCellCoordinateByStartString(String tableXpath, TagNameEnum tagNameEnum, String tdAttribute,
                                                String tdStartSting, int... td$columnFrom0) {


        //先找一下table，进去循环这是要死啊

        WebElement tableElement = findElementByXpath (tableXpath);

        //由于页面刷新会导致之前找到的元素消息，再用元素找元素时报StaleElementReferenceException的异常，所以轮询一下
        loop:
        for (int loop = 0; loop < 200; loop++) {
            try {
                //先获取table
                tableElement = findElementByXpath (tableXpath);

                //根据table找到tr_List
                List<WebElement> trs = findElementsByTagname (tableElement, tagNameEnum.getTagName1 ());
                //轮询每一个tr,找每一行的td_list
                trLoop:
                for (int i = 0; i <= trs.size () - 1; i++) {
                    WebElement tr = trs.get (i);
                    List<WebElement> tds = findElementsByTagname (tr, tagNameEnum.getTagName2 ());


                    int tdStart = 0;
                    int tdEnd = tds.size () - 1;

                    if (td$columnFrom0 != null && td$columnFrom0.length > 0 && td$columnFrom0[0] >= 0) {
                        tdStart = tdEnd = td$columnFrom0[0];
                    }

                    //轮询td_list找到每一个td
                    tdLoop:
                    for (int j = tdStart; j <= tdEnd; j++) {

                        WebElement td = tds.get (j);
                        //如果找到了相符的活动编码
                        if (tdAttribute == null) {
                            if (td.getText ().trim ().startsWith (tdStartSting.trim ())) {
                                //tr,td都是从1开始
                                return new int[]{i, j};
                            }
                        } else {
                            if (td.getAttribute (tdAttribute).trim ().startsWith (tdStartSting.trim ())) {
                                return new int[]{i, j};
                            }
                        }
                    }

                }
            } catch (StaleElementReferenceException e) {

                if (e.getMessage ().indexOf ("element is not attached to the page document") > 0) {
                    //System.out.println("元素" + tableXpath + "在页面刷新后不可用了，失效了");
                    continue;
                }

            }
        }

        logger.warn ("循环了200次，"+this.driver.getCurrentUrl ()+"还是没找到刷新后的元素:" + tableXpath + ",要匹配的td开始字符串是" + tdStartSting+  StackTraceUnit.getStackTrace () );
        return null;

    }


    //比较单元格包含的内容，返回所有的匹配单元格的坐标(坐标系从0开始)
    public Map<Integer, int[]> getCellCoordinatesByIndexOfString(String tableXpath, TagNameEnum tagNameEnum, String
            tdAttribute, String tdIndexOfSting, int... td$columnFrom0) {

        Map<Integer, int[]> CoordinateMap = null;
        //先找一下table，进去循环这是要死啊
        WebElement tableElement = findElementByXpath (tableXpath);

        //由于页面刷新会导致之前找到的元素消息，再用元素找元素时报StaleElementReferenceException的异常，所以轮询一下
        loop:
        for (int loop = 0; loop < 200; loop++) {

            CoordinateMap = new HashMap<Integer, int[]> ();

            try {
                //先获取table
                tableElement = findElementByXpath (tableXpath);

                //根据table找到tr_List
                List<WebElement> trs = findElementsByTagname (tableElement, tagNameEnum.getTagName1 ());
                //轮询每一个tr,找每一行的td_list
                trLoop:
                for (int i = 0; i <= trs.size () - 1; i++) {
                    WebElement tr = trs.get (i);
                    List<WebElement> tds = findElementsByTagname (tr, tagNameEnum.getTagName2 ());


                    int tdStart = 0;
                    int tdEnd = tds.size () - 1;

                    if (td$columnFrom0 != null && td$columnFrom0.length > 0 && td$columnFrom0[0] >= 0) {
                        tdStart = tdEnd = td$columnFrom0[0];
                    }

                    //轮询td_list找到每一个td
                    tdLoop:
                    for (int j = tdStart; j <= tdEnd; j++) {

                        WebElement td = tds.get (j);
                        //如果找到了相符的活动编码
                        if (tdAttribute == null) {
                            if (td.getText ().trim ().indexOf (tdIndexOfSting.trim ()) != -1) {
                                //tr,td都是从1开始
                                CoordinateMap.put (j, new int[]{i, j});


                            }
                        } else {
                            if (td.getAttribute (tdAttribute).trim ().indexOf (tdIndexOfSting.trim ()) != -1) {
                                CoordinateMap.put (j, new int[]{i, j});
                            }
                        }
                    }

                }

                if (CoordinateMap == null || CoordinateMap.size () == 0) {
                    logger.warn (""+this.driver.getCurrentUrl ()+"没有找到包含:" + tdIndexOfSting + "字符串的一组元素"+  StackTraceUnit.getStackTrace ());
                    System.exit (0);
                }

                //没有异常则跳出循环
                return CoordinateMap;

            } catch (StaleElementReferenceException e) {

                if (e.getMessage ().indexOf ("element is not attached to the page document") > 0) {
                    // System.out.println("元素" + tableXpath + "被刷没了");
                    continue;
                }
            }
        }
        logger.warn ("循环200次，"+this.driver.getCurrentUrl ()+"没有找到包含:" + tdIndexOfSting + "字符串的一组元素"+ StackTraceUnit.getStackTrace () );
        //System.exit(0);
        return null;
    }


    //比较单元格开始的内容，返回所有的匹配单元格的坐标(坐标系从0开始)
    public Map<Integer, int[]> getCellCoordinatesByStartString(String tableXpath, TagNameEnum tagNameEnum, String
            tdAttribute, String tdStartSting, int... td$columnFrom0) {

        Map<Integer, int[]> CoordinateMap = null;
        //先找一下table，进去循环这是要死啊
        WebElement tableElement = findElementByXpath (tableXpath);

        //由于页面刷新会导致之前找到的元素消息，再用元素找元素时报StaleElementReferenceException的异常，所以轮询一下
        loop:
        for (int loop = 0; loop < 200; loop++) {

            CoordinateMap = new HashMap<Integer, int[]> ();

            try {
                //先获取table
                tableElement = findElementByXpath (tableXpath);

                //根据table找到tr_List
                List<WebElement> trs = findElementsByTagname (tableElement, tagNameEnum.getTagName1 ());
                //轮询每一个tr,找每一行的td_list
                trLoop:
                for (int i = 0; i <= trs.size () - 1; i++) {
                    WebElement tr = trs.get (i);
                    List<WebElement> tds = findElementsByTagname (tr, tagNameEnum.getTagName2 ());


                    int tdStart = 0;
                    int tdEnd = tds.size () - 1;

                    if (td$columnFrom0 != null && td$columnFrom0.length > 0 && td$columnFrom0[0] >= 0) {
                        tdStart = tdEnd = td$columnFrom0[0];
                    }

                    //轮询td_list找到每一个td
                    tdLoop:
                    for (int j = tdStart; j <= tdEnd; j++) {

                        WebElement td = tds.get (j);
                        //如果找到了相符的活动编码
                        if (tdAttribute == null) {
                            if (td.getText ().trim ().startsWith (tdStartSting.trim ())) {
                                //tr,td都是从1开始
                                CoordinateMap.put (j, new int[]{i, j});


                            }
                        } else {
                            if (td.getAttribute (tdAttribute).trim ().startsWith (tdStartSting.trim ())) {
                                CoordinateMap.put (j, new int[]{i, j});
                            }
                        }
                    }

                }

                if (CoordinateMap == null || CoordinateMap.size () == 0) {
                    logger.warn (""+this.driver.getCurrentUrl ()+"没有找到以:" + tdStartSting + "开头的一组元素"+  StackTraceUnit.getStackTrace ());
                    System.exit (0);
                }

                //没有异常则跳出循环
                return CoordinateMap;

            } catch (StaleElementReferenceException e) {

                if (e.getMessage ().indexOf ("element is not attached to the page document") > 0) {
                    // System.out.println("元素" + tableXpath + "被刷没了");
                    continue;
                }
            }
        }
        logger.warn ("循环200次，"+this.driver.getCurrentUrl ()+"没有找到以:" + tdStartSting + "开头的一组元素"+  StackTraceUnit.getStackTrace ());
        //  System.exit(0);
        return null;
    }

    //通过坐标找到元素 (table/tr[1]/td[2]//input)
    public WebElement findElementByCoordinate(String tableXpath, TagNameEnum tagNameEnum, int[] coordinate, int
            tdOffset) {

        int trCount = coordinate[0] + 1;
        int tdCount = coordinate[1] + 1 + tdOffset;


        //一般要的都是input
        String inputXpath = tableXpath + "//" + tagNameEnum.getTagName1 () + "[" + trCount + "]/" + tagNameEnum
                .getTagName2 () + "[" + tdCount + "]//input";
        String xpath = tableXpath + "//" + tagNameEnum.getTagName1 () + "[" + trCount + "]/" + tagNameEnum
                .getTagName2 () + "[" + tdCount + "]";

        WebElement element = null;

        //既然已经得到了坐标，那起码 tr/td一定可用
        try {
            //如果找不到input,就只返回td元素
            element = this.driver.findElement (By.xpath (inputXpath));
        } catch (Exception e) {
            element = findElementByXpath (xpath);

        }
        //优先返回input下的td
        return element;

    }

    //返回table的行数
    public int getNumberOfRows(String tableXpath, TagNameEnum tagNameEnum) {

        //由于页面刷新会导致之前找到的元素消息，再用元素找元素时报StaleElementReferenceException的异常，所以轮询一下
        loop:
        for (int loop = 0; loop < 5000; loop++) {
            try {
                //先获取table
                WebElement tableElement = findElementByXpath (tableXpath);

                //根据table找到tr_List
                List<WebElement> trs = findElementsByTagname (tableElement, tagNameEnum.getTagName1 ());
                //轮询每一个tr,找每一行的td_list

                if (trs == null) {
                    return 0;
                } else {

                    return trs.size ();
                }


            } catch (WebDriverException e) {

                continue;
            }
        }

        logger.warn ("循环了5000次，"+this.driver.getCurrentUrl ()+"还是没找到刷新后的元素"+  StackTraceUnit.getStackTrace ());

        return 0;
    }

    //返回table的行数.可以传递指定行数，查询这一行的单元格数量
    public int getNumberOfCells(String tableXpath, TagNameEnum tagNameEnum, int... trNum) {

        //由于页面刷新会导致之前找到的元素消息，再用元素找元素时报StaleElementReferenceException的异常，所以轮询一下
        loop:
        for (int loop = 0; loop < 5000; loop++) {
            try {
                //先获取table
                WebElement tableElement = findElementByXpath (tableXpath);

                //接收所有行，所有单元格
                List<WebElement> trs = null;
                List<WebElement> tds = null;


                //根据table找到tr_List
                trs = findElementsByTagname (tableElement, tagNameEnum.getTagName1 ());
                //轮询每一个tr,找每一行的td_list

                if (trs == null) {
                    return 0;
                } else {

                    if (trNum != null && trNum.length > 0) {
                        WebElement tr = trs.get (trNum[0]);

                        tds = findElementsByTagname (tr, tagNameEnum.getTagName2 ());

                        return tds.size ();
                    } else {
                        int maxSize = -1;
                        for (WebElement tr : trs) {

                            tds = findElementsByTagname (tr, tagNameEnum.getTagName2 ());

                            if (tds.size () > maxSize) {
                                maxSize = tds.size ();
                            }
                        }

                        return maxSize;
                    }

                }


            } catch (WebDriverException e) {

                continue;
            }
        }

        logger.warn ("循环了5000次，"+this.driver.getCurrentUrl ()+"还是没找到刷新后的元素"+  StackTraceUnit.getStackTrace ());

        return 0;
    }

    //通过元素找其他的一组元素
    public List<WebElement> findElementsByTagname(final WebElement webElementOrNull, final String tagname) {

        WebDriverWait wait = new WebDriverWait (this.driver, WAITTIME);

        List<WebElement> listElements = null;
        try {
            listElements = wait.until (new ExpectedCondition<List<WebElement>> () {

                @Override
                public List<WebElement> apply(WebDriver webDriver) {

                    if (webElementOrNull != null) {
                        return webElementOrNull.findElements (By.tagName (tagname));
                    } else {
                        return webDriver.findElements (By.tagName (tagname));
                    }
                }
            });
        } catch (Exception e) {
            logger.warn (e + "\n" + "注释，"+this.driver.getCurrentUrl ()+"没有找到的一组元素的tagname是：" + tagname+  StackTraceUnit.getStackTrace ());
            getScreenShot ();
            //0正常退出，非0非正常退出
            System.exit (0);
        }
        if (listElements.size () > 0) {
            untilIsDisPlayed (listElements.get (0));
        }
    /*    //判断一个，全部判断等太久
        //isDiable为true时才放出去，给大家用
        int loopCount = 0;
        while (listElements!=null &&listElements.size()>=1&&!listElements.get(0).isDisplayed()) {
            if (++loopCount == 500) {
                break;
            }
        }*/
        return listElements;
    }

    //通过元素找其他的一组元素
    public List<WebElement> findElementsByClassname(final WebElement webElementOrNull, final String className) {

        WebDriverWait wait = new WebDriverWait (this.driver, WAITTIME);

        List<WebElement> listElements = null;
        try {
            listElements = wait.until (new ExpectedCondition<List<WebElement>> () {

                @Override
                public List<WebElement> apply(WebDriver webDriver) {

                    if (webElementOrNull != null) {
                        return webElementOrNull.findElements (By.className (className));
                    } else {
                        return webDriver.findElements (By.className (className));
                    }
                }
            });
        } catch (Exception e) {
            logger.warn (e + "\n" + "注释，"+this.driver.getCurrentUrl ()+"没有找到的一组元素的className是：" + className+  StackTraceUnit.getStackTrace ());
            getScreenShot ();
            //0正常退出，非0非正常退出
            System.exit (0);
        }

        if (listElements.size () > 0) {
            untilIsDisPlayed (listElements.get (0));
        }  /*      //判断一个，全部判断等太久
        //isDiable为true时才放出去，给大家用
        int loopCount = 0;
        while (listElements!=null &&listElements.size()>=1&&!listElements.get(0).isDisplayed()) {
            if (++loopCount == 500) {
                break;
            }
        }*/
        return listElements;
    }
    //////////////////////////////////////table--END///////////////////////////////////////////////////////


    //通过URL，（具体的定位路径）来请求页面
    public void requestAndDisplayPage(final String url) {

        WebDriverWait wait = new WebDriverWait (this.driver, WAITTIME);

        //循环600次，用时15秒，一秒40次
        first:
        for (int i = 0; i < 600; i++) {
            try {
                boolean r = wait.until (new ExpectedCondition<Boolean> () {

                    @Override
                    public Boolean apply(WebDriver webDriver) {

                        webDriver.get (url);

                        return true;
                    }
                });
                if (r) {
                    return;
                }

            } catch (Exception e) {
                // logger.warn (e + "\n" + "注释,没有从  " + url + "  请求到网页：");
            }

        }
        logger.warn ("没有从  " + url + "  请求到网页："+ StackTraceUnit.getStackTrace ());
        getScreenShot ();
        System.exit (0);

    }

    //鼠标操作方法  (click/contextClick/clickAndHold/doubleClick/moveToElement/dragAndDrop)
    public Actions mouseOperation(MouseOperationEnum mouseOperationEnum, final Object... xpathOrElement) {

        if (xpathOrElement == null) {
            logger.debug ("鼠标操作对象不能为空");
            return null;
        }
        //方法名
        String methodName = mouseOperationEnum.getMethodName ();

        WebElement sourceElement = null;
        WebElement targetElement = null;

        //如果传过来的是元素的xpath
        if (xpathOrElement[0] instanceof String) {
            String sourceXpath = (String) xpathOrElement[0];
            //定位第一个元素
            sourceElement = findElementByXpath (sourceXpath);
            //定位第二个元素（如果有的话）
            if (xpathOrElement.length == 2) {
                String targetXpath = (String) xpathOrElement[1];
                targetElement = findElementByXpath (targetXpath);
            }
        } else {

            sourceElement = (WebElement) xpathOrElement[0];

            //定位第二个元素（如果有的话）
            if (xpathOrElement.length == 2) {
                targetElement = (WebElement) xpathOrElement[1];
            }
        }


        //匿名内部类只能访问final局部变量，所以用final接一下可能有的第二个元素
        final WebElement sourceElement_final = sourceElement;
        final WebElement targetElement_final = targetElement;


        final Object[] object = {null};
        try {
            //反射执行方法：一切基于class
            Class actionsClass = Class.forName ("org.openqa.selenium.interactions.Actions");

            //通过class获取有参数的构造方法（必须是方法的本身参数类，不能是其子类）
            Constructor actionConstructor = actionsClass.getDeclaredConstructor (WebDriver.class);

            //调用构造函数并传参生成action对象. Actions actions = new Actions(driver);
            object[0] = actionConstructor.newInstance (this.driver);

            //通过class和方法名获取可执行的方法

            //第一个方法是click或者draganddrop
            final Method method;
            if (xpathOrElement.length == 1) {
                method = actionsClass.getMethod (methodName, WebElement.class);
            } else {
                method = actionsClass.getMethod (methodName, WebElement.class, WebElement.class);
            }
            //第二个方法是提交
            final Method performMethod = actionsClass.getMethod ("perform");
            final int[] i = {0};

            //执行方法
            {
                WebDriverWait wait = new WebDriverWait (this.driver, WAITTIME);
                try {
                    wait.until (new ExpectedCondition<Boolean> () {
                        @Override
                        public Boolean apply(WebDriver webDriver) {

                            i[0]++;

                            try {
                                //执行方法一，click或draganddrop方法
                                if (xpathOrElement.length == 1) {
                                    object[0] = method.invoke (object[0], sourceElement_final);
                                } else {
                                    object[0] = method.invoke (object[0], sourceElement_final, targetElement_final);
                                }
                                //执行方法二，提交方法
                                performMethod.invoke (object[0]);
                            } catch (IllegalAccessException e) {
                                logger.warn (e);
                                e.printStackTrace ();
                            } catch (InvocationTargetException e) {
                                logger.warn (e);
                                e.printStackTrace ();
                            }

                            return true;
                        }
                    });
                } catch (IllegalArgumentException e) {
                    logger.warn (e + "\n" + "注释，"+this.driver.getCurrentUrl ()+"鼠标操作失败，起始元素是：" + sourceElement+  StackTraceUnit.getStackTrace ());
                    getScreenShot ();
                }

                //System.out.println("内部循序===鼠标操作-用了" + i[0] + "次");

            }

        } catch (Exception e) {
            logger.warn (e);
            e.printStackTrace ();
        }

        return ((Actions) object[0]);
    }

    //键盘操作 -- 输入空格/enter或者字符串
    public void keyboardOperation(final Object wordsOrSpecialkey, Object xpathOrElement,boolean...defaultNoclean) {
        keyboardOperation (wordsOrSpecialkey, null, xpathOrElement,defaultNoclean);
    }

    //键盘操作 （输入的内容越多，耗费的时间越长。只输入1还要200毫秒，十来个字一秒钟）
    public void keyboardOperation( Object wordsOrSpecialkey0, final GeneralKeyEnum gerralKey, Object
            xpathOrElement,boolean... defaultNoclean) {
        //普通的字母、特殊按键、组合按键 （通过xpath或element）

        //获取element
        WebElement element = null;
        //如果传的是xpath,需要手动获取element
        if (xpathOrElement instanceof String) {
            //找到元素(无论有没有ctrl acv,最后传的才是elementxpath)
            element = findElementByXpath ((String) xpathOrElement);
            //2019年6月4日14:43:53 元素为空时，不点击即可，不用kill系统
            if (element == null) {
                logger.warn ("由于没有找到元素，不再尝试鼠标操作"+ StackTraceUnit.getStackTrace ());
                return;
            }
        } else {
            element = (WebElement) xpathOrElement;
        }



        //如果输入为空
        if (wordsOrSpecialkey0 == null) {
            wordsOrSpecialkey0 = "";
        }

        //先清空？
        if (defaultNoclean !=null && defaultNoclean.length>0 && defaultNoclean[0]==true) {
            clearInput (wordsOrSpecialkey0, element);
        }

        final Object wordsOrSpecialkey = wordsOrSpecialkey0;

        //匿名内部类，需要final局部变量
        final WebElement element_final = element;

        final int[] i = {0};
        //按键
        WebDriverWait wait = new WebDriverWait (this.driver, WAITTIME);


        try {
            wait.until (new ExpectedCondition<Boolean> () {
                @Override
                public Boolean apply(WebDriver webDriver) {

                    i[0]++;


                    if (wordsOrSpecialkey instanceof String) {
                        element_final.sendKeys ((String) wordsOrSpecialkey);
                    }
                    //如果传的是特殊按键
                    else {
                        //如果是enter,space,backspace等单一的按键
                        if (gerralKey == null) {
                            element_final.sendKeys ((Keys) wordsOrSpecialkey);
                        } else {
                            element_final.sendKeys ((Keys) wordsOrSpecialkey, gerralKey.getGeneralKey ());
                        }
                    }

                    return true;
                }
            });
        } catch (Exception e) {
            logger.warn (e + ""+this.driver.getCurrentUrl ()+"异常输入" + wordsOrSpecialkey + "。异常元素" + xpathOrElement+  StackTraceUnit.getStackTrace ());
            getScreenShot ();
        }

        // System.out.println("内部循序===键盘输入--用了" + i[0] + "次");

    }

    //跳到新打开的窗口中
    public void switchToNewWindow() {
        this.driver.close ();
        //获取所有窗口的handles
        Set<String> handles = this.driver.getWindowHandles ();

        //获取新窗口的handle
        for (String handle : handles) {
            this.driver.switchTo ().window (handle);

        }
    }

    //比较title以跳转到某一个窗口中
    public void switchToWindow(String windowTitle, Boolean closeOtherWindows) {

        //获取所有窗口的handles
        Set<String> handles = this.driver.getWindowHandles ();

        //如果不需要关闭其他窗口，就没必要在找到了符合条件的窗体后，再轮训一遍
        if (!closeOtherWindows) {

            //当前窗体如果是想要的，就省事了
            if (this.driver.getTitle ().trim ().equals (windowTitle.trim ())) {
                return;
            }
            //当前窗口不是的话，就逐个跳入，对比title
            else {
                for (String handle : handles) {

                    //循环以跳入窗口
                    switchToWindow_plungeIntoCommon (handle);

                    //如果title相符，return即可
                    if (this.driver.getTitle ().trim ().equals (windowTitle.trim ())) {
                        return;
                    }
                }
            }
        }

        //需要关闭其他所有的窗口
        else {

            //接收与title匹配的handle
            String windowHandle = null;

            //循环关闭所有无关窗口
            for (String handle : handles) {

                System.out.println ("欲进入的handle====" + handle);

                switchToWindow_plungeIntoCommon (handle);

                   /* System.out.println("执行完跳转后，handle是" + this.driver.getWindowHandle());
                   System.out.println("执行完跳转后，title是" + this.driver.getTitle());
                  System.out.println("执行完跳转后，url是" + this.driver.getCurrentUrl());*/
                //记下相同title的handle，关掉无关的窗口
                if (this.driver.getTitle () != null && this.driver.getTitle ().trim ().equals (windowTitle.trim ())) {

                    windowHandle = this.driver.getWindowHandle ();
                    //     System.out.println("要跳转到的handle是" + windowHandle);
                } else {
                    this.driver.close ();
                }

            }

            // System.out.println("执行完循环后，最终将要跳入到的handle是：" + windowHandle);
            //最终跳入title相符的window中
            switchToWindow_plungeIntoCommon (windowHandle);
        }


    }

    //跳入窗口的一部分，循环跳，直到跳进去
    private void switchToWindow_plungeIntoCommon(final String windowHandle) {

        long start = System.currentTimeMillis ();

        int result = -1;

        final int[] j = {0};
        //循环直到展示了url 循环600次，用时15秒，一秒40次
        for (int i = 0; i < 600; i++) {
            WebDriverWait wait = new WebDriverWait (this.driver, WAITTIME);

            try {
                wait.until (new ExpectedCondition<Boolean> () {

                    @Override
                    public Boolean apply(WebDriver webDriver) {

                        webDriver = webDriver.switchTo ().window (windowHandle);


                        //  webDriver.findElementByXpath(By.pageLocation(ActivitySettingConstant.BOHXPATH));

                        return true;
                    }
                });
            } catch (Exception e) {

            }
            //如果通过driver获取到的handle与相同，说明跳进去了
            if (this.driver.getWindowHandle ().trim ().equals (windowHandle.trim ())) {
                result = 1;
                //直到能获取到http开头的url才能进行比较，才允许继续往下执行
                if (this.driver.getCurrentUrl ().startsWith ("http:") || this.driver.getCurrentUrl ().startsWith
                        ("https:")) {

                    return;
                }
            }

        }
        long end = System.currentTimeMillis ();

        if (result == -1) {
            logger.warn ("用时" + (end - start) + "秒，还跳不进去的窗口的handle是：" + windowHandle + ";当前driver的title是：" + this
                    .driver.getTitle ()
                    + "url是：" + this.driver.getCurrentUrl ()+  StackTraceUnit.getStackTrace () );
        } else {
            logger.warn ("虽然能跳进窗口。但是用时" + (end - start) + "秒，此页面还是没有url。handle是：" + windowHandle + ";" +
                    "当前driver的title是：" + this.driver.getTitle ()
                    + "url是：" + this.driver.getCurrentUrl ()+  StackTraceUnit.getStackTrace ());
        }
        getScreenShot ();
        return;

    }

    //跳入表单中
    public void switchToFrame(String frameXpath, String... checkXpath) {

        //找到表单元素
        WebElement frameElment = findElementByXpath (frameXpath);

        Boolean switchToFrame = true;
        //轮询0 ~ 120s
        int ii = 1200;
        for (int i = 0; i < ii; i++) {

            //为避免抛出元素超时的异常，不要每次都跳入
            if (switchToFrame) {
                this.driver.switchTo ().frame (frameElment);
                switchToFrame = false;
            }

            //通过寻找表单frame元素来判断是否可能已跳入表单
            try {
                this.driver.findElement (By.xpath (frameXpath));
                //能找到，说明当前在表单之上一层，直接可跳进表单中
                switchToFrame = true;
                continue;
            } catch (Exception e) {
                //找不到要跳入的iframe，则说明可能已经进入了表单，也可能在表单之外的两层，也可能再表单之内的两层
                if (checkXpath != null && checkXpath.length > 0) {
                    try {
                        //根据检察元素，判断是否已经进入表单中
                        this.driver.findElement (By.xpath (checkXpath[0]));
                        //  System.out.println("for循环了" + i + "次，进入了表单:" + frameXpath);
                        return;
                    } catch (Exception e1) {
                        if (i == (ii - 1)) {
                            //如果找不到，继续往下通过焦点判断是否在二层表单
                            logger.warn (""+this.driver.getCurrentUrl ()+"没有进入表单，上下的最少还要再跳入一层iframe"+  StackTraceUnit.getStackTrace ());
                            //      System.exit (0);
                        }
                    }
                } else {//走到这里，就只剩了两种可能：①进入了表单 ②上下的最少还要再跳入一层iframe
                    logger.debug (""+this.driver.getCurrentUrl ()+"没有检查点元素,也没法判断是进入了表单，还是上下的最少还要再跳入一层iframe"+ StackTraceUnit.getStackTrace () );

                    return;
                }
            }
        }

        //循环结束了，还没跳入表单
        logger.warn (""+this.driver.getCurrentUrl ()+"循环结束了，还没有跳入表单中，pageLocation:" + frameXpath+  StackTraceUnit.getStackTrace () );
        System.exit (0);
    }


    //获取select所有options
    public List<WebElement> getAllOptions(Object xpathOrElement) {

        WebElement element = null;
        //如果传的是xpath,需要手动获取element
        if (xpathOrElement instanceof String) {
            //找到元素(无论有没有ctrl acv,最后传的才是elementxpath)
            element = findElementByXpath ((String) xpathOrElement);
        } else {
            element = (WebElement) xpathOrElement;
        }


        Select select = new Select (element);

        List<WebElement> options = new ArrayList<> ();

        options = select.getOptions ();

        return options;
    }

    //选择某个值。是text值。如getText()
    public void selectByVisibleText(Object xpathOrElement, String visibleText) {

        WebElement element = null;
        //如果传的是xpath,需要手动获取element
        if (xpathOrElement instanceof String) {
            //找到元素(无论有没有ctrl acv,最后传的才是elementxpath)
            element = findElementByXpath ((String) xpathOrElement);
        } else {
            element = (WebElement) xpathOrElement;
        }


        Select select = new Select (element);

        select.selectByVisibleText (visibleText);


    }

    //选择属性value = 某个值的元素
    public void selectByValue(Object selectXpathOrElement, String value) {

        WebElement element = null;
        //如果传的是xpath,需要手动获取element
        if (selectXpathOrElement instanceof String) {
            //找到元素(无论有没有ctrl acv,最后传的才是elementxpath)
            element = findElementByXpath ((String) selectXpathOrElement);
        } else {
            element = (WebElement) selectXpathOrElement;
        }


        Select select = new Select (element);

        select.selectByValue (value);


    }

    //高亮显示元素
    public void highLightElement(ColorEnum colorEnum, Object xpathOrElement) {

        String backgroundColor = "yellow";
        String elmentColor = colorEnum.getColor ();

        JavascriptExecutor js = (JavascriptExecutor) this.driver;
        String jsString = "element = arguments[0];" +
                "original_style = element.getAttribute('style');" +
                "element.setAttribute('style', original_style + \";" +
                "background: " + backgroundColor + "; border: 2px solid " + elmentColor + ";\");" +
                "setTimeout(function(){element.setAttribute('style', original_style);}, 2000);";

        if (xpathOrElement instanceof String) {
            js.executeScript (jsString, findElementByXpath ((String) xpathOrElement));
        } else {
            js.executeScript (jsString, (WebElement) xpathOrElement);
        }


    }

    //获取屏幕截图
    public void getScreenShot() {

        //得到系统时间
        Date date = new Date ();

        //日期格式化
        SimpleDateFormat format_year_month_day = new SimpleDateFormat ("MMdd_yyyy");
        SimpleDateFormat format_hour_m_s = new SimpleDateFormat ("HH：mm：ss：SSS");

        String year_month_day = format_year_month_day.format (date);
        String hour_m_s = format_hour_m_s.format (date);

        //将截图放到E盘,以当天日期命名的文件夹下，以当前时间命名的png
        File srcFile = ((TakesScreenshot) this.driver).getScreenshotAs (OutputType.FILE);

        try {
            FileUtils.copyFile (srcFile, new File (errorLogFile + year_month_day + "\\" + hour_m_s + ".png"));
        } catch (IOException e) {
            e.printStackTrace ();
        }

    }

    public void sleep(long millis) {
        try {
            Thread.sleep (millis);
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }
    }

    public void clearInput(Object xpathOrElement) {
      clearInput (null,xpathOrElement);
    }

    /**
     * Clear input.
     * 应该是在使用el.clear()时遇到了问题：当计划clear后输入空格时，实际未被清除
     *
     * @param inputWordsOrSpecialkey the input words or specialkey
     * @param xpathOrElement         the xpath or element
     */
    public void clearInput(Object inputWordsOrSpecialkey,Object xpathOrElement) {

        WebElement element = null;
        //如果传的是xpath,需要手动获取element
        if (xpathOrElement instanceof String) {
            //找到元素(无论有没有ctrl acv,最后传的才是elementxpath)
            element = findElementByXpath ((String) xpathOrElement);
        } else {
            element = (WebElement) xpathOrElement;
        }

        //如果没有传过来后续要输入的内容
        if (inputWordsOrSpecialkey == null) {
            element.clear ();
            return;
        }

        for (int i =0 ;i<20;i++) {
            //传过来的如果是空字符串，就需要循环退格来达到清空目的
            if (inputWordsOrSpecialkey instanceof String) {
                String input = (String)inputWordsOrSpecialkey;
                String value = element.getAttribute ("value");
                String text = element.getAttribute ("text");
                String text2 = element.getText ();
                if ((value == null || value.length () == 0) && (text == null || text.length () == 0) && (text2 ==null || text2.length () == 0)) {
                    return;//搞空了
                }
                int loop = -1;
                if (input.length () == 0) {//如果要输入的是空格，就只能一个个退格。clear（）并不能达到目的
                    //根据当前输入框中的内容，确定退格次数
                    if (value != null) {
                        loop = value.length ();

                    } else if (text != null) {
                        loop = text.length ();
                    } else if (text2 != null) {
                        loop = text2.length ();
                    }else {//不能得到页面输入内容，不能确认退格次数，退100次吧
                        loop = 100;
                    }

                    for (int j = 0; j < loop; j++) {
                        element.sendKeys (Keys.BACK_SPACE);//TODO 这样退格，要求页面鼠标不能失去焦点。一直在退格，如果突然脱离编辑状态。就找不到要退格的元素。抛异常
                    }
                } else {
                    element.clear ();

                }
                //最后没办法了，轮询退格试试
                if (i==20) {
                    for (int j = 0; j < loop; j++) {
                        element.sendKeys (Keys.BACK_SPACE);
                    }

                    if (!((value == null || value.length () == 0) && (text == null || text.length () == 0) && (text2 ==
                            null || text2.length () == 0))) {
                        logger.warn (""+this.driver.getCurrentUrl ()+"无法清空input内容");
                    }
                }
            }

        }

    }

    //关闭driver和service
    public void quertDriver(int waitTime) {

        try {
            Thread.sleep (waitTime * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace ();
        }

        this.driver.quit ();
        service.stop ();

    }
}
