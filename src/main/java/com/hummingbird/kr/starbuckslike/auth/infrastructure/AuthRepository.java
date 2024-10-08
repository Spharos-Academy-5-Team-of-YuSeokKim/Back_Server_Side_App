package com.hummingbird.kr.starbuckslike.auth.infrastructure;

import com.hummingbird.kr.starbuckslike.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface AuthRepository extends JpaRepository<Member, Long> {

  Optional<Member> findByMemberUID(String uuid);

  @Query("SELECT m FROM Member m WHERE m.loginID = :loginID")
  Optional<Member> findByLoginID(@Param("loginID") String loginID);

  @Query("SELECT m FROM Member m WHERE m.email = :email")
  Optional<Member> findByEmail(@Param("email") String email);

  Boolean existsByPhone(String phone);

  Boolean existsByEmail(String email);

  Boolean existsByLoginID(String loginID);


  @Query("SELECT m FROM Member m WHERE m.phone = :phone")
  Optional<Member> findByPhone(@Param("phone") String phone);

  @Modifying
  @Transactional
  @Query("UPDATE Member m SET m.password = :password WHERE m.memberUID = :uuid")
  void updatePasswordByUuid(@Param("uuid") String uuid, @Param("password") String password);

  @Modifying
  @Transactional
  @Query("UPDATE Member m SET m.password = :password WHERE m.loginID = :loginID")
  void updatePasswordByLoginID(@Param("loginID") String loginID, @Param("password") String password);

  @Modifying
  @Transactional
  @Query("UPDATE Member m SET m.isDeleted = true WHERE m.memberUID = :uuid")
  void disableMember(@Param("uuid") String uuid);
}