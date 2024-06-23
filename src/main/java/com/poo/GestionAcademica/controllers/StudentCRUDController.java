package com.poo.GestionAcademica.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.poo.GestionAcademica.APILOGS.LOGSController;
import com.poo.GestionAcademica.entities.Course;
import com.poo.GestionAcademica.entities.Inscription;
import com.poo.GestionAcademica.entities.Student;
import com.poo.GestionAcademica.services.CourseService;
import com.poo.GestionAcademica.services.InscriptionService;
import com.poo.GestionAcademica.services.StudentService;


@Controller
public class StudentCRUDController {

    @Autowired
    private StudentService studentService;

    @Autowired
    private CourseService courseService;

    @Autowired
    private InscriptionService inscriptionService;

    @GetMapping({ "/estudiantes" })
    public String listarEstudiantes(Model model) {
        model.addAttribute("students", studentService.findAll());
        return "students";
    }

    @GetMapping("/estudiantes/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("student", new Student());
        return "CreateStudent"; // Retorna al archivo crear_estudiante.html
    }

    @PostMapping("/estudiantes")
    public String guardarEstudiante(@ModelAttribute("student") Student student) {
        studentService.save(student);
        return "redirect:/estudiantes"; // Redirige a la lista de estudiantes
    }

    @GetMapping("/estudiantes/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") int id, Model model) {
        Student student = studentService.findById(id);
        model.addAttribute("student", student);
        return "EditStudent"; // Retorna al archivo EditStudent.html
    }

    @PostMapping("/estudiantes/{id}")
    public String actualizarEstudiante(@PathVariable("id") int id, @ModelAttribute("student") Student student) {
        student.setStudentId(id); // Aseguramos que el ID del estudiante sea el correcto
        studentService.save(student); // Guarda o actualiza al estudiante
        return "redirect:/estudiantes"; // Redirige a la lista de estudiantes
    }

    @GetMapping("/estudiantes/eliminar/{id}")
    public String eliminarEstudiante(@PathVariable("id") int id) {
        studentService.deleteById(id);
        return "redirect:/estudiantes"; // Redirige a la lista de estudiantes
    }

    // Mostrar los cursos del estudiante y los cursos disponibles
    @GetMapping("estudiantes/cursos/{id}")
    public String misCursos(@PathVariable("id") int id, Model model) {

        // Obtener el estudiante por su ID
        Student student = studentService.findById(id);
        // Obtener todos los cursos disponibles
        List<Course> studentCourses = student.getStudentCourses();

        // Filtrar los cursos disponibles para los que el estudiante no está inscrito
        List<Course> availableCourses = courseService.findAll().stream()
                .filter(course -> !student.getStudentCourses().contains(course))
                .collect(Collectors.toList());

        // Agregar los cursos del estudiante y los cursos disponibles al modelo
        model.addAttribute("courses", studentCourses);
        model.addAttribute("availableCourses", availableCourses);
        model.addAttribute("studentId", id);

        return "StudentCourses";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("estudiantes/cursos/{studentId}/inscribir/{courseId}")
    public String inscribirMisCursos(@PathVariable("studentId") int studentId, @PathVariable("courseId") int courseId,
            Model model) {

        Student auxStudent = studentService.findById(studentId);
        Course auxCourse = courseService.findById(courseId);

        // Crear la inscripción
        Inscription inscription = new Inscription();
        inscription.setStudent(auxStudent);
        inscription.setCourse(auxCourse);

        // Guardar la inscripción
        inscriptionService.save(inscription);

        // Obtener todos los cursos disponibles
        List<Course> studentCourses = auxStudent.getStudentCourses();

        // Filtrar los cursos disponibles para los que el estudiante no está inscrito
        List<Course> availableCourses = courseService.findAll().stream()
                .filter(course -> !auxStudent.getStudentCourses().contains(course))
                .collect(Collectors.toList());

        // Agregar los cursos del estudiante y los cursos disponibles al modelo
        model.addAttribute("courses", studentCourses);
        model.addAttribute("availableCourses", availableCourses);

        // actualizar tabla de estudiantes
        model.addAttribute("studentId", studentId);

        return "StudentCourses";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("estudiantes/cursos/{studentId}/darDeBaja/{courseId}")
    public String desuscribirMisCursos(@PathVariable("studentId") int studentId, @PathVariable("courseId") int courseId,
            Model model) {
        Student auxStudent = studentService.findById(studentId);
        Course auxCourse = courseService.findById(courseId);

        // Buscar la inscripción del estudiante en el curso
        Inscription inscriptionToRemove = inscriptionService.findInscriptionByStudentIdAndCourseId(studentId, courseId);

        // Remover la inscripción del estudiante en el curso
        auxCourse.getStudentsInscriptions().remove(inscriptionToRemove);
        auxStudent.getInscriptions().remove(inscriptionToRemove);

        // Eliminar la inscripción de la base de datos
        inscriptionService.deleteById(inscriptionToRemove.getInscriptionId());

        // Guardar los cambios en estudiantes y cursos
        studentService.save(auxStudent);
        courseService.save(auxCourse);

        // Obtener todos los cursos disponibles
        List<Course> studentCourses = auxStudent.getStudentCourses();

        // Filtrar los cursos disponibles para los que el estudiante no está inscrito
        List<Course> availableCourses = courseService.findAll().stream()
                .filter(course -> !auxStudent.getStudentCourses().contains(course))
                .collect(Collectors.toList());

        // Agregar los cursos del estudiante y los cursos disponibles al modelo
        model.addAttribute("courses", studentCourses);
        model.addAttribute("availableCourses", availableCourses);

        // actualizar tabla de estudiantes
        model.addAttribute("studentId", studentId);

        return "StudentCourses";
    }

    @PostMapping("/estudiantes/actualizarLogs")
    public String actualizarLogsEstudiantes(@RequestBody String entity) {
        LOGSController logsController = new LOGSController();
        logsController.findLogs(studentService.findAll());
        return "students";
    }
}