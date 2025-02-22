package com.project.marathon.controller;

import com.project.marathon.dto.MessageResponse;
import com.project.marathon.entity.Message;
import com.project.marathon.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/messages")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @Operation(summary = "모든 메시지 조회", description = "저장된 모든 메시지를 조회합니다.")
    @GetMapping
    public ResponseEntity<List<MessageResponse>> getMessages() {
        List<MessageResponse> messages = messageService.getAllMessages();
        return ResponseEntity.ok(messages);
    }

    @Operation(summary = "새 메시지 저장", description = "새로운 메시지를 저장합니다.")
    @PostMapping
    public ResponseEntity<String> createMessage(@RequestBody Message message) {
        messageService.addMessage(message.getMessage());
        return ResponseEntity.ok("메시지가 저장되었습니다.");
    }

    @Operation(summary = "메시지 삭제", description = "ID를 기반으로 메시지를 삭제합니다.")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteMessage(@PathVariable Long id) {
        messageService.deleteMessage(id);
        return ResponseEntity.ok("메시지가 삭제되었습니다.");
    }
}
