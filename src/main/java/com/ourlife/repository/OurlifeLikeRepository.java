package com.ourlife.repository;

import com.ourlife.entity.OurLife;
import com.ourlife.entity.OurlifeLike;
import com.ourlife.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OurlifeLikeRepository extends JpaRepository<OurlifeLike, Long> {

    boolean existsByOurLifeIdAndUserId(Long ourlifeId, Long userId);

    Optional<OurlifeLike> findByOurLifeIdAndUserId(Long ourlifeId, Long userId);

    int countByOurLifeId(Long ourlifeId);

    List<OurlifeLike> findByOurLifeId(Long araId);

}
