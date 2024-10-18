package com.parser.ytparser.dto;

import lombok.Data;

import java.util.List;

@Data
public class YouTubeChannelResponse {
    private List<YouTubeChannelItem> items;
}
