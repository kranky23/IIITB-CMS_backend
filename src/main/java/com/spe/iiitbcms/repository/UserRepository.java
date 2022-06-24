package com.spe.iiitbcms.repository;
import com.spe.iiitbcms.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Query("select u from User u where u.rollNo = ?1")
    Optional<User> findByRollNo(String rollNo);


    @Query("select u from User u where u.email = ?1")
    Optional<User> findByEmail(String email);

    @Query("select u from User u where u.email = ?1")
    User getByEmail(String email);

    @Query("select u from User u where u.email = ?1")
    UserDetails getEmail(String email);


}
