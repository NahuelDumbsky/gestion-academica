package com.poo.GestionAcademica.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.poo.GestionAcademica.entities.Student;
import com.poo.GestionAcademica.services.StudentService;

@Controller
public class StudentCRUDController {

    @Autowired
    private StudentService studentService;

    @GetMapping({"/estudiantes"})
    public String listarEstudiantes(Model model) {
        model.addAttribute("students", studentService.findAll());
        return "students"; // Retorna al archivo index.html
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
}