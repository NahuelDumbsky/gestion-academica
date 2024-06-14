package com.poo.GestionAcademica.services;

import com.poo.GestionAcademica.entities.Inscription;
import com.poo.GestionAcademica.repositories.InscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InscriptionService {

    @Autowired
    private InscriptionRepository inscriptionRepository;

    public List<Inscription> findAll() {
        return inscriptionRepository.findAll();
    }

    public Inscription findById(int inscriptionId) {
        return inscriptionRepository.findById(inscriptionId).orElseThrow(null);
    }

    public Inscription findInscriptionByStudentIdAndCourseId(int studentId, int courseId) {
        List<Inscription> inscriptions = inscriptionRepository.findAll();
        return inscriptions.stream()
                .filter(inscription -> 
                    inscription.getStudent().getStudentId() == studentId && 
                    inscription.getCourse().getCourseId() == courseId)
                .findFirst()
                .orElse(null);
    }

    public Inscription save(Inscription inscription) {
        return inscriptionRepository.save(inscription);
    }

    public void deleteById(int inscriptionId) {
        inscriptionRepository.deleteById(inscriptionId);
    }
}
