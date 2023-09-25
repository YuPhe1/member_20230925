package com.icia.member.controller;

import com.icia.member.dto.MemberDTO;
import com.icia.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member/save")
    public String savePage(){
        return "memberPages/memberSave";
    }

    @PostMapping("/member/save")
    public String save(@ModelAttribute MemberDTO memberDTO){
        memberService.save(memberDTO);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String findAll(Model model){
        model.addAttribute("memberList", memberService.findAll());
        return "memberPages/memberList";
    }

    @GetMapping("/member/{id}")
    public String detail(@PathVariable("id") Long id, Model model){
        model.addAttribute("member", memberService.findById(id));
        return "memberPages/memberDetail";
    }
}
