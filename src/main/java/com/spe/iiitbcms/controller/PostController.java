package com.spe.iiitbcms.controller;

import com.spe.iiitbcms.dto.PostRequest;
import com.spe.iiitbcms.dto.PostResponse;
import com.spe.iiitbcms.model.Post;
import com.spe.iiitbcms.service.PostService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.ResponseEntity.status;

@RestController
@RequestMapping("/api/posts/")
@AllArgsConstructor
public class PostController {

    private static final Logger logger = LogManager.getLogger(PostController.class);
    private final PostService postService;

    @PostMapping
    public ResponseEntity<Void> createPost(@RequestBody PostRequest postRequest) {
        HttpStatus stat;
        try {
            System.out.println("subpost name is" + postRequest.getSubpostName());
            postService.save(postRequest);
            stat = HttpStatus.CREATED;
            logger.info("Successfully created post");
        } catch (Exception e){
            System.out.println("Exception is " + e);
            logger.error("Error in creating post");
            stat = HttpStatus.EXPECTATION_FAILED;
        }
        return new ResponseEntity<>(stat);
    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts() {
        HttpStatus stat;
        List<Post> psts = new ArrayList<>();
        try {
            stat = HttpStatus.OK;
            psts = postService.getAllPosts();
            logger.info("Successfully fetched posts");
        } catch (Exception e){
            stat = HttpStatus.EXPECTATION_FAILED;
            logger.error("Could not fetch post(s)");
        }
        return status(stat).body(psts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        HttpStatus stat;
        PostResponse pst = new PostResponse();
        try {
            pst = postService.getPost(id);
            stat = HttpStatus.OK;
            logger.info("Successfully fetched post with id " + id);
        } catch (Exception e) {
            stat = HttpStatus.EXPECTATION_FAILED;
            logger.error("Could not fetch post with id " + id);
        }

        return status(stat).body(pst);
    }

    @GetMapping("by-subpost/{id}")
    public ResponseEntity<List<PostResponse>> getPostsBySubpost(Long id) {
        HttpStatus stat;
        List<PostResponse> pst = new ArrayList<>();
        try {
            pst = postService.getPostsBySubpost(id);
            stat = HttpStatus.OK;
            logger.info("Successfully fetched posts for subpost with id " + id);
        } catch (Exception e) {
            stat = HttpStatus.EXPECTATION_FAILED;
            logger.error("Could not fetch posts for subpost with id " + id);
        }
        return status(stat).body(pst);
    }

    @GetMapping("by-user/{email}")
    public List<Post> getPostsByEmail(@PathVariable String email) {
        HttpStatus stat;
        List<Post> psts = postService.getPostsByEmail(email);
        if(psts.size()==0)
        {
            logger.info("No comments present for " + email);
        }
        try {
            psts = postService.getPostsByEmail(email);
            stat = HttpStatus.OK;
            logger.info("Successfully fetched posts for email " + email);
        } catch (Exception e) {
            stat = HttpStatus.EXPECTATION_FAILED;
            logger.error("Could not fetch posts for email " + email);
        }
//        System.out.println("timestamp of first post is " + psts.get(0).getLocalDateTime());
        return psts;
    }
}
