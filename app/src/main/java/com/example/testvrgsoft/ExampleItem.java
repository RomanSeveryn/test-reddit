package com.example.testvrgsoft;

public class ExampleItem {
    private String imageViewUrl;
    private String nameAuthor;
    private int comment;

    public ExampleItem(String imageViewUrl, String nameAuthor, int comment) {
        this.imageViewUrl = imageViewUrl;
        this.nameAuthor = nameAuthor;
        this.comment = comment;
    }

    public String getImageViewUrl() {
        return imageViewUrl;
    }

    public String getNameAuthor() {
        return nameAuthor;
    }

    public int getComment() {
        return comment;
    }
}
