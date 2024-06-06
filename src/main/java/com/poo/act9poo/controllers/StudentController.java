package com.poo.act9poo.controllers;

import com.poo.act9poo.entities.Student;
import com.poo.act9poo.services.StudentService;
import java.util.LinkedHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/students")
    public List<Map<String, Object>> getAllStudents() {
        List<Student> students = studentService.findAll();
        return students.stream()
                .map(student -> {
                    Map<String, Object> studentMap = new LinkedHashMap<>();
                    studentMap.put("studentId", "student" + student.getStudentId());
                    studentMap.put("firstName", student.getFirstName());
                    studentMap.put("lastName", student.getLastName());
                    studentMap.put("courses", student.getInscriptions().stream()
                            .map(inscription -> "course" + inscription.getCourse().getCourseId())
                            .collect(Collectors.toList()));
                    return studentMap;
                })
                .collect(Collectors.toList());
    }

}
