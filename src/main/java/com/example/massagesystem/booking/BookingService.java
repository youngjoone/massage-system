package com.example.massagesystem.booking;

import com.example.massagesystem.service.MassageService;
import com.example.massagesystem.user.User;
import com.example.massagesystem.user.UserRepository;
import com.example.massagesystem.service.MassageServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private MassageServiceRepository serviceRepository;

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public Optional<Booking> getBookingById(Long id) {
        return bookingRepository.findById(id);
    }

    public Booking createBooking(Long userId, Long serviceId, LocalDateTime bookingTime) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        MassageService service = serviceRepository.findById(serviceId).orElseThrow(() -> new RuntimeException("MassageService not found"));

        Booking booking = new Booking();
        booking.setUser(user);
        booking.setService(service);
        booking.setBookingTime(bookingTime);
        booking.setStatus("PENDING"); // Default status

        return bookingRepository.save(booking);
    }

    public Booking updateBookingStatus(Long id, String status) {
        Booking booking = bookingRepository.findById(id).orElseThrow(() -> new RuntimeException("Booking not found"));
        booking.setStatus(status);
        return bookingRepository.save(booking);
    }

    public void deleteBooking(Long id) {
        bookingRepository.deleteById(id);
    }
}