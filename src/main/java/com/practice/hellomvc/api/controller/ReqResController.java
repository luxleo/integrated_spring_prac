package com.practice.hellomvc.api.controller;

import com.practice.hellomvc.domain.TempMember;
import com.practice.hellomvc.repository.TempMemberRepository;
import com.practice.hellomvc.request.TempMemberCreate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/req-res")
@RequiredArgsConstructor
public class ReqResController {
    private final TempMemberRepository tempMemberRepository;

    @GetMapping("/member/{memberName}")
    public ResponseEntity<TempMember> getMember(@PathVariable String memberName) {
        TempMember findTempMember = tempMemberRepository.findMemberByName(memberName);
        return new ResponseEntity<>(findTempMember, HttpStatus.OK);
    }

    @PostMapping("/create/member")
    public ResponseEntity<String> createMember(@ModelAttribute TempMemberCreate memberDTO) {
        return null;
    }
}

