package com.hummingbird.kr.starbuckslike.auth.infrastucture;

import com.hummingbird.kr.starbuckslike.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

//custom repository for memeber, to protect user
//like pw reset(update), need auth or privileged, so divide Repo interface for each service(member, auth)
@Repository
public interface AuthRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByMemberUID(String memberUid);

    @Query("select m.password, m.memberUID, m.nickname from Member m where m.loginID = :loginId")
    Optional<Member> findByid(@Param("loginId")String loginId);

    @Modifying
    @Transactional
    @Query("UPDATE Member m SET m.password = :password WHERE m.memberUID = :uuid")
    void updatePasswordByUuid(@Param("uuid") String uuid, @Param("password") String password);
}