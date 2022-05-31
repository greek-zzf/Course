package com.greek.Course.dao;

import com.greek.Course.model.Role;
import org.springframework.data.repository.CrudRepository;

/**
 * @author Zhaofeng Zhou
 * @since 2022/5/31
 */
public interface RoleRepository extends CrudRepository<Role, Integer> {
}
