package org.wing4j.network.ftp.server.command;

import org.wing4j.network.ftp.server.Command;
import org.wing4j.network.ftp.server.ControllerThread;

import java.io.BufferedWriter;
import java.io.File;  
import java.io.IOException;  
import java.io.OutputStreamWriter;  
import java.io.Writer;  
import java.net.Socket;  
import java.net.UnknownHostException;  
  
public class ListCommand implements Command {
  
    /** 
     * 获取ftp目录里面的文件列表 
     * */  
    @Override  
    public void getResult(String data, Writer writer,ControllerThread t) {
        String desDir = t.getNowDir()+data;  
        System.out.println(desDir);  
        File dir = new File(desDir);  
        if(!dir.exists()) {  
            try {  
                writer.write("210  文件目录不存在\r\n");  
                writer.flush();  
            } catch (IOException e) {  
                e.printStackTrace();  
            }  
        }  
        else   
        {  
            StringBuilder dirs = new StringBuilder();  
            System.out.println("文件目录如下：");  
            dirs.append("文件目录如下:\n");  
            String[] lists= dir.list();  
            String flag = null;  
            for(String name : lists) {  
                System.out.println(name);  
                File temp = new File(desDir+File.separator+name);  
                if(temp.isDirectory()) {  
                    flag = "d";  
                }  
                else {  
                    flag = "f";  
                }  
                dirs.append("\t");  
                dirs.append(flag);  
                dirs.append("  ");  
                dirs.append(name);  
                dirs.append("\n");  
                  
            }  
              
            //开启数据连接，将数据发送给客户端，这里需要有端口号和ip地址  
             Socket s;  
            try {  
                 writer.write("150 open ascii mode...\r\n");  
                 writer.flush();  
                 s = new Socket(t.getDataIp(), Integer.parseInt(t.getDataPort()));  
                 BufferedWriter dataWriter = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));  
                 dataWriter.write(dirs.toString());  
                 dataWriter.flush();  
                 s.close();  
                 writer.write("220 transfer complete...\r\n");  
                 writer.flush();  
            } catch (NumberFormatException e) {  
                  
                e.printStackTrace();  
            } catch (UnknownHostException e) {  
              
                e.printStackTrace();  
            } catch (IOException e) {  
                  
                e.printStackTrace();  
            }  
        }  
          
    }  
  
} 