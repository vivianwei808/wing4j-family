package org.wing4j.orm.mybatis.spring.exception;

import org.springframework.dao.UncategorizedDataAccessException;

public class MyBatisException extends UncategorizedDataAccessException {

  private static final long serialVersionUID = -5284728621670758939L;

  public MyBatisException(Throwable cause) {
    super(null, cause);
  }

}
