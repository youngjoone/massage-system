package com.example.massagesystem.service;

import com.example.massagesystem.user.User;
import com.example.massagesystem.shop.Shop; // Shop 임포트
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MassageServiceService {

    @Autowired
    private MassageServiceRepository massageServiceRepository;

    // 현재 로그인한 사용자의 Shop 객체를 가져오는 헬퍼 메서드
    private Shop getCurrentUserShop() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return ((User) authentication.getPrincipal()).getShop();
        }
        throw new IllegalStateException("User not authenticated or shop not available.");
    }

    public List<MassageService> getAllServices() {
        Shop currentUserShop = getCurrentUserShop();
        return massageServiceRepository.findAll().stream()
                .filter(service -> service.getShop().equals(currentUserShop))
                .collect(Collectors.toList());
    }

    public Optional<MassageService> getServiceById(Long id) {
        Shop currentUserShop = getCurrentUserShop();
        return massageServiceRepository.findById(id)
                .filter(service -> service.getShop().equals(currentUserShop));
    }

    public MassageService createService(MassageService massageService) {
        massageService.setShop(getCurrentUserShop()); // 현재 로그인한 사장님의 Shop 할당
        return massageServiceRepository.save(massageService);
    }

    public MassageService updateService(Long id, MassageService massageServiceDetails) {
        Shop currentUserShop = getCurrentUserShop();
        MassageService massageService = massageServiceRepository.findById(id)
                .filter(s -> s.getShop().equals(currentUserShop))
                .orElseThrow(() -> new RuntimeException("MassageService not found or unauthorized"));

        massageService.setName(massageServiceDetails.getName());
        massageService.setDescription(massageServiceDetails.getDescription());
        massageService.setPrice(massageServiceDetails.getPrice());
        massageService.setDurationMinutes(massageServiceDetails.getDurationMinutes());
        massageService.setShop(massageServiceDetails.getShop()); // shop도 업데이트 가능하도록 (관리자용)
        return massageServiceRepository.save(massageService);
    }

    public void deleteService(Long id) {
        Shop currentUserShop = getCurrentUserShop();
        MassageService massageService = massageServiceRepository.findByIdAndDelFlagFalse(id)
                .filter(s -> s.getShop().equals(currentUserShop))
                .orElseThrow(() -> new RuntimeException("MassageService not found or already deleted"));
        massageService.setDelFlag(true);
        massageServiceRepository.save(massageService);
    }
}
