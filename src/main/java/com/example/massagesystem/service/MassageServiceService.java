package com.example.massagesystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MassageServiceService {

    @Autowired
    private MassageServiceRepository massageServiceRepository;

    public List<MassageService> getAllServices() {
        return massageServiceRepository.findAll();
    }

    public Optional<MassageService> getServiceById(Long id) {
        return massageServiceRepository.findById(id);
    }

    public MassageService createService(MassageService massageService) {
        return massageServiceRepository.save(massageService);
    }

    public MassageService updateService(Long id, MassageService massageServiceDetails) {
        MassageService massageService = massageServiceRepository.findById(id).orElseThrow(() -> new RuntimeException("MassageService not found"));
        massageService.setName(massageServiceDetails.getName());
        massageService.setDescription(massageServiceDetails.getDescription());
        massageService.setPrice(massageServiceDetails.getPrice());
        massageService.setDurationMinutes(massageServiceDetails.getDurationMinutes());
        return massageServiceRepository.save(massageService);
    }

    public void deleteService(Long id) {
        massageServiceRepository.deleteById(id);
    }
}
