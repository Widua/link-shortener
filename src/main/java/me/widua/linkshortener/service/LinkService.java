package me.widua.linkshortener.service;

import me.widua.linkshortener.entity.LinkDTO;
import org.springframework.stereotype.Service;

import java.net.URL;

@Service
public interface LinkService {
    LinkDTO getWebsiteByRedirectString(String redirectString);

    String shortenLink(URL realURL );
}
