package com.parser.ytparser.dto;

public class YouTubeSearchItem {
    private YouTubeId id;
    private YouTubeSnippet snippet;

    public YouTubeId getId() {
        return id;
    }

    public void setId(YouTubeId id) {
        this.id = id;
    }

    public YouTubeSnippet getSnippet() {
        return snippet;
    }

    public void setSnippet(YouTubeSnippet snippet) {
        this.snippet = snippet;
    }
}
