package com.example.demo.util;


import com.example.demo.model.User;
import com.example.demo.repasitory.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OnApplicationStartEvent implements ApplicationListener<ApplicationReadyEvent> {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        if (userRepository.findByEmail("admin@mail.com").isEmpty()) {
            userRepository.save(User.builder()
                    .firstName("admin")
                    .lastName("admin")
                    .email("admin@mail.com")
                    .password(passwordEncoder.encode("admin"))
                    .role(User.Role.ADMIN)
                    .username("admin")
                    .build());
        }
    }
}