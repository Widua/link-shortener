package me.widua.linkshortener.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.time.LocalDateTime;

@RedisHash("shorten-link-to-string")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LinkModel {
    @Id
    private String redirectString;
    private String realUrl;
    private LocalDateTime lastTimeUsed;
}
