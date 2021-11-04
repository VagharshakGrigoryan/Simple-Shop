package com.example.demo.service;


import com.example.demo.model.Message;
import com.example.demo.repasitory.MessageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final MessageRepository messageRepository;

    public void saveMessage(Message message) {
        messageRepository.save(message);
    }

    public List<Message> findAllMessagesByToId(Long id) {
        return messageRepository.findByToUser_Id(id);
    }


    public void deleteMessage(Long id) {
        messageRepository.deleteById(id);
    }
}
