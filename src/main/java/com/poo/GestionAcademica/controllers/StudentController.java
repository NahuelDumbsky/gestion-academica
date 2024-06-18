package com.poo.GestionAcademica.controllers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.poo.GestionAcademica.entities.Course;
import com.poo.GestionAcademica.entities.Inscription;
import com.poo.GestionAcademica.entities.Student;
import com.poo.GestionAcademica.services.CourseService;
import com.poo.GestionAcademica.services.InscriptionService;
import com.poo.GestionAcademica.services.StudentService;

@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private InscriptionService inscriptionService;

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

    @DeleteMapping("/{courseId}/borrar/{studentId}")
    public String bajaEstudianteDeCurso(@PathVariable("courseId") int courseId,
                                        @PathVariable("studentId") int studentId,
                                        Model model) {
        Student estudianteaux = studentService.findById(studentId);
        Course cursoaux = courseService.findById(courseId);

        // Buscar la inscripción del estudiante en el curso
        Inscription inscriptionToRemove = inscriptionService.findInscriptionByStudentIdAndCourseId(studentId, courseId);

        // Verificar si la inscripción existe antes de intentar eliminarla
        if (inscriptionToRemove == null) {
            throw new RuntimeException("Inscription not found");
        }

        // Remover la inscripción del estudiante en el curso
        cursoaux.getStudentsInscriptions().remove(inscriptionToRemove);
        estudianteaux.getInscriptions().remove(inscriptionToRemove);

        // Eliminar la inscripción de la base de datos
        inscriptionService.deleteById(inscriptionToRemove.getInscriptionId());

        // Guardar los cambios en estudiantes y cursos
        studentService.save(estudianteaux);
        courseService.save(cursoaux);

        model.addAttribute("student", estudianteaux);
        model.addAttribute("courseId", courseId);

        // Redirigir a la página de estudiantes del curso
        return "redirect:/cursos/estudiantes/" + courseId;
    }
}
