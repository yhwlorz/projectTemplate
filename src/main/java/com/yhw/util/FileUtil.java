package com.yhw.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;


/**
 * The type File util.
 *
 * https://www.cnblogs.com/chen-lhx/p/5610678.html
 *
 * @author 杨怀伟
 * @date 2019 -07-03 10:55:34
 */
public class FileUtil {

    /**
     * 任一对象，转为String后，写出到文件中
     *
     * 同名覆盖，而非追加
     *
     * @param <T>      the type parameter
     * @param t        the t
     * @param filePath the file path F:\工作\新美大下单记录\2019-08-14\96475393193451872.txt
     */
    public static <T> void writeOutFile(T t, String filePath) {
        try {
            //将字符串写入文件中。不再需要输入流，byte[]直接用string转就行
            String writeString = JsonUtil.object2String (t);

            List<String> onePathList = new ArrayList<> ();
            String onePath = filePath;
            for (int i = 0; i < filePath.split ("\\\\").length-1-1; i++) {//由于包含文件，故length是5，只需循环4次
                onePath = StringUtil.substringBeforeLast (onePath, "\\");//F:\工作\新美大下单记录\2019-08-14
                onePathList.add (onePath);//"F:\工作\新美大下单记录\2019-08-14" "F:\工作\新美大下单记录" "F:\工作" "F:" -->F盘无法创建，故循环次数再减一次
            }
            for (int i=onePathList.size ()-1;i>=0;i--) {
                File fileDir = new File (onePathList.get (i));
                if (!fileDir.exists ()) {
                    fileDir.mkdir ();
                }
            }
            File file = new File (filePath);
            if (!file.exists ()) {
                file.createNewFile ();
            }
            //实例化文件输出流
            FileOutputStream fileOutputStream = new FileOutputStream (file);
            //实例化缓存字节输出流
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream (fileOutputStream);
            //指定要装载数据的byte数组和每次装载的字节长度
            byte[] inArray = writeString.getBytes ();
            //已经将数据读到byte数组中了，直接把数组中的byte写到文件里就行了
            //将字节数组中的字节写入缓存中。数据会不断的追加，不会替换和消失，指定写入的长度和读取到的长度一致，避免重复写入
            bufferedOutputStream.write (inArray, 0, inArray.length);
            //必须要有的，将缓存中数据写入到文件中，并清掉缓存。
            bufferedOutputStream.flush ();
            //关闭输出缓存流
            //只需将缓冲流关闭，无需将节点流关闭，因为缓冲流内部会自动调用节点流关闭
            bufferedOutputStream.close ();
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }

    /**
     * 读取文件内容，写入对象
     *
     * @return the string
     */
    public static String readFileInString(String filePath) {
        try {
            //文件
            File file = new File (filePath);
            //读入字节流中
            InputStream inputStream = new FileInputStream (file);
            //读入到字符流reader中
            InputStreamReader inputStreamReader = new InputStreamReader (inputStream);
            //读入到字符流bufferread，提高效率
            BufferedReader bufferedReader = new BufferedReader (inputStreamReader);
            //循环写入stringbuffer
            StringBuffer stringBuffer = new StringBuffer ();
            while (bufferedReader.ready ()) {
                stringBuffer.append (bufferedReader.readLine ());
            }
            return stringBuffer.toString ();

        } catch (Exception e) {
            e.printStackTrace ();
            return null;
        }

    }

    /**
     * 将sourceFilePath中的内容复制到targetFilePath
     *
     * @param sourceFilePath the source file path
     * @param targetFilePath the target file path
     */
    public static void copyFileContent(String sourceFilePath,String targetFilePath) {
        try {
            //读取sourceFile，将数据写入targetFile

            //如果不存在，则新建
            File file2 = new File(targetFilePath);
            if (!file2.exists()){
                file2.createNewFile();
            }
            //实例化文件输入流
            FileInputStream fileInputStream = new FileInputStream(sourceFilePath);
            //实例化缓存字节输入流
            BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream);
            //实例化文件输出流
            FileOutputStream fileOutputStream = new FileOutputStream(targetFilePath);
            //实例化缓存字节输出流
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fileOutputStream);
            //指定要装载数据的byte数组和每次装载的字节长度
            byte[] inArray = new byte[1024];//bufferedInputStream.available()返回剩余的字节数，可以代替1024一次读完
            int len = -2;
            //read方法不仅将数据读入到byte数组中，还会返回读到了多少字节，返回值为-1时，说明已经全部读完了
            //用read的三个参数方法，而非只写数组对象的那个方法，好处是可以让最后一次的读取的长度更加灵活，可以不是指定的length
            //read只会替换数组中的元素，如果第二个只有512个元素那么只会替换前512个，那后512个就还是之前重复的
            while ((len=bufferedInputStream.read(inArray,0,inArray.length)) != -1){
                //已经将数据读到byte数组中了，直接把数组中的byte写到文件里就行了
                //将字节数组中的字节写入缓存中。数据会不断的追加，不会替换和消失，指定写入的长度和读取到的长度一致，避免重复写入
                bufferedOutputStream.write(inArray,0,len);
            }
            //必须要有的，将缓存中数据写入到文件中，并清掉缓存。
            bufferedOutputStream.flush();
            //关闭输入输出缓存流
            bufferedInputStream.close();
            bufferedOutputStream.close();
            //只需将缓冲流关闭，无需将节点流关闭，因为缓冲流内部会自动调用节点流关闭
        } catch (IOException e) {
            e.printStackTrace ();
        }
    }
}
