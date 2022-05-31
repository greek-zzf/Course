package com.greek.Course.controller;

import cn.hutool.core.bean.BeanUtil;
import com.greek.Course.model.Role;
import com.greek.Course.model.vo.RoleVo;
import com.greek.Course.service.RoleService;
import org.springframework.web.bind.annotation.*;

/**
 * @author Zhaofeng Zhou
 * @since 2022/5/31
 */
@RestController
@RequestMapping("role")
public class RoleController {
    private RoleService roleService;

    RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/insert")
    public Role insertRole(@RequestBody RoleVo roleVo) {
        Role roleToInsert = new Role();
        BeanUtil.copyProperties(roleVo, roleToInsert);
        return roleService.addRole(roleToInsert);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteRole(@PathVariable("id") Integer roleId) {
        roleService.deleteRole(roleId);
    }

    @PatchMapping("/update")
    public Role patchUpdate(@RequestBody RoleVo roleVo) {
        return roleService.updateRole(roleVo);
    }


}
