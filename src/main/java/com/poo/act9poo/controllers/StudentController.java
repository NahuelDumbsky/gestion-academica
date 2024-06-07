package com.poo.act9poo.controllers;

import com.poo.act9poo.entities.Student;
import com.poo.act9poo.services.StudentService;
import java.util.ArrayList;
import java.util.HashMap;

import org.hibernate.mapping.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/students/{student_id}")
public ResponseEntity<Object> getStudentById(@PathVariable("student_id") String studentIdStr) {
    try {
        int studentId = extractId(studentIdStr);

        // Buscar el estudiante por ID
        Student student = null;
        List<Student> students = studentService.findAll();
        for (Student s : students) {
            if (s.getStudentId() == studentId) {
                student = s;
                break;
            }
        }
        if (student == null) {
            throw new RuntimeException("Student not found");
        }

        HashMap<String, Object> studentData = new HashMap<>();
        studentData.put("studentId", student.getStudentId());
        studentData.put("firstName", student.getFirstName());
        studentData.put("lastName", student.getLastName());
        List<String> courses = student.getInscriptions().stream()
                .map(inscription -> "course" + inscription.getCourse().getCourseId())
                .collect(Collectors.toList());
        studentData.put("courses", courses);

        return new ResponseEntity<>(studentData, HttpStatus.OK);

    } catch (RuntimeException e) {
        HashMap<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}


private int extractId(String idString) {
        if (idString.matches("^student\\d+$")) {
            return Integer.parseInt(idString.replace("student", ""));
        } else if (idString.matches("^course\\d+$")) {
            return Integer.parseInt(idString.replace("course", ""));
        } else {
            throw new RuntimeException("Error en ingreso de id");
        }
    }
}
