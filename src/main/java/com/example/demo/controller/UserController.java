package com.example.demo.controller;


import com.example.demo.model.User;
import com.example.demo.repasitory.UserRepository;
import com.example.demo.service.MailService;
import com.example.demo.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.UUID;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/user")
public class UserController {

    private final UserRepository userRepository;
    private final UserServiceImpl userService;
    private final PasswordEncoder passwordEncoder;
    private final MailService mailService;

    @GetMapping
    public String users(ModelMap modelMap) {
        List<User> all = userService.findAllUsers();
        modelMap.addAttribute("users", all);
        return "user";
    }

    @GetMapping("/add")
    public String addUser() {
        return "user";
    }

    @PostMapping("/add")
    public String addUserPost(@ModelAttribute User user, Locale locale) {
        User byEmail = userService.findByEmail(user.getEmail());
        if (byEmail != null) {
            return "redirect:/users";
        }
        userService.addUser(user, locale);
        return "redirect:/user";
    }

    @GetMapping("/delete")
    public String deleteUser(@RequestParam("id") long id) {
        userRepository.deleteById(id);
        String msg = "User was removed";
        return "redirect:/?msg=" + msg;
    }

    @GetMapping("/forgotPassword")
    public String forgotPass(@RequestParam("email") String email) {
        Optional<User> byUsername = userRepository.findByEmail(email);
        if (byUsername.isPresent() && byUsername.get().isEmailVerified()) {
            User user = byUsername.get();
            UUID token = UUID.randomUUID();
            user.setToken(token);
            userRepository.save(user);
            String link = "http://localhost:8080/user/forgotPassword/reset?email="
                    + user.getUsername() + "&token=" + token;
            mailService.send(user.getEmail(), "RESET password",
                    "Dear user, please open this link in order to reset your password: " + link);
        }
        return "redirect:/";
    }

    @GetMapping("/forgotPassword/reset")
    public String forgotPassReset(ModelMap modelMap, @RequestParam("email") String email, @RequestParam("token") UUID token) {
        Optional<User> byUsername = userRepository.findByUsername(email);
        if (byUsername.isPresent() && byUsername.get().getToken().equals(token)) {
            modelMap.addAttribute("email", byUsername.get().getUsername());
            modelMap.addAttribute("token", byUsername.get().getToken());
            return "changePassword";
        }
        return "redirect:/";
    }

    @PostMapping("/forgotPassword/change")
    public String changePassword(@RequestParam("email") String email, @RequestParam("token") UUID token,
                                 @RequestParam("password") String password,
                                 @RequestParam("repeatPassword") String repeatPassword) {
        Optional<User> byUsername = userRepository.findByEmail(email);
        if (byUsername.isPresent()) {
            User user = byUsername.get();
            if (user.getToken().equals(token) && password.equals(repeatPassword)) {
                user.setToken(token);
                user.setPassword(passwordEncoder.encode(password));
                userRepository.save(user);
                return "redirect:/?msg=Your password changed!";
            }
        }
        return "redirect:/";
    }
}
