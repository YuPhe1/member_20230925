package com.icia.member;

import com.icia.member.dto.MemberDTO;
import com.icia.member.repository.MemberRepository;
import com.icia.member.service.MemberService;
// Assertions에 속한 모든 static 메서드를 사용할 메서드 이름만 작성하여 사용 가능하도록
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.NoSuchElementException;
import java.util.stream.IntStream;

@SpringBootTest
public class MemberTest {

    // 테스트 클래스에서 사용할 클래스 객체 Autowired로 주입
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;

    // 회원 가입용 메서드 정의
    private MemberDTO newMember(int i){
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setMemberEmail("test_email" + i);
        memberDTO.setMemberPassword("test_pass" + i);
        memberDTO.setMemberName("test_name" + i);
        memberDTO.setMemberBirth("2023-09-27" + i);
        memberDTO.setMemberMobile("010-1111-1111" + i);
        return memberDTO;
    }

    @Test
    @DisplayName("회원 데이터 붓기")
    public void dataInsert(){
//        for(int i = 1; i <= 20; i++){
//            MemberDTO memberDTO = newMember(i);
//            memberService.save(memberDTO);
//        }
        IntStream.rangeClosed(1, 20).forEach(i -> {
            MemberDTO memberDTO = newMember(i);
            memberService.save(memberDTO);
        });
    }

    // 회원가입 기능 테스트
    /*
        1. 새로운 회원을 하나 가입시킴
        2. 가입된 회원의 id 값을 받아옴
        3. 그 id로 조회 기능을 실행
        4. 1번에서 만든 회원의 이메일 값과 3번에서 조회한 이메일 값이 일치하면 테스트 성공
     */
    @Test
    @Transactional
    @Rollback(value = true)
    @DisplayName("회원가입 테스트")
    public void memberSaveTest(){
        // 1.
        MemberDTO newMemberDTO = new MemberDTO();
        newMemberDTO.setMemberEmail("test_email");
        newMemberDTO.setMemberPassword("test_pass");
        newMemberDTO.setMemberName("test_name");
        newMemberDTO.setMemberBirth("2023-09-27");
        newMemberDTO.setMemberMobile("010-1111-1111");
        // 2.
        Long saveId = memberService.save(newMemberDTO);
        // 3.
        MemberDTO findMember = memberService.findById(saveId);
        // 4.
        assertThat(newMemberDTO.getMemberEmail()).isEqualTo(findMember.getMemberEmail());
    }

    /**
     * 1. 신규회원 가입
     * 2. 로그인을 위한 DTO 객체 생성 후 로그인 수행
     * 3. 로그인 결과 값이 있는지로 통해서 성공여부 판단
     */
    @Test
    @Transactional
    @Rollback(value = true)
    @DisplayName("로그인 테스트")
    public void loginTest() {
        // 1.
        MemberDTO memberDTO = newMember(999);
        memberService.save(memberDTO);
        // 2.
        MemberDTO loginMemberDTO = new MemberDTO();
        loginMemberDTO.setMemberEmail(memberDTO.getMemberEmail());
        loginMemberDTO.setMemberPassword(memberDTO.getMemberPassword());
        boolean loginResult = memberService.login(loginMemberDTO);
        // 3.
        assertThat(true).isEqualTo(loginResult);
    }

    /**
     * 삭제기능 테스트
     * 1. 회원가입 수행
     * 2. 회원가입 후 id 받아옴
     * 3. 삭제 기능 수행
     * 4. 삭제 후 해당 id로 조회했을 때 NoSuchElementException 빌생
     */
    @Test
    @Transactional
    @Rollback
    @DisplayName("회원 삭제 테스트")
    public void deleteTest(){
        // 1.
        MemberDTO memberDTO = newMember(9999);
        // 2.
        Long saveId = memberService.save(memberDTO);
        // 3.
        memberService.delete(saveId);
        // 4.
        assertThatThrownBy(() -> memberService.findById(saveId)).isInstanceOf(NoSuchElementException.class);
    }
}
