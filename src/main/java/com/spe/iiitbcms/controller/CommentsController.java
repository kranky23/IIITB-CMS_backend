package com.spe.iiitbcms.controller;

import com.spe.iiitbcms.dto.CommentsDto;
import com.spe.iiitbcms.model.Comment;
import com.spe.iiitbcms.service.CommentService;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/api/comments/")
@AllArgsConstructor
public class CommentsController {
    private static final Logger logger = LogManager.getLogger(CommentsController.class);
    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<Void> createComment(@RequestBody CommentsDto commentsDto) {
        try {
            commentService.save(commentsDto);
            logger.info("Comment created successfully");
        } catch(Exception e) {
            logger.error("Comment could not be created");
        }
        return new ResponseEntity<>(CREATED);
    }

    @GetMapping("/by-post/{postId}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForPost(@PathVariable Long postId) {
        List<CommentsDto> cmts = new ArrayList<>();
        try {
            cmts = commentService.getAllCommentsForPost(postId);
            logger.info("Successfully fetched comments for post with id " + postId);
        } catch (Exception e){
            logger.error("Error in fetching comments for the post " + postId);
        }
        return ResponseEntity.status(OK).body(cmts);
    }

    @GetMapping("/by-user/{email}")
    public ResponseEntity<List<CommentsDto>> getAllCommentsForUser(@PathVariable String email){
        List<CommentsDto> cmts = new ArrayList<>();
        try {
            cmts = commentService.getAllCommentsForUser(email);
            logger.info("Successfully fetched comments for the user with roll number " + email);
        } catch (Exception e){
            logger.error("Error in fetching comments for the user with roll number " + email);
        }
        return ResponseEntity.status(OK).body(cmts);
    }

}
