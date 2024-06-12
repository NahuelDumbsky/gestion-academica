package com.poo.GestionAcademica.controllers;

import com.poo.GestionAcademica.entities.Course;
import com.poo.GestionAcademica.entities.Inscription;
import com.poo.GestionAcademica.entities.Student;
import com.poo.GestionAcademica.services.CourseService;
import com.poo.GestionAcademica.services.InscriptionService;
import com.poo.GestionAcademica.services.StudentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class CourseController {

    @Autowired
    private CourseService courseService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private InscriptionService inscriptionService;

    @GetMapping("/courses")
    public List<List<Object>> getAllCourses() {
        List<Course> courses = courseService.findAll();
        return courses.stream()
                .map(course -> {
                    List<Object> courseData = new ArrayList<>();
                    courseData.add("courseId: course" + course.getCourseId());
                    courseData.add("courseName: " + course.getCourseName());
                    courseData.add("description: " + course.getDescription());
                    //Buscar sus estudiantes
                    List<String> students = course.getStudentsInscriptions().stream()
                            .map(inscription -> "student" + inscription.getStudent().getStudentId())
                            .collect(Collectors.toList());
                    courseData.add(Map.of("students", students));
                    return courseData;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/courses/{course_id}")
    public List<Object> getCourseById(@PathVariable("course_id") String courseIdStr) {
        try {
            int courseId = extractId(courseIdStr);

            Course course = courseService.findById(courseId);

            if (course == null) {
                throw new RuntimeException("Student not found");
            }

            // Construir la lista con los datos del curso y los estudiantes inscriptos
            List<Object> courseData = new ArrayList<>();
            courseData.add("courseId: course" + course.getCourseId());
            courseData.add("courseName: " + course.getCourseName());
            courseData.add("description: " + course.getDescription());
            //Buscar sus estudiantes
            List<String> students = course.getStudentsInscriptions().stream()
                    .map(inscription -> "student" + inscription.getStudent().getStudentId())
                    .collect(Collectors.toList());
            courseData.add(Map.of("students", students));

            return courseData;

        } catch (RuntimeException e) {
            return Collections.singletonList("error: " + e.getMessage());
        }
    }

    @PostMapping("/courses/{course_id}/enroll/{student_id}")
    public String enrollStudent(@PathVariable("course_id") String courseIdStr, @PathVariable("student_id") String studentIdStr) {
        try {
            int studentId = extractId(studentIdStr);
            int courseId = extractId(courseIdStr);

            //Buscamos estudiante
            Student student = studentService.findById(studentId);

            //Buscamos curso
            Course course = courseService.findById(courseId);

            // Buscar la inscripción del estudiante en el curso
            Inscription inscriptionToRemove = inscriptionService.findInscriptionByStudentIdAndCourseId(studentId, courseId);

            if (inscriptionToRemove != null) {
                return "{\n\tEnrollment already exists\n}";
            }

            // Crear la inscripción
            Inscription inscription = new Inscription();
            inscription.setStudent(student);
            inscription.setCourse(course);

            // Guardar la inscripción
            inscriptionService.save(inscription);

            // Agregar la inscripción al estudiante y al curso
            student.getInscriptions().add(inscription);
            course.getStudentsInscriptions().add(inscription);

            // Guardar los cambios en estudiantes y cursos
            studentService.save(student);
            courseService.save(course);

            return "{\n\tEnrollment successful\n}";
        } catch (RuntimeException e) {
            return "{\n\t" + e.getMessage() + "\n}";
        }
    }

    @DeleteMapping("/courses/{course_id}/enroll/{student_id}")
    public String unenrollStudent(@PathVariable("course_id") String courseIdStr, @PathVariable("student_id") String studentIdStr) {
        try {
            int studentId = extractId(studentIdStr);
            int courseId = extractId(courseIdStr);

            //Buscar estudiante
            Student student = studentService.findById(studentId);
            if (student == null) {
                throw new RuntimeException("Student not found");
            }

            // Buscar el curso
            Course course = courseService.findById(courseId);
            if (course == null) {
                throw new RuntimeException("Course not found");
            }

            // Buscar la inscripción del estudiante en el curso
            Inscription inscriptionToRemove = inscriptionService.findInscriptionByStudentIdAndCourseId(studentId, courseId);

            if (inscriptionToRemove == null) {
                throw new RuntimeException("Enrollment not found");
            }

            // Remover la inscripción del estudiante en el curso
            course.getStudentsInscriptions().remove(inscriptionToRemove);
            student.getInscriptions().remove(inscriptionToRemove);

            // Eliminar la inscripción de la base de datos
            inscriptionService.deleteById(inscriptionToRemove.getInscriptionId());

            // Guardar los cambios en estudiantes y cursos
            studentService.save(student);
            courseService.save(course);

            return "{\n\tUnenrollment successful\n}";
        } catch (RuntimeException e) {
            return "{\n\t" + e.getMessage() + "\n}";
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
