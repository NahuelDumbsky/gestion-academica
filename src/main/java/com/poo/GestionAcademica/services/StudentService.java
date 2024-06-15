package com.poo.GestionAcademica.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poo.GestionAcademica.entities.Student;
import com.poo.GestionAcademica.repositories.StudentRepository;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public List<Student> findAll() {
        return studentRepository.findAll();
    }

    public Student save(Student student) {
        return studentRepository.save(student);
    }

    public Student findById(int studentId) {
        return studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found "));
    }

    public Student updateStudent(Student student) {
        return studentRepository.save(student);
    }

    public void deleteById(int studentId) {
        studentRepository.deleteById(studentId);
    }

}
