package com.demo.vo;

import java.util.UUID;

public record UnsignedMessage(UUID messageId, String text) {

}
