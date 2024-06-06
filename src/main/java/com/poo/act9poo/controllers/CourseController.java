package com.poo.act9poo.controllers;

import com.poo.act9poo.entities.Course;
import com.poo.act9poo.services.CourseService;
import java.util.LinkedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/courses")
    public List<Map<String, Object>> getAllCourses() {
        List<Course> courses = courseService.findAll();
        return courses.stream()
                .map(course -> {
                    Map<String, Object> courseMap = new LinkedHashMap<>();
                    courseMap.put("courseId", "course" + course.getCourseId());
                    courseMap.put("courseName", course.getCourseName());
                    courseMap.put("description", course.getDescription());
                    courseMap.put("students", course.getStudentsInscriptions().stream()
                            .map(inscription -> "student" + inscription.getStudent().getStudentId())
                            .collect(Collectors.toList()));
                    return courseMap;
                })
                .collect(Collectors.toList());
    }
    
}
