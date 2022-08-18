package com.spe.iiitbcms.repository;

import com.spe.iiitbcms.model.Comment;
import com.spe.iiitbcms.model.Post;
import com.spe.iiitbcms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select c from Comment c where c.post = ?1")
    List<Comment> findByPost(Post post);

    List<Comment> findAllByUser(User user);
}
