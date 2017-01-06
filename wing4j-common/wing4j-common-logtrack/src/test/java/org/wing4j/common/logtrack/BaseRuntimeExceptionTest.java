package org.wing4j.common.logtrack;

import org.junit.Test;

/**
 * Created by woate on 2017/1/6.
 */
public class BaseRuntimeExceptionTest {

    void makeException() {
        throw new RuntimeException("this is a root exception");
    }
    void wrappeException2() {
        try {
            makeException();
        } catch (Exception e) {
            throw new BaseRuntimeException(ErrorContextFactory.instance()
                    .code("1")
                    .desc("发生致命错误")
                    .activity("this is doing {}", "1")
                    .message("this is cause {}", "do fisrt thing")
                    .solution("this is solution {}", "A")
                    .cause(e));
        }
    }
    void wrappeException1() {
        try {
            makeException();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    @Test
    public void testToString1() throws Exception {
        try{
            wrappeException1();
        }catch (Exception e){
            System.out.println(e);
            e.printStackTrace();
        }

    }
    @Test
    public void testToString2() throws Exception {
        try{
            wrappeException2();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}