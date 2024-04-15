package com.practice.hellomvc.repository;

import com.practice.hellomvc.domain.TempMember;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class TempMemberRepository {
    private static Map<Long, TempMember> store = new HashMap<>();
    private static long sequential = 0;

    public void save(TempMember tempMember) {
        tempMember.setId(sequential);
        store.put(sequential, tempMember);
        sequential++;
    }

    public TempMember findMemberById(Long id) {
        return store.getOrDefault(id, null);
    }
    public TempMember findMemberByName(String name) {
        return store.values().stream()
                .filter(tempMember -> tempMember.getName().equals(name))
                .findFirst().orElse(null);
    }

    public List<TempMember> findAll() {
        return store.values().stream().toList();
    }
}
