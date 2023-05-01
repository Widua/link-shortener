package me.widua.linkshortener.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.net.URL;

@Getter
@Setter
@AllArgsConstructor
public class LinkDTO {
    @JsonProperty("redirectString")
    private String redirectString;
    @JsonProperty("realURL")
    private URL realUrl;
}
