package com.poo.GestionAcademica.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.poo.GestionAcademica.entities.Course;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.poo.GestionAcademica.entities.Student;
import com.poo.GestionAcademica.entities.Inscription;
import com.poo.GestionAcademica.services.CourseService;

@Controller
public class CourseCRUDController {

    @Autowired
    private CourseService courseService;

    @GetMapping({"/cursos"})
    public String listarEstudiantes(Model model) {
        model.addAttribute("courses", courseService.findAll());
        return "courses"; // Retorna al archivo index.html
    }

    @GetMapping("/cursos/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("course", new Course());
        return "CreateCourse"; // Retorna al archivo crear_estudiante.html
    }

    @PostMapping("/cursos")
    public String guardarEstudiante(@ModelAttribute("course") Course course) {
        courseService.save(course);
        return "redirect:/cursos"; // Redirige a la lista de cursos
    }

    @GetMapping("/cursos/editar/{id}")
    public String mostrarFormularioEdicion(@PathVariable("id") int id, Model model) {
        Course course = courseService.findById(id);
        model.addAttribute("course", course);
        return "EditCourse"; // Retorna al archivo EditCourse.html
    }

    @PostMapping("/cursos/{id}")
    public String actualizarEstudiante(@PathVariable("id") int id, @ModelAttribute("course") Course course) {
        course.setCourseId(id); // Aseguramos que el ID del estudiante sea el correcto
        courseService.save(course); // Guarda o actualiza al estudiante
        return "redirect:/cursos"; // Redirige a la lista de cursos
    }

    @GetMapping("/cursos/eliminar/{id}")
    public String eliminarEstudiante(@PathVariable("id") int id) {
        courseService.deleteById(id);
        return "redirect:/cursos"; // Redirige a la lista de estudiantes
    }
    
    // @GetMapping("/cursos/estudiantes/{id}")
    // public String listarEstudiante(@PathVariable("id") int id, Model model) {
    //     Course course = courseService.findById(id);
    //     // Lista de inscripciones
    //     List<Inscription> inscriptions = course.getStudentsInscriptions();
    //     //Buscar sus estudiantes
    //     List<Student> students = new ArrayList<>();
    //     for (Inscription inscription : inscriptions) {
    //         students.add(inscription.getStudent());
    //     }
    //     model.addAttribute("students", students);
    //     model.addAttribute("courseId", course);
    //     return "SeeStudentsCourses"; // Redirige a la lista de estudiantes
    // }
    @GetMapping("/cursos/estudiantes/{id}")
    public String listarEstudiante(@PathVariable("id") int id, Model model) {
        Course course = courseService.findById(id);
        // Lista de inscripciones
        List<Inscription> inscriptions = course.getStudentsInscriptions();
        //Buscar sus estudiantes
        List<Student> students = new ArrayList<>();
    
        for (Inscription inscription : inscriptions) {
            students.add(inscription.getStudent());
        }
    
        // Agregar curso al modelo
        model.addAttribute("course", course);
        // Agregar estudiantes al modelo
        model.addAttribute("students", students);
    
        return "SeeStudentsCourses"; // Redirige a la lista de estudiantes
    }
}