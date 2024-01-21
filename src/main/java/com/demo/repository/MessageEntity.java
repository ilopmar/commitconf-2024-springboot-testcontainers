package com.demo.repository;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import org.springframework.lang.Nullable;

@Entity
@Table(name = "message")
public class MessageEntity {

  @Id
  private UUID id;

  private String text;

  private Instant createdAt = Instant.now();

  @Nullable
  private String signature;

  @Nullable
  private Instant signedAt;

  public MessageEntity() {
  }

  public MessageEntity(UUID id, String text) {
    this.id = id;
    this.text = text;
  }

  public UUID getId() {
    return id;
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  @Nullable
  public String getSignature() {
    return signature;
  }

  public void setSignature(@Nullable String signature) {
    this.signature = signature;
  }

  public Instant getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(Instant createdAt) {
    this.createdAt = createdAt;
  }

  @Nullable
  public Instant getSignedAt() {
    return signedAt;
  }

  public void setSignedAt(@Nullable Instant signedAt) {
    this.signedAt = signedAt;
  }
}
