package com.project.marathon.service;

import com.project.marathon.repository.MessageRepository;
import com.project.marathon.entity.Message;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MessageService {
    private final MessageRepository messageRepository;

    public MessageService(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // 모든 메시지 조회
    public List<Message> getAllMessages() {
        return messageRepository.findAll();
    }

    // 메시지 저장 (중복된 메시지는 저장하지 않도록 처리)
    public void addMessage(String content) {
        Optional<Message> existingMessage = messageRepository.findByContent(content);
        if (existingMessage.isPresent()) {
            throw new IllegalArgumentException("이미 존재하는 메시지입니다.");
        }
        Message message = new Message(content);
        messageRepository.insert(message);
    }

    // 메시지 삭제 (존재하지 않는 메시지는 예외 처리)
    public void deleteMessage(Long id) {
        Optional<Message> message = messageRepository.findById(id);
        if (message.isEmpty()) {
            throw new IllegalArgumentException("존재하지 않는 메시지입니다.");
        }
        messageRepository.delete(id);
    }
}
