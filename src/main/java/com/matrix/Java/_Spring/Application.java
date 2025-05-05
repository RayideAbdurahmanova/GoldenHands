package com.matrix.Java._Spring;

import com.matrix.Java._Spring.jwt.JwtService;
import com.matrix.Java._Spring.model.entity.security.Authority;
import com.matrix.Java._Spring.model.entity.security.User;
import com.matrix.Java._Spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
public class Application implements CommandLineRunner {

	private final BCryptPasswordEncoder passwordEncoder;
	private final UserRepository userRepository;

	private final JwtService jwtService;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}



	@Override
	public void run(String... args) throws Exception {

		String name="zeyneb";

		// Əvvəlcə istifadəçinin mövcudluğunu yoxlayın
		if (!userRepository.findByUsername(name).isPresent()) {
			User user = new User();
			user.setUsername(name);
			user.setPassword(passwordEncoder.encode("12345"));
			user.setCredentialsNonExpired(true);
			user.setAccountNonExpired(true);
			user.setEnabled(true);
			user.setAccountNonLocked(true);

			Authority authority1 = new Authority();
			authority1.setAuthority("ADMIN");
			authority1.setUser(user);

			Authority authority2 = new Authority();
			authority2.setAuthority("USER");
			authority2.setUser(user);

			user.setAuthorities(Set.of(authority1, authority2));

			userRepository.save(user);
			log.info("User " + name + "created successfully.");
		}

		User user = userRepository.findByUsername(name).orElseThrow(() -> new RuntimeException("User not found"));
		String token = jwtService.issueToken(user);
		user.setIssueToken(token);
		userRepository.save(user);

		log.info("JWT: {}", token);
		log.info("Token verification: {}", jwtService.verifyToken(token));
	}



//	@Override
//	public void run(String... args) throws Exception {
//
//
//
//		User user=new User();
//		user.setUsername("benove");
//		user.setPassword(passwordEncoder.encode("12345"));
//		user.setCredentialsNonExpired(true);
//		user.setAccountNonExpired(true);
//		user.setEnabled(true);
//		user.setAccountNonLocked(true);
//
//		Authority authority1=new Authority();
//		authority1.setAuthority("ADMIN");
//		authority1.setUser(user);
//
//		Authority authority2=new Authority();
//		authority2.setAuthority("USER");
//		authority2.setUser(user);
//
//		user.setAuthorities(Set.of(authority2,authority1));
//
//		User userEntity=userRepository.save(user);
//
//		User user =userRepository.findByUsername("benove").get();
//		String token=jwtService.issueToken(user);
//		user.setIssueToken(token);
//		userRepository.save(user);
////
//		System.out.println("JWT:"+token);
//		System.out.println(jwtService.verifyToken(token));
////

//	}
}
