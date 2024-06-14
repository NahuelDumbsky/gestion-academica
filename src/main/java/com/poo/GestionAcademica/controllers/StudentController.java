package com.poo.GestionAcademica.controllers;

import com.poo.GestionAcademica.entities.Student;
import com.poo.GestionAcademica.services.StudentService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@CrossOrigin(origins = "http://localhost:4200/")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping("/students")
    public List<List<Object>> getAllStudents() {
        List<Student> students = studentService.findAll();
        return students.stream()
                .map(student -> {
                    List<Object> studentData = new ArrayList<>();
                    studentData.add("studentId: student" + student.getStudentId());
                    studentData.add("firstName: " + student.getFirstName());
                    studentData.add("lastName: " + student.getLastName());
                    List<String> courses = student.getInscriptions().stream()
                            .map(inscription -> "course" + inscription.getCourse().getCourseId())
                            .collect(Collectors.toList());
                    studentData.add(Map.of("courses", courses));
                    return studentData;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/students/{student_id}")
    public List<Object> getStudentById(@PathVariable("student_id") String studentIdStr) {
        try {
            int studentId = extractId(studentIdStr);
            
            Student student = studentService.findById(studentId);
            
            if(student == null){
                throw new RuntimeException ("Student not found");
            }
            
            // Construir la lista con los datos del estudiante y los cursos inscriptos
            List<Object> studentData = new ArrayList<>();
            studentData.add("studentId: student" + student.getStudentId());
            studentData.add("firstName: " + student.getFirstName());
            studentData.add("lastName: " + student.getLastName());

            List<String> courses = student.getInscriptions().stream()
                    .map(inscription -> "course" + inscription.getCourse().getCourseId())
                    .collect(Collectors.toList());
            studentData.add(Map.of("courses", courses));

            return studentData;

        } catch (RuntimeException e) {
            return Collections.singletonList("error: " + e.getMessage());
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
