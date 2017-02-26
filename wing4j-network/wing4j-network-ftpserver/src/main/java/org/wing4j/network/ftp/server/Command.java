package org.wing4j.network.ftp.server;
  
import java.io.Writer;
  
public interface Command {
    /** 
     * @param data    从ftp客户端接收的除ftp命令之外的数据 
     * @param writer  网络输出流 
     * @param t       控制连接所对应的处理线程 
     * */
     void getResult(String data,Writer writer,ControllerThread t);
}  