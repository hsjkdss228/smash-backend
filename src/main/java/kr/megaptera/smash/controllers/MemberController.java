package kr.megaptera.smash.controllers;

import kr.megaptera.smash.dtos.MemberCancelRegisterDto;
import kr.megaptera.smash.dtos.MemberRegisterDto;
import kr.megaptera.smash.dtos.MemberRegisterResultDto;
import kr.megaptera.smash.models.Member;
import kr.megaptera.smash.services.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/member/register")
    @ResponseStatus(HttpStatus.CREATED)
    public MemberRegisterResultDto register(
        @RequestBody MemberRegisterDto memberRegisterDto,
        @RequestAttribute("userId") Long accessedUserId
    ) {
        Member member = memberService.createMember(
            memberRegisterDto.getGameId(),
            memberRegisterDto.getTeamId(),
            memberRegisterDto.getRoleId(),
            accessedUserId
        );

        return member.toRegisterResultDto();
    }

    @DeleteMapping("/member/user")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancelRegister(
        @RequestBody MemberCancelRegisterDto memberCancelRegisterDto,
        @RequestAttribute("userId") Long accessedUserId
    ) {
        memberService.cancelRegister(
            memberCancelRegisterDto.getRoleId(),
            accessedUserId
        );
    }
}
