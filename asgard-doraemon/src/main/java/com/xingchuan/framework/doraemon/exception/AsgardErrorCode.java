package com.xingchuan.framework.doraemon.exception;

import org.apache.commons.lang3.RegExUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * error code define.
 *
 * @author xingchuan.qxc
 * @date 2019/5/17 16:37
 */
public class AsgardErrorCode implements Serializable {

    private String errorCode;

    private String errorMsg;

    public AsgardErrorCode(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }

    public MessageFormatter custom() {
        return new MessageFormatter(this);
    }

    public void throwException() {
        throw new AsgardException(this);
    }

    public AsgardException getAsgardException() {
        return new AsgardException(this);
    }

    public class MessageFormatter {

        private Map<String, String> params = new HashMap<>();

        private AsgardErrorCode asgardErrorCode;

        public MessageFormatter(AsgardErrorCode asgardErrorCode) {
            this.asgardErrorCode = asgardErrorCode;
        }

        public MessageFormatter withParam(String paramName, String paramValue) {
            this.params.put(paramName, paramValue);
            return this;
        }

        public AsgardErrorCode format() {
            Set<Map.Entry<String, String>> paramsEntries = params.entrySet();
            String errorMsgBuild = asgardErrorCode.getErrorMsg();
            for (Map.Entry<String, String> paramsEntry : paramsEntries) {
                errorMsgBuild = RegExUtils.replaceAll(errorMsgBuild, "\\$\\{" + paramsEntry.getKey() + "\\}", paramsEntry.getValue());
            }
            return new AsgardErrorCode(asgardErrorCode.getErrorCode(), errorMsgBuild);
        }
    }
}
