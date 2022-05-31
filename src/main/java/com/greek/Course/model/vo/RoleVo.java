package com.greek.Course.model.vo;

import com.greek.Course.model.Status;

/**
 * @author Zhaofeng Zhou
 * @since 2022/5/31
 */
public class RoleVo {
    private String name;
    private Status status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
