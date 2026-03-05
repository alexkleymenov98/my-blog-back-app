package org.blog.model;

public class UpdatePost {
    private Long id;
    private String title;
    private String text;
    private String[] tags;

    public UpdatePost() {}

    public UpdatePost(Long id, String title, String text, String[] tags) {
        this.id = id;
        this.title = title;
        this.text = text;
        this.tags = tags;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String[] getTags() {
        return tags;
    }

    public void setTags(String[] tags) {
        this.tags = tags;
    }
}
