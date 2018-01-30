package com.ink.log;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.UUID;

/**
 * @author zhangyongchao[sanmao]
 * 2018/1/29
 **/
public class InkLogger {

    private Logger logger;
    private String className;

    public static InkLogger getLogger(Class clazz){
        return new InkLogger(clazz);
    }

    @Deprecated
    public InkLogger(Class clazz){
        className = clazz.getName();
        logger = LoggerFactory.getLogger(clazz);
        try {
            MDC.put(LogConst.HOST,InetAddress.getLocalHost().getHostAddress());
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        MDC.put(LogConst.USER,System.getProperty("user.name"));
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        MDC.put(LogConst.LOG_SEQ, uuid);
    }

    @Deprecated
    public InkLogger(){

    }

    /**
     * 系统日志info接口
     * @param content 操作内容
     */
    public void info(String content){
        stackTrace();
        logger.info(content);
        MDC.remove(LogConst.CLAZZ);
        MDC.remove(LogConst.METHOD);
        MDC.remove(LogConst.LINE);
        autoIncrSeq();
    }

    /**
     * 系统日志 info接口
     * @param content 操作内容
     * @param arguments
     */
    public void info(String content,Object... arguments){
        stackTrace();
        logger.info(content,arguments);
        MDC.remove(LogConst.CLAZZ);
        MDC.remove(LogConst.METHOD);
        MDC.remove(LogConst.LINE);

        autoIncrSeq();
    }

    /**
     * 系统日志 info 接口
     * @param module  模块
     * @param content 操作内容
     */
    public void info(String module,String content){
        info(module,null,content);
    }

    /**
     *  系统日志 info 接口
     * @param module  模块
     * @param infoId  业务ID
     * @param content 操作内容
     */
     public void info(String module,String infoId,String content){
         info(module,infoId,content,null,null);
     }

    /**
     *  系统日志 info 接口
     * @param module   模块
     * @param infoId   业务ID
     * @param content   操作内容
     * @param treadId   流水id
     */
     public void info(String module,String infoId,String content,String treadId){
         info(module,infoId,content,null,treadId);
     }

    /**
     *  系统日志 info  接口
     * @param module  模块
     * @param content 操作内容
     * @param result  操作结果
     */
    public void info(String module,String content,Boolean result){
        info(module,null,content,result,null);
    }
    /**
     *  系统日志info接口
     * @param module   模块
     * @param infoId   业务ID
     * @param content  操作内容
     * @param result   操作结果
     * @param treadId  流水ID
     */
    public void info(String module,String infoId,String content,Boolean result,String treadId){

        stackTrace();
        MDC.put(LogConst.OP_MODULE,module);
        if (null!=result){
            MDC.put(LogConst.OP_RESULT,true == result?"成功":"失败");
        }
        if (infoId!=null&&!"".equals(infoId)){
            MDC.put(LogConst.OP_INFOID,infoId);
        }
        if (treadId!=null&&!"".equals(treadId)){
            MDC.put(LogConst.TREAD_ID,treadId);
        }
        logger.info(content);

        MDC.remove(LogConst.OP_MODULE);
        MDC.remove(LogConst.OP_RESULT);
        MDC.remove(LogConst.OP_INFOID);
        MDC.remove(LogConst.TREAD_ID);
        MDC.remove(LogConst.CLAZZ);
        MDC.remove(LogConst.METHOD);
        MDC.remove(LogConst.LINE);

        autoIncrSeq();
    }


    /**
     * 系统日志error 接口
     * @param content 操作内容
     */
    public void error(String content){

        stackTrace();
        logger.error(content);
        MDC.remove(LogConst.CLAZZ);
        MDC.remove(LogConst.METHOD);
        MDC.remove(LogConst.LINE);
        autoIncrSeq();
    }

    /**
     * 系统日志 error接口
     * @param content 操作内容
     * @param throwable 异常
     */
    public void error(String content,Throwable throwable){

        stackTrace();
        logger.error(content,throwable);
        MDC.remove(LogConst.CLAZZ);
        MDC.remove(LogConst.METHOD);
        MDC.remove(LogConst.LINE);
        autoIncrSeq();
    }

    /**
     *  系统日志 error 接口
     * @param module  模块
     * @param content  操作内容
     */
    public void error(String module,String content){
        error(module,content,null);
    }

    /**
     *  系统日志 error 接口
     * @param module  模块
     * @param content  操作内容
     * @param throwable  异常
     */
    public void error(String module,String content,Throwable throwable){
        error(module,null,content,throwable,null);
    }
    /**
     *  系统日志 error 接口
     * @param module   模块
     * @param infoId   业务ID
     * @param content  操作内容
     * @param treadId   流水ID
     */
    public void error(String module,String infoId,String content,String treadId){

        error(module,infoId,content,null,treadId);
    }

    /**
     *  系统日志 error 接口
     * @param module      模块
     * @param infoId      业务ID
     * @param content     操作内容
     * @param throwable   异常
     * @param treadId     流水ID
     */
    public void error(String module,String infoId,String content,Throwable throwable,String treadId){

        stackTrace();

        MDC.put(LogConst.OP_MODULE,module);
        MDC.put(LogConst.OP_RESULT,"失败");
        if(null!=infoId&&!"".equals(infoId)){
            MDC.put(LogConst.OP_INFOID,infoId);
        }
        if (null!=treadId&&!"".equals(infoId)){
            MDC.put(LogConst.TREAD_ID,treadId);
        }

        if (null == throwable ){
            logger.error(content);
        }else {
            MDC.put(LogConst.OP_EXCEPTIONMSG,getExceptionMessage(throwable));
            logger.error(content,throwable);
        }

        MDC.remove(LogConst.OP_MODULE);
        MDC.remove(LogConst.OP_RESULT);
        MDC.remove(LogConst.TREAD_ID);
        MDC.remove(LogConst.OP_INFOID);
        MDC.remove(LogConst.CLAZZ);
        MDC.remove(LogConst.METHOD);
        MDC.remove(LogConst.LINE);

        autoIncrSeq();
    }

    /**
     * 日志序列 UUID
     */
    public void autoIncrSeq(){
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        MDC.put(LogConst.LOG_SEQ, uuid);
    }

    private String getExceptionMessage(Throwable t) {
        StringBuffer remark=new StringBuffer("异常类型:"+t.getClass().getName()+";异常信息:"+t.getMessage()+"  ");
        StackTraceElement[] trace=t.getStackTrace();
        for (int i=0; i < trace.length; i++){
            remark.append("\r\n"+trace[i]+";");
            if(remark.length()>800){
                break;
            }
        }
        return remark.toString();
    }

    public void stackTrace(){
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement log =null;
        for (int i = 1; i < stackTrace.length; i++) {
            StackTraceElement e = stackTrace[i];
            if (e.getClassName().equals(className)) {
                log = e;
                break;
            }
        }
        MDC.put(LogConst.CLAZZ,log.getClassName());
        MDC.put(LogConst.METHOD,log.getMethodName());
        MDC.put(LogConst.LINE,String.valueOf(log.getLineNumber()));
    }
}
