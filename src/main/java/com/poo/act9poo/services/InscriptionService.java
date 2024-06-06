package com.poo.act9poo.services;

import com.poo.act9poo.entities.Inscription;
import com.poo.act9poo.repositories.InscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InscriptionService {

    @Autowired
    private InscriptionRepository inscriptionRepository;

    public List<Inscription> findAll() {
        return inscriptionRepository.findAll();
    }

    public Optional<Inscription> findById(int inscriptionId) {
        return inscriptionRepository.findById(inscriptionId);
    }

    public Inscription save(Inscription inscription) {
        return inscriptionRepository.save(inscription);
    }

    public void deleteById(int inscriptionId) {
        inscriptionRepository.deleteById(inscriptionId);
    }
}
