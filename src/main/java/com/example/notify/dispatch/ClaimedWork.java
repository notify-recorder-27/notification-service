package com.example.notify.dispatch;

import com.example.notify.channel.Channel;

import java.util.UUID;

public record ClaimedWork(UUID notificationId, UUID tenantId, Channel channel) {
}
