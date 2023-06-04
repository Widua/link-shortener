package me.widua.linkshortener.service;

import me.widua.linkshortener.configuration.ShortenerConfig;
import me.widua.linkshortener.entity.LinkDTO;
import me.widua.linkshortener.entity.LinkModel;
import me.widua.linkshortener.exceptions.LinkNotFoundException;
import me.widua.linkshortener.mapper.LinksMapper;
import me.widua.linkshortener.repository.LinkRepository;
import org.springframework.stereotype.Service;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.Random;

@Service
public class SimpleLinkServiceImpl implements LinkService {

    private final LinksMapper mapper;
    private final LinkRepository repository;
    private final ShortenerConfig config;
    private final Random randomizer;

    public SimpleLinkServiceImpl(LinksMapper mapper, LinkRepository repository, ShortenerConfig config, Random randomizer) {
        this.mapper = mapper;
        this.repository = repository;
        this.config = config;
        this.randomizer = randomizer;
    }

    @Override
    public LinkDTO getWebsiteByRedirectString(String redirectString) {
        LinkModel link = repository.findById(redirectString).orElseThrow(LinkNotFoundException::new);
        link.setLastTimeUsed(LocalDateTime.now());
        repository.save(link);
        return mapper.linkModelToLinkDTO(link);
    }

    @Override
    public String shortenLink(URL realURL) {
        String newRedirectString;

        do {
            newRedirectString = generateRandomString(config.getRandomStringSize());
        } while (repository.existsById(newRedirectString));

        repository.save(new LinkModel(newRedirectString, realURL.toString(), LocalDateTime.now()));
        return newRedirectString;
    }

    private String generateRandomString(Integer size) {
        final String characters = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder randomStringBuilder = new StringBuilder();
        for (int i = 0; i < size; i++) {
            int randomIndex = randomizer.nextInt(characters.length());
            randomStringBuilder.append(characters.charAt(randomIndex));
        }
        return randomStringBuilder.toString();
    }
}
