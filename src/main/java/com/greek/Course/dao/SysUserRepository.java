package com.greek.Course.dao;

import com.greek.Course.model.SysUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

/**
 * @author Zhaofeng Zhou
 * @since 2022/5/30
 */
public interface SysUserRepository extends JpaRepository<SysUser, Integer> {

    SysUser findByUsername(String username);

    @Query(value = "select u from SysUser u where u.username like %:search%")
    Page<SysUser> findBySearch(@Param("search") String search, Pageable pageable);
}
