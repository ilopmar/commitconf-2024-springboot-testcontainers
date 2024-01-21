package com.demo.controller;

import com.demo.MessageService;
import com.demo.repository.MessageEntity;
import com.demo.repository.MessageRepository;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/messages")
class SignMessageController {

  private final MessageService messageService;

  private final MessageRepository messageRepository;

  SignMessageController(MessageService messageService, MessageRepository messageRepository) {
    this.messageService = messageService;
    this.messageRepository = messageRepository;
  }

  @PostMapping
  public ResponseEntity<Void> signMessage(@RequestBody PlainMessage plainMessage) {
    UUID messageId = messageService.save(plainMessage.text());

    return ResponseEntity
        .accepted()
        .header(HttpHeaders.LOCATION, "/messages/" + messageId)
        .build();
  }

  @GetMapping("/{messageId}")
  public ResponseEntity<SignedMessageDTO> findSignedMessage(@PathVariable UUID messageId) {
    return messageRepository
        .findById(messageId)
        .map(message -> ResponseEntity.ok(toDto(message)))
        .orElse(ResponseEntity.notFound().build());
  }

  @GetMapping
  public List<SignedMessageDTO> findAll() {
    return messageRepository
        .findAll()
        .stream()
        .map(this::toDto)
        .toList();
  }

  private SignedMessageDTO toDto(MessageEntity message) {
    return new SignedMessageDTO(message.getId(), message.getText(), message.getSignature(), message.getSignedAt());
  }

}
