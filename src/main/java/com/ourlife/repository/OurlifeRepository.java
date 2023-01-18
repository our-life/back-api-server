package com.ourlife.repository;

import com.ourlife.entity.OurLife;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OurlifeRepository extends JpaRepository<OurLife, Long> {

    List<OurLife> findAllByUserId(Long id);
}
