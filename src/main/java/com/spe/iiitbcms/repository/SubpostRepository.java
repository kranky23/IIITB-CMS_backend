package com.spe.iiitbcms.repository;

import com.spe.iiitbcms.model.Subpost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubpostRepository extends JpaRepository<Subpost, Long> {

    Optional<Subpost> findByName(String subpostName);

    @Query("select u from Subpost u")
    List<Subpost> findAll();

    @Query("select u from Subpost u where u.name=?1")
    Subpost getByName(String role);
}
