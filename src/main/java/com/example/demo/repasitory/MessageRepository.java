package com.example.demo.repasitory;

import com.example.demo.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository <Message, Long> {

        List<Message> findByToUser_Id(Long id);
}