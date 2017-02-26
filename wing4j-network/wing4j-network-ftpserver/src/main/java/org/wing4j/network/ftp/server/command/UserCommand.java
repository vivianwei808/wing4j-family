package org.wing4j.network.ftp.server.command;

import org.wing4j.network.ftp.server.ControllerThread;
import org.wing4j.network.ftp.server.Share;
import org.wing4j.network.ftp.server.Command;

import java.io.IOException;
import java.io.Writer;

public class UserCommand implements Command {
      
    /** 
     * 检验是否有这个用户名存在 
     * */  
    @Override  
    public void getResult(String data,Writer writer,ControllerThread t) {
        String response = "";  
        if(Share.users.containsKey(data)) {
            ControllerThread.USER.set(data);  
            response = "331";  
        }  
        else {  
            response = "501";  
        }  
          
        try {  
            writer.write(response);  
            writer.write("\r\n");  
            writer.flush();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
          
          
    }  
  
} 