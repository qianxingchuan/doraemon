package com.xingchuan.framework.doraemon.constants;

/**
 * Constants defined here.
 *
 * @author xingchuan.qxc
 * @date 2019/5/15 21:00
 */
public interface AsgardMouleConstants {

    enum ModuleDirectoryPrefix {

        /**
         * prefix for directory <b>BUNDLE-CLASS<b/>
         */
        MODULE_DIR_PREFIX_BUNDLE_CLASS("BUNDLE-CLASS/"),

        /**
         * prefix for directory <b>lib</b>
         */
        MODULE_DIR_PREFIX_LIB("lib/"),

        /**
         * prefix for directory <b>META-INF</b>
         */
        MODULE_DIR_PREFIX_META_INF("META-INF/");

        private String prefix;

        ModuleDirectoryPrefix(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return this.prefix;
        }

    }

    /**
     * Url prefix
     */
    enum UrlPrefix {
        /**
         * file
         */
        URL_PREFIX_FILE("file:");

        private String prefix;

        UrlPrefix(String prefix) {
            this.prefix = prefix;
        }

        public String getPrefix() {
            return this.prefix;
        }
    }
}
