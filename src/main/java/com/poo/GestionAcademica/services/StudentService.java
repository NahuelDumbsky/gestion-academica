package com.poo.GestionAcademica.services;

import com.poo.GestionAcademica.entities.Student;
import com.poo.GestionAcademica.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

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

    public void deleteById(int studentId) {
        studentRepository.deleteById(studentId);
    }

}
