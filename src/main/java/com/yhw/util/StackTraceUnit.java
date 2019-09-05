package com.yhw.util;

public class StackTraceUnit {

    static int defaultLevel = 6;
    /**
     * 当前方法：level = 1
     *
     * @param level 包括出错方法在内，共要查看多少层调用
     *
     * @return the stack trace
     */
    public static String getStackTrace(int... level) {
        int maxLevel = 1; //当前方法，占用了[1]
        if (level.length > 1) {
            maxLevel += level[0];
        } else {
            maxLevel += defaultLevel;
        }

        String stackInfo = "";
        //if leavel=3 .则取[2][3][4]
        for (int i=2;i<= maxLevel;i++) {
            String className = Thread.currentThread ().getStackTrace ()[i].getClassName ();
            className = StringUtil.substringAfterLast (className, ".");
            String methodName = Thread.currentThread ().getStackTrace ()[i].getMethodName ();
            int lineNum = Thread.currentThread ().getStackTrace ()[i].getLineNumber ();
            if (i==2) {
                stackInfo += "\n\t at "+className + "." + methodName + ":" + lineNum;
            } else {
                stackInfo += "\n\t up "+className + "." + methodName + ":" + lineNum;
            }

        }

        return stackInfo;
    }

}
