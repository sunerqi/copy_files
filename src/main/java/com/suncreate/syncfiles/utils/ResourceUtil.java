package com.suncreate.syncfiles.utils;

import java.util.ResourceBundle;

/**
 * <p>
 * Title: ResourceUtil.java
 * </p>
 * <p>
 * Description:system.properties配置文件工具类
 * </p>
 * <p>
 * Copyright: Copyright (c) 2014
 * </p>
 *
 * @Author: sunhailong
 * @Date: 2019/8/9 下午3:31
 * @Desc: 复制文件夹或文件夹
 */
public class ResourceUtil {
    private static final ResourceBundle bundle = java.util.ResourceBundle.getBundle("application");

    public static String getResourceUrl() {
        return bundle.getString("resource_url");
    }

    public static String getToUrl() {
        return bundle.getString("to_url");
    }
}
