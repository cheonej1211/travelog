package com.travelog.member.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.travelog.domain.Member;


@Repository
public interface MemberRepository extends JpaRepository<Member, Long>{

	Optional<Member> findByLoginId(String loginId);

}
