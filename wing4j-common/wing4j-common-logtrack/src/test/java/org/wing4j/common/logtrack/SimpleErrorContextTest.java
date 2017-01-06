package org.wing4j.common.logtrack;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by woate on 2017/1/6.
 */
public class SimpleErrorContextTest {
    @Test
    public void testString(){
        ErrorContext ctx = ErrorContextFactory.instance()
                .cause(new RuntimeException())
                .solution("this is a solution")
                .message("this is a message")
                .activity("this is a activity")
                .code("AAAAAAA")
                .desc("成功")
                .addSubError(new SubError("1", "错误1"))
                .addSubError(new SubError("2", "错误2"));
        System.out.println(ctx);
    }

    @Test
    public void testString2(){
        ErrorContext ctx = ErrorContextFactory.instance()
                .cause(new RuntimeException())
                .solution("this is a solution {}", "A")
                .message("this is a message {}", "B")
                .activity("this is a activity {}", "C")
                .code("AAAAAAA")
                .desc("成功")
                .addSubError(new SubError("1", "错误1"))
                .addSubError(new SubError("2", "错误2"));
        System.out.println(ctx);
    }
}