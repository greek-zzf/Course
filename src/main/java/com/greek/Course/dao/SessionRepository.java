package com.greek.Course.dao;

import com.greek.Course.model.Session;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * @author Zhaofeng Zhou
 * @since 2022/6/7
 */
public interface SessionRepository extends CrudRepository<Session, Integer> {

    Optional<Session> findByCookie(String cookie);

    void deleteByCookie(String cookie);

}
