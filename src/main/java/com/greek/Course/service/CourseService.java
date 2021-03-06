package com.greek.Course.service;

import cn.hutool.core.util.StrUtil;
import com.greek.Course.annotation.Admin;
import com.greek.Course.dao.CourseRepository;
import com.greek.Course.dao.VideoRepository;
import com.greek.Course.exception.HttpException;
import com.greek.Course.model.Course;
import com.greek.Course.model.Status;
import com.greek.Course.model.SysUser;
import com.greek.Course.model.Video;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author Zhaofeng Zhou
 * @date 2022/6/15
 */
@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private VideoRepository videoRepository;

    public Course createCourse(Course course) {
        Course saveResult = courseRepository.save(course);

        List<Video> videos = course.getVideos();
        videos.forEach(e -> e.setCourse(course));
        videoRepository.saveAll(videos);

        return saveResult;
    }

    public Course updateCourse(Integer courseId, Course course) {
        Course courseInDb = findById(courseId);
        courseInDb.setName(course.getName());
        courseInDb.setTeacherName(course.getTeacherName());
        courseInDb.setTeacherDescription(course.getTeacherDescription());
        courseInDb.setPrice(course.getPrice());

        courseRepository.save(courseInDb);
        return courseInDb;
    }

    public void deleteCourse(Integer courseId) {
        courseRepository.findById(courseId)
                .ifPresent(e -> {
                    e.setStatus(Status.DELETED);
                    courseRepository.save(e);
                });
    }

    public Course findById(Integer id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> HttpException.notFound("??????????????????"));
    }


    @Admin
    public Page<Course> getAllCourse(String search, Integer pageSize, Integer pageNum, String orderBy, Sort.Direction sortRule) {
        Pageable page = StrUtil.isEmpty(orderBy) ?
                PageRequest.of(pageNum - 1, pageSize) :
                PageRequest.of(pageNum - 1, pageSize, sortRule);

        return StrUtil.isEmpty(search) ?
                courseRepository.findAll(page) :
                courseRepository.findBySearch(search, page);
    }

    public List<Course> getCourseList() {
        return courseRepository.findAll();
    }
}
