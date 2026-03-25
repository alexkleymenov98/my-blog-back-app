package com.yandex.backend_boot.controller;

import com.yandex.backend_boot.model.CreatePost;
import com.yandex.backend_boot.model.Pagination;
import com.yandex.backend_boot.model.Post;
import com.yandex.backend_boot.model.UpdatePost;
import com.yandex.backend_boot.service.FilesService;
import com.yandex.backend_boot.service.PostService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "*")
public class PostController {

    private final PostService service;
    private final FilesService filesService;

    public PostController(PostService service, FilesService filesService) {
        this.service = service;
        this.filesService = filesService;
    }

    @GetMapping
    public Pagination<Post> getPosts(
            @RequestParam(value = "search", required = false, defaultValue = "") String search,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "1") int pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "5") int pageSize) {

        return service.getAllPosts(search, pageNumber, pageSize);
    }

    @PostMapping
    public Post createPost(@RequestBody CreatePost post) {
        return service.createPost(post);
    }

    @GetMapping("/{postId}")
    public Post getPosts(@PathVariable(name = "postId") Long postId) {
        return service.findPostById(postId);
    }

    @PutMapping("/{postId}")
    public Post updatePost(@PathVariable(name = "postId") Long postId, @RequestBody UpdatePost body) {
        return service.updatePost(postId, body);
    }

    @DeleteMapping("/{postId}")
    public void deletePost(@PathVariable(name = "postId") Long postId) {
        service.deletePost(postId);
    }

    @PostMapping("/{postId}/likes")
    public Integer incrementLike(@PathVariable(name = "postId") Long postId) {
        return service.incrementLike(postId);
    }

    @PutMapping("/{postId}/image")
    public ResponseEntity<String> uploadPostImages( @PathVariable("postId") Long postId, @RequestParam("image") MultipartFile image) {

        try {
            filesService.uploadStorage(image, postId);
            return ResponseEntity.ok("Image uploaded successfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/{postId}/image")
    public ResponseEntity<Resource> getPostImages(@PathVariable(name = "postId") Long postId) {
        Resource file = filesService.getFromStorage(postId);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(file);

    }
}
