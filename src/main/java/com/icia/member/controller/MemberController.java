package com.icia.member.controller;

import com.icia.member.dto.MemberDTO;
import com.icia.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.NoSuchElementException;

@Controller
@RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/save")
    public String savePage(){
        return "memberPages/memberSave";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute MemberDTO memberDTO){
        memberService.save(memberDTO);
        return "redirect:/member/login";
    }

    @GetMapping()
    public String findAll(Model model){
        model.addAttribute("memberList", memberService.findAll());
        return "memberPages/memberList";
    }

    @GetMapping("/{id}")
    public String detail(@PathVariable("id") Long id, Model model){
        try {
            model.addAttribute("member", memberService.findById(id));
            return "memberPages/memberDetail";
        } catch (Exception e){
            return "memberPages/memberNotFound";
        }
    }

    @GetMapping("/login")
    public String loginPage(){
        return "memberPages/memberLogin";
    }

    @PostMapping("/login")
    public ResponseEntity login(@ModelAttribute MemberDTO memberDTO, HttpSession session){
        try {
            MemberDTO dto = memberService.findByEmail(memberDTO.getMemberEmail());
            if(dto.getMemberEmail().equals(memberDTO.getMemberEmail())
                    && dto.getMemberPassword().equals(memberDTO.getMemberPassword())){
                session.setAttribute("loginEmail", dto.getMemberEmail());
                session.setAttribute("loginId", dto.getId());
                return new ResponseEntity(HttpStatus.OK);
            } else {
                return new ResponseEntity(HttpStatus.CONFLICT);
            }
        } catch (NoSuchElementException e) {
            return new ResponseEntity(HttpStatus.CONFLICT);
        }
    }

    @GetMapping("/main")
    public String member(){
        return "memberPages/memberMain";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/dup-check")
    public ResponseEntity dupCheck(@RequestParam("memberEmail") String memberEmail) {
        System.out.println("memberEmail = " + memberEmail);
        try {
            MemberDTO dto = memberService.findByEmail(memberEmail);
            return new ResponseEntity(HttpStatus.CONFLICT);
        } catch (NoSuchElementException e){
            return new ResponseEntity(HttpStatus.OK);
        }
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") Long id){
        memberService.delete(id);
        return "redirect:/member";
    }
    @DeleteMapping("/{id}")
    public ResponseEntity axios_delete(@PathVariable("id") Long id){
        memberService.delete(id);
        return new ResponseEntity(HttpStatus.OK);
    }

    @GetMapping("/update/{id}")
    public String updatePage(@PathVariable("id") Long id, Model model){
        try {
            MemberDTO memberDTO = memberService.findById(id);
            model.addAttribute("member", memberDTO);
            return "memberPages/memberUpdate";
        } catch (NoSuchElementException e){
            return "memberPages/memberNotFound";
        }
    }

    @PostMapping("/update")
    public String update(@ModelAttribute MemberDTO memberDTO){
        memberService.update(memberDTO);
        return "redirect:/member/"+memberDTO.getId();
    }
}
