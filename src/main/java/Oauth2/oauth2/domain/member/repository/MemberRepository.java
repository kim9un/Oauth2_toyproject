package Oauth2.oauth2.domain.member.repository;

import Oauth2.oauth2.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByUsername(String username);

}
