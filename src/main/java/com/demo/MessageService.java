package com.demo;

import com.demo.repository.MessageEntity;
import com.demo.repository.MessageRepository;
import com.demo.vo.SignedMessage;
import com.demo.vo.UnsignedMessage;
import java.time.Instant;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

  private static final Logger LOG = LoggerFactory.getLogger(MessageService.class);

  private final MessageRepository messageRepository;

  private final KafkaTemplate<String, UnsignedMessage> kafkaTemplate;

  public MessageService(MessageRepository messageRepository, KafkaTemplate<String, UnsignedMessage> kafkaTemplate) {
    this.messageRepository = messageRepository;
    this.kafkaTemplate = kafkaTemplate;
  }

  public UUID save(String text) {
    MessageEntity messageEntity = messageRepository.save(new MessageEntity(UUID.randomUUID(), text));

    LOG.debug("Requesting to sign a message. [messageId={}, text={}]", messageEntity.getId(), messageEntity.getText());

    kafkaTemplate.send("messages.unsigned", new UnsignedMessage(messageEntity.getId(), messageEntity.getText()));

    return messageEntity.getId();
  }

  @KafkaListener(groupId = "messages", topics = "messages.signed")
  public void handleMessage(@Payload SignedMessage signedMessage) {
    messageRepository
        .findById(signedMessage.messageId())
        .ifPresent(messageEntity -> {
          messageEntity.setSignature(signedMessage.signature());
          messageEntity.setSignedAt(Instant.now());
          messageRepository.save(messageEntity);
        });
  }

}

