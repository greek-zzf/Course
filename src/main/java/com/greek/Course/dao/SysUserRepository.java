package com.greek.Course.dao;

import com.greek.Course.model.SysUser;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @author Zhaofeng Zhou
 * @since 2022/5/30
 */
public interface SysUserRepository extends CrudRepository<SysUser, Integer> {

    SysUser findByUsername(String username);

    Optional<SysUser> findByCookie(String cookie);

}
