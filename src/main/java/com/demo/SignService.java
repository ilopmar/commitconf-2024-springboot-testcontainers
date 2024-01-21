package com.demo;

import com.demo.vo.SignedMessage;
import com.demo.vo.UnsignedMessage;
import java.security.SecureRandom;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

@Service
public class SignService {

  private static final Logger LOG = LoggerFactory.getLogger(SignService.class);

  private final KafkaTemplate<String, SignedMessage> kafkaTemplate;

  public SignService(KafkaTemplate<String, SignedMessage> kafkaTemplate) {
    this.kafkaTemplate = kafkaTemplate;
  }

  @KafkaListener(groupId = "messages", topics = "messages.unsigned")
  public void handleMessage(@Payload UnsignedMessage messageToSign) throws InterruptedException {
    // Just wait random time simulating the operation takes some time to complete
    Thread.sleep(new SecureRandom().nextInt(1_500, 3_000));

    // ⚠️ Warning! Never use this in production. Use a library and use a proper and modern hashing algorithm
    String signature = DigestUtils.md5DigestAsHex(messageToSign.text().getBytes());

    LOG.debug("Message signed. [messageId={}, text={}]", messageToSign.messageId(), messageToSign.text());

    kafkaTemplate.send("messages.signed", new SignedMessage(messageToSign.messageId(), signature));
  }

}

