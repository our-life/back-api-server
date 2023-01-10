package com.ourlife.repository;

import com.ourlife.entity.Imgs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ImgsRepository extends JpaRepository<Imgs, Long> {

    Optional<Imgs> findByOurLifeId(Long ourLife_id);

}
