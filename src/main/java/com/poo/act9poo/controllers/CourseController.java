package com.poo.act9poo.controllers;

import com.poo.act9poo.entities.Course;
import com.poo.act9poo.entities.Inscription;
import com.poo.act9poo.entities.Student;
import com.poo.act9poo.services.CourseService;
import com.poo.act9poo.services.InscriptionService;
import com.poo.act9poo.services.StudentService;
import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
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
                    courseData.add("course" + course.getCourseId());
                    courseData.add(course.getCourseName());
                    courseData.add(course.getDescription());
                    List<String> students = course.getStudentsInscriptions().stream()
                            .map(inscription -> "student" + inscription.getStudent().getStudentId())
                            .collect(Collectors.toList());
                    courseData.add(students);
                    return courseData;
                })
                .collect(Collectors.toList());
    }
    
@GetMapping("/courses/{course_id}")
public ResponseEntity<Object> getCourseById(@PathVariable("course_id") String courseIdStr) {
    try {
        int courseId = extractId(courseIdStr);

        // Buscar el curso por ID
        Course course = null;
        List<Course> courses = courseService.findAll();
        for (Course c : courses) {
            if (c.getCourseId() == courseId) {
                course = c;
                break;
            }
        }
        if (course == null) {
            throw new RuntimeException("Course not found");
        }

        HashMap<String, Object> courseData = new HashMap<>();
        courseData.put("courseId", "course" + course.getCourseId());
        courseData.put("courseName", course.getCourseName());
        courseData.put("description", course.getDescription());
        List<String> students = course.getStudentsInscriptions().stream()
                .map(inscription -> "student" + inscription.getStudent().getStudentId())
                .collect(Collectors.toList());
        courseData.put("students", students);

        return new ResponseEntity<>(courseData, HttpStatus.OK);

    } catch (RuntimeException e) {
        HashMap<String, String> error = new HashMap<>();
        error.put("error", e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }
}

    @PostMapping("/courses/{course_id}/enroll/{student_id}")
    public String enrollStudent(@PathVariable("course_id") String courseIdStr, @PathVariable("student_id") String studentIdStr) {
        try {
            int studentId = extractId(studentIdStr);
            int courseId = extractId(courseIdStr);

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

            // Buscar el curso por ID
            Course course = null;
            List<Course> courses = courseService.findAll();
            for (Course c : courses) {
                if (c.getCourseId() == courseId) {
                    course = c;
                    break;
                }
            }
            if (course == null) {
                throw new RuntimeException("Course not found");
            }

            // Verificar si ya existe una inscripción para el estudiante y el curso dados
            boolean enrollmentExists = checkIfEnrollmentExists(studentId, courseId);
            if (enrollmentExists) {
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

            // Buscar el estudiante por ID
            Student student = null;
            for (Student s : studentService.findAll()) {
                if (s.getStudentId() == studentId) {
                    student = s;
                    break;
                }
            }
            if (student == null) {
                throw new RuntimeException("Student not found");
            }

            // Buscar el curso por ID
            Course course = null;
            for (Course c : courseService.findAll()) {
                if (c.getCourseId() == courseId) {
                    course = c;
                    break;
                }
            }
            if (course == null) {
                throw new RuntimeException("Course not found");
            }

            // Buscar la inscripción del estudiante en el curso
            Inscription inscriptionToRemove = null;
            for (Inscription inscription : course.getStudentsInscriptions()) {
                if (inscription.getStudent().getStudentId() == studentId) {
                    inscriptionToRemove = inscription;
                    break;
                }
            }
            if (inscriptionToRemove == null) {
                throw new RuntimeException("{\n\tEnrollment not found\n}");
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

    private boolean checkIfEnrollmentExists(int studentId, int courseId) {
        for (Inscription inscription : inscriptionService.findAll()) {
            if (inscription.getStudent().getStudentId() == studentId && inscription.getCourse().getCourseId() == courseId) {
                return true; // Si encontramos una inscripcion que coincide con el estudiante y el curso, retornamos verdadero
            }
        }
        return false; // Si no encontramos ninguna inscripcion que coincida, retornamos falso
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
