package com.example.massagesystem.user;

import com.example.massagesystem.shop.Shop;
import com.example.massagesystem.shop.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ShopRepository shopRepository; // ShopRepository 주입

    // 현재 로그인한 사용자의 Shop 객체를 가져오는 헬퍼 메서드
    private Shop getCurrentUserShop() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof User) {
            return ((User) authentication.getPrincipal()).getShop();
        }
        throw new IllegalStateException("User not authenticated or shop not available.");
    }

    public List<User> getAllUsers() {
        Shop currentUserShop = getCurrentUserShop();
        return userRepository.findAll().stream()
                .filter(user -> user.getShop().equals(currentUserShop))
                .collect(Collectors.toList());
    }

    public Optional<User> getUserById(Long id) {
        Shop currentUserShop = getCurrentUserShop();
        return userRepository.findById(id)
                .filter(user -> user.getShop().equals(currentUserShop));
    }

    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public User updateUser(Long id, User userDetails) {
        Shop currentUserShop = getCurrentUserShop();
        User user = userRepository.findById(id)
                .filter(u -> u.getShop().equals(currentUserShop))
                .orElseThrow(() -> new RuntimeException("User not found or unauthorized"));

        user.setUsername(userDetails.getUsername());
        if (userDetails.getPassword() != null && !userDetails.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(userDetails.getPassword()));
        }
        user.setRole(userDetails.getRole());
        // userDetails에 shop이 포함된 경우 업데이트 (관리자용 또는 shop 변경 로직)
        if (userDetails.getShop() != null && userDetails.getShop().getId() != null) {
            Shop newShop = shopRepository.findById(userDetails.getShop().getId())
                                        .orElseThrow(() -> new RuntimeException("Shop not found"));
            user.setShop(newShop);
        }
        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        Shop currentUserShop = getCurrentUserShop();
        User user = userRepository.findById(id)
                .filter(u -> u.getShop().equals(currentUserShop))
                .orElseThrow(() -> new RuntimeException("User not found or unauthorized"));
        userRepository.delete(user);
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> validateUser(String username, String password) {
        return userRepository.findByUsername(username)
                .filter(user -> passwordEncoder.matches(password, user.getPassword()));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
    }
}
