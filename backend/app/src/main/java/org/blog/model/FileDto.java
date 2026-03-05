package org.blog.model;

public class FileDto {
    private String fileName;
    private Long postId ;

    public FileDto(String fileName, Long postId) {
        this.fileName = fileName;
        this.postId = postId;
    }

    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getPostId() {
        return postId;
    }
    public void setPostId(Long postId) {
        this.postId = postId;
    }
}
