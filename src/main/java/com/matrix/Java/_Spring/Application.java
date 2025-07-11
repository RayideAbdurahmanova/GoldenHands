package com.matrix.Java._Spring;

import com.matrix.Java._Spring.model.entity.Role;
import com.matrix.Java._Spring.model.entity.User;
import com.matrix.Java._Spring.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDateTime;

import java.util.Set;


@SpringBootApplication
@RequiredArgsConstructor
@Slf4j
@EnableFeignClients(basePackages = "com.matrix.Java._Spring.client")
public class Application implements CommandLineRunner {

    private final BCryptPasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private static final String AdminEmail = "java@gmail.com";

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
        if (userRepository.findByUsername(AdminEmail).isEmpty()) {
            User user = new User();
            user.setUsername(AdminEmail);
            user.setPassword(passwordEncoder.encode("12345"));
            user.setEnabled(true);
            Role role = new Role();
            role.setName("ADMIN");
            role.setCreateDate(LocalDateTime.now());
            user.setRoles(Set.of(role));
            userRepository.save(user);
        }
    }
}
