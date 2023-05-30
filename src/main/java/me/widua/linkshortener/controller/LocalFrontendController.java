package me.widua.linkshortener.controller;

import me.widua.linkshortener.entity.LinkDTO;
import me.widua.linkshortener.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

@Controller
@RequestMapping("/")
public class LocalFrontendController {

    private final LinkService linkService;

    @Autowired
    public LocalFrontendController(LinkService linkService) {
        this.linkService = linkService;
    }


    @GetMapping("/{redirectString}")
    public RedirectView redirectToShortenString(@PathVariable String redirectString) {
        RedirectView redirectView = new RedirectView();
        LinkDTO link = linkService.getWebsiteByRedirectString(redirectString);
        redirectView.setUrl(link.getRealUrl().toString());
        return redirectView;
    }

}
