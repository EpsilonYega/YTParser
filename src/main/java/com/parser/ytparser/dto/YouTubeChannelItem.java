package com.parser.ytparser.dto;

public class YouTubeChannelItem {
    private String id;
    private YouTubeSnippet snippet;
    private YouTubeStatistics statistics;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public YouTubeSnippet getSnippet() {
        return snippet;
    }

    public void setSnippet(YouTubeSnippet snippet) {
        this.snippet = snippet;
    }

    public YouTubeStatistics getStatistics() {
        return statistics;
    }

    public void setStatistics(YouTubeStatistics statistics) {
        this.statistics = statistics;
    }
}
