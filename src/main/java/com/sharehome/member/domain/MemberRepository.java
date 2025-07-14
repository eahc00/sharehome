package com.sharehome.member.domain;

import com.sharehome.common.exception.NotFoundException;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    default Member getById(Long id) {
        return findById(id).orElseThrow(() ->
                new NotFoundException("해당 id를 가진 회원이 없습니다.")
        );
    }

    Optional<Member> findByEmail(String email);
}

