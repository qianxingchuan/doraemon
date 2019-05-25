package io.github.xingchuan.framework.doraemon.bundle;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashSet;
import java.util.Set;

/**
 * the bundle configuration
 *
 * @author xingchuan.qxc
 * @date 2019/5/17 16:00
 */
public class BundleConfiguration {

    public static final String PATH_END_CHARACTOR = "/";
    private String bundleZipFilePath;

    private String bundleExtractPath = "~";

    /**
     * bundle load all classes exclude the class in <b>loadClassSkipList</b>
     */
    private Set<String> loadClassSkipList = new HashSet<>();

    /**
     * the classes can be shown to parent classloader when bundle init over.
     */
    private Set<String> exportClassList = new HashSet<>();

    private String initInstanceClass;

    public BundleConfiguration(String bundleZipFilePath, String bundleExtractPath) {
        String moduleName = FilenameUtils.getBaseName(bundleZipFilePath);
        if (StringUtils.isBlank(bundleExtractPath)) {
            bundleExtractPath = "~/doraemon-bundle/" + moduleName + PATH_END_CHARACTOR;
        }
        if (!StringUtils.endsWith(bundleExtractPath, PATH_END_CHARACTOR)) {
            bundleExtractPath = bundleExtractPath + PATH_END_CHARACTOR;
        }
        this.bundleZipFilePath = bundleZipFilePath;
        this.bundleExtractPath = bundleExtractPath;
        Set<String> defaultSkipClassSet = new HashSet<>();
        defaultSkipClassSet.add("io.github.xingchuan.framework.doraemon.BundleService");
        addSkipClassFullName(defaultSkipClassSet);
    }

    public String getBundleZipFilePath() {
        return bundleZipFilePath;
    }

    public String getBundleExtractPath() {
        return bundleExtractPath;
    }

    public void addSkipClassFullName(Set<String> fullNameSet) {
        this.loadClassSkipList.addAll(fullNameSet);
    }

    public boolean containSkipClass(String fullName) {
        return this.loadClassSkipList.contains(fullName);
    }

    public void addExportClassFullName(Set<String> fullNames) {
        this.exportClassList.addAll(fullNames);
    }

    public Set<String> getExportClassList() {
        return exportClassList;
    }

    public String getInitInstanceClass() {
        return initInstanceClass;
    }

    public void setInitInstanceClass(String initInstanceClass) {
        this.initInstanceClass = initInstanceClass;
    }
}
