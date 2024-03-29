package com.spe.iiitbcms.service;

import com.spe.iiitbcms.dto.PostRequest;
import com.spe.iiitbcms.dto.PostResponse;
import com.spe.iiitbcms.exceptions.PostNotFoundException;
import com.spe.iiitbcms.exceptions.SubpostNotFoundException;
import com.spe.iiitbcms.mapper.PostMapper;
import com.spe.iiitbcms.model.Post;
import com.spe.iiitbcms.model.Subpost;
import com.spe.iiitbcms.model.User;
import com.spe.iiitbcms.repository.PostRepository;
import com.spe.iiitbcms.repository.SubpostRepository;
import com.spe.iiitbcms.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@AllArgsConstructor
@Slf4j
@Transactional
public class PostService {

    private final PostRepository postRepository;
    private final SubpostRepository subpostRepository;
    private final UserRepository userRepository;
    private final AuthService authService;
    private final PostMapper postMapper;

    public void save(PostRequest postRequest) {
        Subpost subpost = subpostRepository.getByName(postRequest.getSubpostName());
        System.out.println("SUbpost is " + subpost.toString());
        User user = userRepository.getByEmail(postRequest.getEmail());
        Post post = new Post();
        post.setPostName(postRequest.getPostName());
        post.setLocalDateTime(LocalDateTime.now());
        post.setDescription(postRequest.getDescription());
        post.setSubpost(subpost);
        post.setUrl(postRequest.getUrl());
        post.setVoteCount(0);
        post.setUser(user);
        postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new PostNotFoundException(id.toString()));
//        System.out.println(post.getDescription());
        PostResponse postResponse = new PostResponse();
        postResponse.setId(post.getPostId());
        postResponse.setSubpostName(post.getSubpost().getName());
        postResponse.setVoteCount(post.getVoteCount());
        postResponse.setDescription(post.getDescription());
        postResponse.setPostName(post.getPostName());
        return postResponse;
    }

    @Transactional(readOnly = true)
    public List<Post> getAllPosts() {
        return postRepository.findAll();

    }

    @Transactional(readOnly = true)
    public List<PostResponse> getPostsBySubpost(Long subpostId) {
        Subpost subpost = subpostRepository.findById(subpostId)
                .orElseThrow(() -> new SubpostNotFoundException(subpostId.toString()));
        List<Post> posts = postRepository.findAllBySubpost(subpost);
        return posts.stream().map(postMapper::mapToDto).collect(toList());
    }

    @Transactional(readOnly = true)
    public List<Post> getPostsByEmail(String email) {
        User user = userRepository.getByEmail(email);
        System.out.println("User of these posts is " + user.getEmail() + " " + user.getName());
        return postRepository.findByUser(user);
    }
}
