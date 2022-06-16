package com.greek.Course.controller;

import com.greek.Course.exception.HttpException;
import com.greek.Course.model.Course;
import com.greek.Course.model.PageResponse;
import com.greek.Course.service.CourseService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author Zhaofeng Zhou
 * @date 2022/6/15
 */
@RestController
@RequestMapping("/api/v1")
public class CourseController {

    private CourseService courseService;

    CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @PostMapping("/course")
    public Course createCourse(@RequestBody Course course) {
        return courseService.createCourse(course);
    }

    @PatchMapping("/course/{id}")
    public Course updateCourse(@PathVariable("id") Integer courseId, @RequestBody Course course) {
        return courseService.updateCourse(courseId, course);
    }

    @DeleteMapping("/course/{id}")
    public void deleteCourse(@PathVariable("id") Integer courseId) {
        courseService.deleteCourse(courseId);
    }

    @GetMapping("/course/{id}")
    public Course getCourse(@PathVariable Integer id) {
        return courseService.findById(id);
    }

    @GetMapping("/course/list")
    public List<Course> getCourseList() {
        return courseService.getCourseList();
    }

    @GetMapping("/course/page")
    public PageResponse getAllCourse(
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") Integer pageSize,
            @RequestParam(value = "pageNum", required = false, defaultValue = "1") Integer pageNum,
            @RequestParam(value = "orderBy", required = false) String orderBy,
            @RequestParam(value = "orderType", required = false) String orderType) {

        if (orderType != null && orderBy == null) {
            throw HttpException.badRequest("缺少orderBy!");
        }

        Page<Course> response = courseService.getAllCourse(search, pageSize, pageNum, orderBy, orderType == null ? null : Sort.Direction.fromString(orderType));
        return PageResponse.of(response, pageNum, pageSize);
    }

}
