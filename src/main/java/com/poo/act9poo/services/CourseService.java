package com.poo.act9poo.services;

import com.poo.act9poo.entities.Course;
import com.poo.act9poo.repositories.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    public List<Course> findAll() {
        return courseRepository.findAll();
    }

    public Course save(Course course) {
        return courseRepository.save(course);
    }

    public void deleteById(String courseId) {
        courseRepository.deleteById(courseId);
    }
}
