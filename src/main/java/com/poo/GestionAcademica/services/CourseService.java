package com.poo.GestionAcademica.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poo.GestionAcademica.entities.Course;
import com.poo.GestionAcademica.repositories.CourseRepository;

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
                .orElse(null);
    }
    
    public void deleteById(int courseId) {
        courseRepository.deleteById(courseId);
    }

}
