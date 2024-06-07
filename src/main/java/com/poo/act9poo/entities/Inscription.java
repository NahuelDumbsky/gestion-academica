package com.poo.act9poo.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Inscription {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int inscriptionId;
    
    @ManyToOne
    @JsonBackReference
    private Student student;
    
    @ManyToOne
    @JsonBackReference
    private Course course;

    public Inscription() {
    }

    public Inscription(int inscriptionId, Student student, Course course) {
        this.inscriptionId = inscriptionId;
        this.student = student;
        this.course = course;
    }
    
    public int getInscriptionId() {
        return inscriptionId;
    }

    public void setInscriptionId(int inscriptionId) {
        this.inscriptionId = inscriptionId;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
    
}
