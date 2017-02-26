package org.wing4j.network.ftp.server;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;  
import java.util.Map;
/**
 * 所有线程共享的变量 
 * */  
public class Share {  
      
    /*根目录的路径*/  
    public static  String rootDir = "C:"+File.separator;  
      
    /*允许登录的用户*/  
    public static Map<String,String> users = new HashMap<String,String>();  
          
    /*已经登录的用户*/  
    public static HashSet<String> loginedUser = new HashSet<String>();  
      
    /*拥有权限的用户*/  
    public static HashSet<String> adminUsers = new HashSet<String>();  
      
    //初始化根目录，权限用户，能够登录的用户信息  
    public static void init(){
    }
}  