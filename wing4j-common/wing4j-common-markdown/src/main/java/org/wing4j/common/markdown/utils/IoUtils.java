package org.wing4j.common.markdown.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by wing4j on 2017/1/30.
 */
public class IoUtils {
    /**
     * 将文件读取成列表
     *
     * @param is      输入流
     * @param encoding 文件编码
     * @return 文本列表
     */
    public static List<String> read2list(InputStream is, String encoding) {
        List<String> lines = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is, encoding));
            String line = null;
            while ((line = br.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {

        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    //
                }
            }
        }
        return lines;
    }
}
