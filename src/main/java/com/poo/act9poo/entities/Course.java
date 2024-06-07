package com.poo.act9poo.entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Course {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int courseId;
    private String courseName;
    private String description;
    
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Inscription> inscriptions;

    public Course() {
    }

    public Course(int courseId, String courseName, String description) {
        this.courseId = courseId;
        this.courseName = courseName;
        this.description = description;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Inscription> getStudentsInscriptions() {
        return inscriptions;
    }

    public void setStudentsInscriptions(List<Inscription> inscriptions) {
        this.inscriptions = inscriptions;
    }
}
