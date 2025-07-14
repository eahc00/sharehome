package com.sharehome.place.domain;

import com.sharehome.common.exception.NotFoundException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlaceRepository extends JpaRepository<Place, Long> {

    default Place getById(Long id) {
        return findById(id).orElseThrow(() ->
                new NotFoundException("해당 id를 가진 숙소가 없습니다.")
        );
    }

    Optional<Place> findByName(String name);
}
