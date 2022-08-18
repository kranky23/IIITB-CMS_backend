package com.spe.iiitbcms.mapper;

import com.spe.iiitbcms.dto.CommentsDto;
import com.spe.iiitbcms.model.Comment;
import com.spe.iiitbcms.model.Post;
import com.spe.iiitbcms.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "text", source = "commentsDto.text")
    @Mapping(target = "localDateTime", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "post", source = "post")
    @Mapping(target = "user", source = "user")
    Comment map(CommentsDto commentsDto, Post post, User user);

    @Mapping(target = "postId", expression = "java(comment.getPost().getPostId())")
    @Mapping(target = "email", expression = "java(comment.getUser().getEmail())")
    @Mapping(target = "username", expression = "java(comment.getUser().getName())")
    CommentsDto mapToDto(Comment comment);
}