package com.xingchuan.framework.doraemon.exception;

import java.io.Serializable;

/**
 * @author xingchuan.qxc
 * @date 2019/5/17 16:49
 */
public class AsgardException extends RuntimeException implements Serializable {

    private final AsgardErrorCode errorCode;

    public AsgardException(AsgardErrorCode errorCode) {
        super();
        this.errorCode = errorCode;
    }

    @Override
    public String getMessage() {
        if (super.getMessage() == null && errorCode != null) {
            return errorCode.getErrorMsg();
        }
        return super.getMessage();
    }
}
