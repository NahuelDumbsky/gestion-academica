package com.poo.GestionAcademica.services;

import com.poo.GestionAcademica.entities.Course;
import com.poo.GestionAcademica.repositories.CourseRepository;
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

    public Course findById(int courseId){
        return courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found "));
    }
    
    public void deleteById(int courseId) {
        courseRepository.deleteById(courseId);
    }

}
