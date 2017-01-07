package org.wing4j.orm.codegen;

import org.wing4j.common.logtrack.LogtrackRuntimeException;
import org.wing4j.common.logtrack.ErrorContextFactory;
import org.wing4j.orm.entity.metadata.TableMetadata;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

/**
 * Created by wing4j on 2017/1/7.
 */
public class DaoCodeGen {
    public static final String DEFAULT_HEAD = "/**\n" +
            " * the file is using DaoCodeGen auto codegen! \n" +
            " */";

    /**
     * 根据表元信息生成DAO类
     *
     * @param head              文件头部模板
     * @param tail              文件尾部模板
     * @param tableMetadatas    表元信息
     * @param daoPackageName    DAO包路径
     * @param entityPackageName 实体包路径
     * @param savePath          保存文件路径
     */
    public static void generate(String head, String tail, List<TableMetadata> tableMetadatas, String daoPackageName, String entityPackageName, String savePath) {
        savePath = savePath.endsWith("/") ? savePath.substring(0, savePath.length() - 1) : savePath;
        String parnetDir = savePath + "/" + daoPackageName.replaceAll("\\.", "/");
        File file = new File(parnetDir);
        if (!file.exists()) {
            file.mkdirs();
        }
        for (TableMetadata tableMetadata : tableMetadatas) {
            FileOutputStream fos = null;
            String entityClassName = tableMetadata.getClassName();
            String daoClassName = entityClassName.substring(0, entityClassName.length() - "Entity".length()) + "DAO";
            try {
                fos = new FileOutputStream(parnetDir + "/" + daoClassName + ".java");
                generate(head, tail, fos, tableMetadata, daoPackageName, entityPackageName, daoClassName);
            } catch (Exception e) {
                throw new LogtrackRuntimeException(ErrorContextFactory.instance()
                        .activity("生成实体{}的数据访问对象{}", tableMetadata.getClassName(), daoClassName)
                        .message("发生异常，导致存放到{}数据访问对象{}生成失败", daoPackageName, daoClassName)
                        .solution("联系开发者wing4j@foxmail.com")
                        .cause(e));
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

            }
        }

    }

    /**
     * 根据表元信息生成DAO类
     *
     * @param os                输出流
     * @param tableMetadata     表元信息
     * @param daoPackageName    DAO包路径
     * @param entityPackageName 实体包路径
     * @throws IOException 异常
     */
    public static void generate(String head, String tail, OutputStream os, TableMetadata tableMetadata, String daoPackageName, String entityPackageName, String daoClassName) throws IOException {
        String entityClassName = tableMetadata.getClassName();
        String primaryKey = tableMetadata.getPrimaryKeys().get(0);
        Class pkClass = tableMetadata.getColumnMetadatas().get(primaryKey).getJavaType();
        StringBuilder headBuf = new StringBuilder();
        if (head != null && !head.isEmpty()) {
            headBuf.append(head);
            if (!head.endsWith("\n")) {
                headBuf.append("\n");
            }
        }
        headBuf.append("package").append(" ").append(daoPackageName).append(";\n");
        headBuf.append("\n");
        headBuf.append("import").append(" ").append(entityPackageName).append(".").append(entityClassName).append(";\n");
        headBuf.append("import").append(" ").append("org.wing4j.orm.CrudMapper").append(";\n");
        headBuf.append("import").append(" ").append(pkClass.getName()).append(";\n");
        headBuf.append("\n");
        headBuf.append("public").append(" ").append("interface").append(" ").append(daoClassName).append(" ").append("extends CrudMapper<").append(entityClassName).append(", ").append(pkClass.getSimpleName()).append("> {\n");
        os.write(headBuf.toString().getBytes("UTF-8"));

        StringBuilder tailBuf = new StringBuilder();
        tailBuf.append("\n");
        tailBuf.append("}\n");
        if (tail != null && !tail.isEmpty()) {
            tailBuf.append(tail);
        }
        os.write(tailBuf.toString().getBytes("UTF-8"));
    }
}
