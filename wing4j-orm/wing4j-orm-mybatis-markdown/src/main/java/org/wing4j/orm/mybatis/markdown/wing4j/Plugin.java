package org.wing4j.orm.mybatis.markdown.wing4j;

/**
 * Created by woate on 2017/1/10.
 */
public interface Plugin {
    boolean accpet(LifeCycle lifeCycle);
    void execute(MarkdownContext markdownContext, RuntimeContext runtimeContext);
}
