package com.greek.Course.service;

import com.greek.Course.dao.SysUserRepository;
import com.greek.Course.model.Status;
import com.greek.Course.model.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author Zhaofeng Zhou
 * @since 2022/5/30
 */
@Service
public class SysUserService {

    private SysUserRepository sysUserRepository;

    @Autowired
    SysUserService(SysUserRepository sysUserRepository) {
        this.sysUserRepository = sysUserRepository;
    }

    public SysUser getUserByUsername(String username) {
        return sysUserRepository.findByUsername(username);
    }

    public SysUser addSysUser(SysUser sysUser) {
        sysUser.setCreatedAt(LocalDateTime.now());
        sysUser.setStatus(Status.OK);
        return sysUserRepository.save(sysUser);
    }


}
