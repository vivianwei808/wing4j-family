package org.wing4j.toolkit;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by woate on 2017/1/4.
 */
public class Help {
    public static List<String> toList(CommandCollection commandCollection) {
        List<String> lines = new ArrayList<>();
        Map<String, CommandDefine> defines = commandCollection.getOptionCollection();
        List<String> cmds = new ArrayList<>(defines.keySet());
        for (int i = 0; i < cmds.size(); i++) {
            String cmd = cmds.get(i);
            CommandDefine define = defines.get(cmd);
            StringBuilder build = new StringBuilder();
            build.append(i).append(". ").append(define.getName()).append(" ").append(cmd).append("\n");
            build.append("\t").append(cmd).append(" ");
            for (String name : define.options.keySet()){
                Option option = define.options.get(name);
                build.append("-").append(name).append(" ").append(option.example).append(" ");
            }
            build.append("\n");
            build.append("\texample:").append(define.example).append("\n");
            for (String name : define.options.keySet()){
                Option option = define.options.get(name);
                build.append("\t").append(name).append(":").append(option.desc).append("\n");
            }
            lines.add(build.toString());
        }
        return lines;
    }

    public static String toString(CommandCollection commandCollection){
        StringBuilder build = new StringBuilder();
        List<String> lines = toList(commandCollection);
        for (String line : lines){
            build.append(line);
        }
        return build.toString();
    }
}
