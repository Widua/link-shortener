package me.widua.linkshortener.entity;

import jakarta.validation.constraints.NotNull;

public record URLRecord(@NotNull(message = "URL cannot be null.") String url) {
}
