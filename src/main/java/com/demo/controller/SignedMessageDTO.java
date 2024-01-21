package com.demo.controller;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.time.Instant;
import java.util.UUID;
import org.springframework.lang.Nullable;

public record SignedMessageDTO(
    @JsonProperty("message_id")
    UUID messageId,

    @JsonProperty("message")
    String message,

    @JsonProperty("signature")
    @Nullable
    String signature,

    @JsonProperty("signed_at")
    @Nullable
    Instant signedAt) {

}
