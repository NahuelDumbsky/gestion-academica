package com.poo.act9poo.controllers;

import com.poo.act9poo.entities.Student;
import com.poo.act9poo.services.StudentService;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/students")
    public List<List<Object>> getAllStudents() {
        List<Student> students = studentService.findAll();
        return students.stream()
                .map(student -> {
                    List<Object> studentData = new ArrayList<>();
                    studentData.add("student" + student.getStudentId());
                    studentData.add(student.getFirstName());
                    studentData.add(student.getLastName());
                    List<String> courses = student.getInscriptions().stream()
                            .map(inscription -> "course" + inscription.getCourse().getCourseId())
                            .collect(Collectors.toList());
                    studentData.add(courses);
                    return studentData;
                })
                .collect(Collectors.toList());
    }
}
