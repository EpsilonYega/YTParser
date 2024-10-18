package com.parser.ytparser.dto;

import lombok.Data;

import java.util.List;

@Data
public class YouTubeSearchResponse {
    private List<YouTubeSearchItem> items;
}
