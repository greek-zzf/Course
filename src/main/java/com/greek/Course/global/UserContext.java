package com.greek.Course.global;

import com.greek.Course.model.SysUser;

/**
 * @author Zhaofeng Zhou
 * @since 2022/6/6
 */
public class UserContext {

    private static ThreadLocal<SysUser> userThreadLocal = new ThreadLocal<>();

    public static SysUser getCurrentUser() {
        return userThreadLocal.get();
    }

    public static void setCurrentUser(SysUser user) {
        userThreadLocal.set(user);
    }

    public static void clearUser() {
        userThreadLocal.set(null);
    }
}
