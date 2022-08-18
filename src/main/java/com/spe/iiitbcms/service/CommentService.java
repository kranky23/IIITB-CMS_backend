package com.spe.iiitbcms.service;

import com.spe.iiitbcms.dto.CommentsDto;
import com.spe.iiitbcms.exceptions.PostNotFoundException;
import com.spe.iiitbcms.exceptions.CMSException;
import com.spe.iiitbcms.mapper.CommentMapper;
import com.spe.iiitbcms.model.Comment;
import com.spe.iiitbcms.model.NotificationEmail;
import com.spe.iiitbcms.model.Post;
import com.spe.iiitbcms.model.User;
import com.spe.iiitbcms.repository.CommentRepository;
import com.spe.iiitbcms.repository.PostRepository;
import com.spe.iiitbcms.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
public class CommentService {
    private static final String POST_URL = "";
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final CommentMapper commentMapper;
    private final CommentRepository commentRepository;
    private final MailContentBuilder mailContentBuilder;
    private final MailService mailService;

    public void save(CommentsDto commentsDto)
    {
        System.out.println(commentsDto.getPostId());
        System.out.println(commentsDto.getId());

        User user = userRepository.getByEmail(commentsDto.getEmail());
//        Post pst = postRepository.getById(commentsDto.getPostId());
        Post post = postRepository.findById(commentsDto.getPostId())
                .orElseThrow(() -> new PostNotFoundException(commentsDto.getPostId().toString()));
        System.out.println("post is " + post.getPostName());
        System.out.println("user is " + user.getName());
        Comment comment = new Comment();
        comment.setPost(post);
        comment.setUser(user);
        comment.setText(commentsDto.getText());
        comment.setLocalDateTime(LocalDateTime.now());
//        System.out.println(comment.getText());
        commentRepository.save(comment);
//
//        String message = mailContentBuilder.build(post.getUser().getName() + " ("+post.getUser().getRollNo() + ") " + " posted a comment on your post." + POST_URL);
//        sendCommentNotification(message, post.getUser());
    }

    private void sendCommentNotification(String message, User user) {
        mailService.sendMail(new NotificationEmail(user.getName() + " ("+user.getRollNo()+") " + "Commented on your post", user.getEmail(), message));
    }

    public List<CommentsDto> getAllCommentsForPost(Long postId) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new PostNotFoundException(postId.toString()));
        List<Comment> al;
        al = commentRepository.findByPost(post);
        for(Comment c: al)
        {
            System.out.println("user is " + c.getUser().getName());
            System.out.println(c.getText());
            System.out.println(c.getPost().getDescription());
        }
//        return al;
        return commentRepository.findByPost(post)
                .stream()
                .map(commentMapper::mapToDto).collect(toList());
    }

    public List<CommentsDto> getAllCommentsForUser(String rollNo) {
        User user = userRepository.findByRollNo(rollNo)
                .orElseThrow(() -> new UsernameNotFoundException(rollNo));
        return commentRepository.findAllByUser(user)
                .stream()
                .map(commentMapper::mapToDto)
                .collect(toList());
    }

    public boolean containsSwearWords(String comment) {
        if (comment.contains("shit")) {
            throw new CMSException("Comments contains unacceptable language");
        }
        return false;
    }
}
