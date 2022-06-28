package com.spe.iiitbcms.repository;

import com.spe.iiitbcms.model.Post;
import com.spe.iiitbcms.model.Subpost;
import com.spe.iiitbcms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findAllBySubpost(Subpost subpost);

    @Query("select u from Post u where u.user = ?1")
    List<Post> findByUser(User user);
}
