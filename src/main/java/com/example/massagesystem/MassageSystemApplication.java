package com.example.massagesystem;

import com.example.massagesystem.announcement.Announcement;
import com.example.massagesystem.announcement.AnnouncementRepository;
import com.example.massagesystem.shop.Shop;
import com.example.massagesystem.shop.ShopRepository;
import com.example.massagesystem.user.User;
import com.example.massagesystem.user.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.time.LocalDateTime;

@SpringBootApplication
public class MassageSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(MassageSystemApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/**")
						.allowedOrigins("http://localhost:3000") // React app's origin
						.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
						.allowedHeaders("*")
						.allowCredentials(true);
			}
		};
	}

	@Bean
	public CommandLineRunner demoData(ShopRepository shopRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, AnnouncementRepository announcementRepository) {
		return args -> {
			// Shop 데이터 생성
			Shop shopA = new Shop(null, "마사지샵 A", "서울시 강남구", "02-1111-2222");
			Shop shopB = new Shop(null, "마사지샵 B", "서울시 서초구", "02-3333-4444");
			shopRepository.save(shopA);
			shopRepository.save(shopB);

			// 사용자 데이터 생성 (사장님)
			User adminA = new User(null, "adminA", passwordEncoder.encode("password"), "ADMIN", shopA);
			User adminB = new User(null, "adminB", passwordEncoder.encode("password"), "ADMIN", shopB);
			userRepository.save(adminA);
			userRepository.save(adminB);

			// 사용자 데이터 생성 (손님)
			User userA1 = new User(null, "userA1", passwordEncoder.encode("password"), "USER", shopA);
			User userA2 = new User(null, "userA2", passwordEncoder.encode("password"), "USER", shopA);
			User userB1 = new User(null, "userB1", passwordEncoder.encode("password"), "USER", shopB);
			userRepository.save(userA1);
			userRepository.save(userA2);
			userRepository.save(userB1);

			// 공지사항 데이터 생성
			Announcement announcement1 = new Announcement(null, "시스템 점검 안내", "안녕하세요. 시스템 안정화를 위한 점검이 예정되어 있습니다. 서비스 이용에 참고 부탁드립니다.", LocalDateTime.now());
			Announcement announcement2 = new Announcement(null, "새로운 기능 추가", "고객 관리 편의를 위한 새로운 기능이 추가되었습니다. 많은 이용 바랍니다.", LocalDateTime.now().plusDays(1));
			announcementRepository.save(announcement1);
			announcementRepository.save(announcement2);
		};
	}

}