package com.greek.Course.service;

import cn.hutool.core.util.StrUtil;
import com.greek.Course.annotation.Admin;
import com.greek.Course.dao.SysUserRepository;
import com.greek.Course.exception.HttpException;
import com.greek.Course.model.Role;
import com.greek.Course.model.Status;
import com.greek.Course.model.SysUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static java.util.stream.Collectors.toList;

/**
 * @author Zhaofeng Zhou
 * @since 2022/5/30
 */
@Service
public class SysUserService {

    private SysUserRepository sysUserRepository;
    private RoleService roleService;

    @Autowired
    SysUserService(SysUserRepository sysUserRepository, RoleService roleService) {
        this.sysUserRepository = sysUserRepository;
        this.roleService = roleService;
    }

    public SysUser getUserByUsername(String username) {
        return sysUserRepository.findSysUserByUsername(username);
    }

    public SysUser addSysUser(SysUser sysUser) {
//        sysUser.setCreatedAt(LocalDateTime.now());
        sysUser.setStatus(Status.OK);
        return sysUserRepository.save(sysUser);
    }


    @Admin
    public SysUser updateUser(Integer id, SysUser user) {
        SysUser userInDb = findById(id);

        Map<String, Role> nameToRole = roleService.getAllRoleToMap();
        List<Role> newRoles = userInDb.getRoles()
                .stream()
                .map(Role::getName)
                .map(nameToRole::get)
                .filter(Objects::nonNull)
                .collect(toList());

        userInDb.setRoles(newRoles);
        sysUserRepository.save(userInDb);
        return userInDb;
    }


    private SysUser findById(Integer id) {
        return sysUserRepository.findById(id)
                .orElseThrow(() -> HttpException.notFound("用户不存在！"));
    }


    @Admin
    public SysUser getUser(Integer id) {
        return findById(id);
    }

    @Admin
    public Page<SysUser> getAllUsers(String search, Integer pageSize, Integer pageNum, String orderBy, Sort.Direction sortRule) {
        Pageable page = StrUtil.isEmpty(orderBy) ?
                PageRequest.of(pageNum - 1, pageSize) :
                PageRequest.of(pageNum - 1, pageSize, sortRule);

        return StrUtil.isEmpty(search) ?
                sysUserRepository.findAll(page) :
                sysUserRepository.findBySearch(search, page);
    }
}
