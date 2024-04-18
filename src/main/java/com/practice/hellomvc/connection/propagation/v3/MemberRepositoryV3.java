package com.practice.hellomvc.connection.propagation.v3;

import com.practice.hellomvc.connection.propagation.Member;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MemberRepositoryV3 {
    private final EntityManager em;

    @Transactional(propagation = Propagation.REQUIRED)
    public void save(Member member){
        log.info("save member = {}",member);
        em.persist(member);
    }
    public Optional<Member> findByName(String username){
        return em.createQuery("select m from Member m where m.username=:username",Member.class)
                .setParameter("username",username)
                .getResultList().stream().findAny();
    }
}
