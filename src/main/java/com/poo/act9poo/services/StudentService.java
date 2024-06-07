package com.poo.act9poo.services;

import com.poo.act9poo.entities.Inscription;
import com.poo.act9poo.entities.Student;
import com.poo.act9poo.repositories.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

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

    public void deleteById(String studentId) {
        studentRepository.deleteById(studentId);
    }

}
