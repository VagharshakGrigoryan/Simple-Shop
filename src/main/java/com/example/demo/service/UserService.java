package com.example.demo.service;


import com.example.demo.model.User;
import com.example.demo.security.CurrentUser;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Service
public interface UserService {
    Optional<User> findByname(String username);

    User findByEmail(String email);

    void userAdd(User user, CurrentUser currentUser, Locale locale);

    void sendVerificEmail(User user, Locale locale) throws MessagingException;

    void verifyUser(String email, UUID token);

    List<User> findAllUsers();

    void addUser(User user, Locale locale);

}
