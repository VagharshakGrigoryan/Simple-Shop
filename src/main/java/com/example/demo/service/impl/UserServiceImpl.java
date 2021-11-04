package com.example.demo.service.impl;

import com.example.demo.model.User;
import com.example.demo.repasitory.UserRepository;
import com.example.demo.security.CurrentUser;
import com.example.demo.service.MailService;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final MailService mailService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    @Override
    public void userAdd(User user, CurrentUser currentUser, Locale locale) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        try {
            sendVerificEmail(user, locale);
        } catch (MessagingException e) {
            log.error(e.getMessage());
        }
    }

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public void sendVerificEmail(User user, Locale locale) throws MessagingException {
        UUID uuid = UUID.randomUUID();
        user.setToken(uuid);
        userRepository.save(user);
        String link = "http://localhost:8080/register/verifyEmail?email=" + user.getEmail() +
                "&token=" + user.getToken();
        mailService.sendHtmlEmail(user.getEmail(), "Verify your account",
                user, link, "UserWelcomeMail", locale);
    }

    @Override
    public void verifyUser(String email, UUID token) {
        User byEmail = findByEmail(email);
        if (byEmail != null && byEmail.getToken().equals(token)) {
            byEmail.setEmailVerified(true);
            byEmail.setToken(null);
            userRepository.save(byEmail);
        }
    }

    @Override
    public Page<User> userPage(PageRequest pageRequest) {
        return userRepository.findAll(pageRequest);
    }
}