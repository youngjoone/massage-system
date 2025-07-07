package com.example.massagesystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/services")
public class MassageServiceController {

    @Autowired
    private MassageServiceService massageServiceService;

    @GetMapping
    public List<MassageService> getAllServices() {
        return massageServiceService.getAllServices();
    }

    @GetMapping("/{id}")
    public ResponseEntity<MassageService> getServiceById(@PathVariable Long id) {
        return massageServiceService.getServiceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public MassageService createService(@RequestBody MassageService massageService) {
        return massageServiceService.createService(massageService);
    }

    @PutMapping("/{id}")
    public ResponseEntity<MassageService> updateService(@PathVariable Long id, @RequestBody MassageService massageServiceDetails) {
        return ResponseEntity.ok(massageServiceService.updateService(id, massageServiceDetails));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        massageServiceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}
