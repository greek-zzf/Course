package com.greek.Course.dao;

import com.greek.Course.model.Course;
import com.greek.Course.model.SysUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CourseRepository extends JpaRepository<Course, Integer> {

    @Query(value = "select u from Course u where u.name like %:search%")
    Page<Course> findBySearch(@Param("search") String search, Pageable pageable);
}
