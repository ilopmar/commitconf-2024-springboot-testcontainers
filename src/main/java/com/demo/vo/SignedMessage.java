package com.demo.vo;

import java.util.UUID;

public record SignedMessage(UUID messageId, String signature) {

}
