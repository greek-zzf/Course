package com.greek.Course.service;

import com.greek.Course.dao.RoleRepository;
import com.greek.Course.model.Role;
import com.greek.Course.model.vo.RoleVo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * @author Zhaofeng Zhou
 * @since 2022/5/31
 */
@Service
public class RoleService {

    private RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public void addRole(Role role) {
        role.setCreatedAt(LocalDateTime.now());
        roleRepository.save(role);
    }

    public void deleteRole(int roleId) {
        roleRepository.deleteById(roleId);
    }

    public void updateRole(Role role){

    }
}
