package kr.megaptera.smash.services;

import kr.megaptera.smash.dtos.MemberDetailDto;
import kr.megaptera.smash.dtos.MembersDetailDto;
import kr.megaptera.smash.exceptions.UserNotFound;
import kr.megaptera.smash.models.Member;
import kr.megaptera.smash.models.User;
import kr.megaptera.smash.repositories.MemberRepository;
import kr.megaptera.smash.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GetMembersService {
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;

    public GetMembersService(MemberRepository memberRepository,
                             UserRepository userRepository) {
        this.memberRepository = memberRepository;
        this.userRepository = userRepository;
    }

    public MembersDetailDto findTargetMembers(Long targetGameId) {
        List<Member> members = memberRepository.findAllByGameId(targetGameId);

        List<MemberDetailDto> memberDetailDtos = members.stream()
            .map(member -> {
                User user = userRepository.findById(member.userId())
                    .orElseThrow(UserNotFound::new);
                return new MemberDetailDto(
                    member.id(),
                    user.name().value(),
                    user.gender().value(),
                    user.phoneNumber().value()
                );
            })
            .toList();

        return new MembersDetailDto(memberDetailDtos);
    }
}
