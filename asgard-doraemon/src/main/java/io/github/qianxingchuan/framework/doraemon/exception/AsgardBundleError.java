package io.github.qianxingchuan.framework.doraemon.exception;

/**
 * @author xingchuan.qxc
 * @date 2019/5/17 16:57
 */
public class AsgardBundleError {

    public static final AsgardErrorCode BUNDLE_COMMON_ERROR = new AsgardErrorCode("20480000", "${errorName} ,common error.");
    public static final AsgardErrorCode BUNDLE_FILE_NOT_EXIST = new AsgardErrorCode("204800001", "${bundleName} not exist.");
    private AsgardBundleError() {
        // no impl
    }

}
