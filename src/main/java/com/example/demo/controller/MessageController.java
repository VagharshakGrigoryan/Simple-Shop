package com.example.demo.controller;


import com.example.demo.model.Message;
import com.example.demo.model.User;
import com.example.demo.security.CurrentUser;
import com.example.demo.service.MessageService;
import com.example.demo.service.impl.UserServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MessageController {


    private final UserServiceImpl userService;
    private final MessageService messageService;

    @GetMapping("/sendMessage")
    public String getAllEmployees(ModelMap modelMap) {
        List<User> all = userService.findAllUsers();
        modelMap.addAttribute("userID", all);
        return "messages";
    }

    @GetMapping("/allMessages")
    public String getAllMessages(ModelMap modelMap, @AuthenticationPrincipal CurrentUser currentUser) {
        List<Message> all = messageService.findAllMessagesByToId(currentUser.getUser().getId());
        modelMap.addAttribute("messages", all);
        return "showMessages";
    }

    @PostMapping("/sendMessage")
    public String sendMessage(@ModelAttribute Message message, @AuthenticationPrincipal CurrentUser currentUser) {
        message.setFromUser(currentUser.getUser());
        messageService.saveMessage(message);
        return "redirect:/allMessages";
    }

}

