package com.greek.Course.service;

import com.greek.Course.dao.SysUserRepository;
import com.greek.Course.model.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

    public Optional<SysUser> getUserByUsername(String username) {
        return Optional.ofNullable(sysUserRepository.findByUsername(username));
    }


}
