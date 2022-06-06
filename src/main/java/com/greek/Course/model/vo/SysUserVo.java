package com.greek.Course.model.vo;

import com.greek.Course.model.SysUser;

/**
 * @author Zhaofeng Zhou
 * @since 2022/6/6
 */
public class SysUserVo {
    private Integer id;
    private String username;

    public static SysUserVo toSysUserVo(SysUser sysUser) {
        SysUserVo sysUserVo = new SysUserVo();
        sysUserVo.setId(sysUser.getId());
        sysUserVo.setUsername(sysUser.getUsername());
        return sysUserVo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
