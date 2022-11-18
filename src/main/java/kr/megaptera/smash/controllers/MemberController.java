package kr.megaptera.smash.controllers;

import kr.megaptera.smash.dtos.MembersDetailDto;
import kr.megaptera.smash.services.DeleteGameMemberService;
import kr.megaptera.smash.services.GetMembersService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("members")
public class MemberController {
    private final GetMembersService getMembersService;
    private final DeleteGameMemberService deleteGameMemberService;

    public MemberController(GetMembersService getMembersService,
                            DeleteGameMemberService deleteGameMemberService) {
        this.getMembersService = getMembersService;
        this.deleteGameMemberService = deleteGameMemberService;
    }

    @GetMapping("games/{gameId}")
    public MembersDetailDto members(
        @PathVariable("gameId") Long targetGameId
    ) {
        return getMembersService.findTargetMembers(targetGameId);
    }

    @DeleteMapping("games/{gameId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMember(
        @RequestAttribute("userId") Long accessedUserId,
        @PathVariable("gameId") Long gameId
    ) {
        deleteGameMemberService.deleteGameMember(accessedUserId, gameId);
    }
}
