package me.widua.linkshortener.controller;

import jakarta.validation.Valid;
import me.widua.linkshortener.entity.LinkDTO;
import me.widua.linkshortener.entity.URLRecord;
import me.widua.linkshortener.entity.ShortenLinkRecord;
import me.widua.linkshortener.exceptions.LinkNotFoundException;
import me.widua.linkshortener.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.MalformedURLException;
import java.net.URL;

@RestController
@RequestMapping("shortener/api/v1/")
public class LinkRestController {

    private final LinkService linkService;

    @Autowired
    public LinkRestController( LinkService linkService){
        this.linkService = linkService;
    }

    @ExceptionHandler(MalformedURLException.class)
    public ResponseEntity<String> malformedURL(){
        return ResponseEntity.badRequest().body("Wrong URL provided!");
    }

    @ExceptionHandler(LinkNotFoundException.class)
    public ResponseEntity<String> doesNotExist(){
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<ShortenLinkRecord> shortenLink(@RequestBody @Valid URLRecord linkToShorten) throws MalformedURLException {
        String shortenLink = linkService.shortenLink(new URL(linkToShorten.url()));
        return ResponseEntity.ok(new ShortenLinkRecord(shortenLink));
    }

    @GetMapping("/{redirectString}")
    public ResponseEntity<URLRecord> getWebsiteAddress(@PathVariable String redirectString ){
        LinkDTO dto = linkService.getWebsiteByRedirectString(redirectString);
        return ResponseEntity.ok( new URLRecord(dto.getRealUrl().toString()) );
    }
}
