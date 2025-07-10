package com.example.massagesystem.booking;

import com.example.massagesystem.service.MassageService;
import com.example.massagesystem.service.MassageServiceRepository;
import com.example.massagesystem.user.User;
import com.example.massagesystem.user.UserRepository;
import com.example.massagesystem.shop.Shop; // Shop 임포트
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MassageServiceRepository serviceRepository;

    // 현재 로그인한 사용자의 Shop 객체를 가져오는 헬퍼 메서드
    private Shop getCurrentUserShop() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return ((User) authentication.getPrincipal()).getShop();
        }
        throw new IllegalStateException("User not authenticated or shop not available.");
    }

    public List<Booking> getAllBookings() {
        Shop currentUserShop = getCurrentUserShop();
        return bookingRepository.findAll().stream()
                .filter(booking -> booking.getShop().equals(currentUserShop))
                .collect(Collectors.toList());
    }

    public Optional<Booking> getBookingById(Long id) {
        Shop currentUserShop = getCurrentUserShop();
        return bookingRepository.findById(id)
                .filter(booking -> booking.getShop().equals(currentUserShop));
    }

    public Booking createBooking(Long userId, Long serviceId, LocalDateTime bookingTime) {
        Shop currentUserShop = getCurrentUserShop();

        User user = userRepository.findById(userId)
                .filter(u -> u.getShop().equals(currentUserShop)) // 생성하려는 유저가 현재 사장님 shop에 속하는지 확인
                .orElseThrow(() -> new RuntimeException("User not found or unauthorized"));
        MassageService service = serviceRepository.findById(serviceId)
                .filter(s -> s.getShop().equals(currentUserShop)) // 선택한 서비스가 현재 사장님 shop에 속하는지 확인
                .orElseThrow(() -> new RuntimeException("MassageService not found or unauthorized"));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setService(service);
        booking.setBookingTime(bookingTime);
        booking.setStatus("PENDING"); // Default status
        booking.setShop(currentUserShop); // 현재 로그인한 사장님의 Shop 할당

        return bookingRepository.save(booking);
    }

    public Booking updateBookingStatus(Long id, String status) {
        Shop currentUserShop = getCurrentUserShop();
        Booking booking = bookingRepository.findById(id)
                .filter(b -> b.getShop().equals(currentUserShop))
                .orElseThrow(() -> new RuntimeException("Booking not found or unauthorized"));
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        Shop currentUserShop = getCurrentUserShop();
        Booking booking = bookingRepository.findById(id)
                .filter(b -> b.getShop().equals(currentUserShop))
                .orElseThrow(() -> new RuntimeException("Booking not found or unauthorized"));
        bookingRepository.delete(booking);
    }
}
