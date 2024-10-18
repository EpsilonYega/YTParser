package com.parser.ytparser.controller;

import com.parser.ytparser.model.ChannelInfo;
import com.parser.ytparser.service.YouTubeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.List;

@RestController
@Slf4j
public class ApiController {
    private final YouTubeService youTubeService;
    @Autowired
    public ApiController(YouTubeService youTubeService) {
        this.youTubeService = youTubeService;
    }
    @GetMapping("/get")
    public ResponseEntity<List<ChannelInfo>> getInfo(@RequestParam String query, @RequestParam BigDecimal minSubscribers) {
        return ResponseEntity.ok(youTubeService.searchChannels(query, minSubscribers));
    }
}
