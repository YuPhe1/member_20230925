package com.icia.member.service;

import com.icia.member.dto.MemberDTO;
import com.icia.member.entity.MemberEntity;
import com.icia.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public Long save(MemberDTO memberDTO) {
        MemberEntity memberEntity = MemberEntity.toSaveEntity(memberDTO);
        return memberRepository.save(memberEntity).getId();
    }

    public List<MemberDTO> findAll() {
        List<MemberEntity> memberEntityList = memberRepository.findAll();
        List<MemberDTO> memberDTOList = new ArrayList<>();
        for(MemberEntity memberEntity : memberEntityList){
            memberDTOList.add(MemberDTO.toMemberDTO(memberEntity));
        }
        return memberDTOList;
    }

    public MemberDTO findById(Long id) {
        MemberEntity memberEntity = memberRepository.findById(id).orElseThrow(() -> new NoSuchElementException());
        return MemberDTO.toMemberDTO(memberEntity);
    }

    public MemberDTO findByMemberEmail(String memberEmail) {
        MemberEntity memberEntity = memberRepository.findByMemberEmail(memberEmail).orElseThrow(() -> new NoSuchElementException());
        return MemberDTO.toMemberDTO(memberEntity);
    }

    public boolean login(MemberDTO memberDTO) {
        MemberEntity memberEntity = memberRepository.findByMemberEmailAndMemberPassword(memberDTO.getMemberEmail(), memberDTO.getMemberPassword())
                .orElseThrow(() -> new NoSuchElementException());
        return true;
    }

    public void delete(Long id) {
        memberRepository.deleteById(id);
    }

    public void update(MemberDTO memberDTO) {
        MemberEntity memberEntity = MemberEntity.toEntity(memberDTO);
        memberRepository.save(memberEntity);
    }

    public boolean emailCheck(String memberEmail) {
        Optional<MemberEntity> optionalMemberEntity = memberRepository.findByMemberEmail(memberEmail);
        if(optionalMemberEntity.isEmpty()){
            return true;
        } else {
            return false;
        }
    }
}
