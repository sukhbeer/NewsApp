package com.example.gur.newsapp;


public class News {
    private String articleTitle;
    private String articleSection;
    private String articlePublicationDate;
    private String articleUrl;
    private String articleAuthor;

    News(String articleTitle, String articleSection, String articlePublicationDate,
         String articleUrl, String articleAuthor) {

        this.articleTitle = articleTitle;
        this.articleSection = articleSection;
        this.articlePublicationDate = articlePublicationDate;
        this.articleUrl = articleUrl;
        this.articleAuthor = articleAuthor;
    }

    public String getArticleTitle() {
        return articleTitle;
    }


    public String getArticleSection() {
        return articleSection;
    }

    public String getArticlePublicationDate() {
        return articlePublicationDate;
    }

    public String getArticleUrl() {
        return articleUrl;
    }

    public String getArticleAuthor() {
        return articleAuthor;
    }
}


