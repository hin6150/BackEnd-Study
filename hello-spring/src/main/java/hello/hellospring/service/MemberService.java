package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepositroy;
import hello.hellospring.repository.MemoryMemberRepository;
import org.springframework.web.ErrorResponseException;

import java.lang.reflect.Constructor;
import java.util.List;
import java.util.Optional;

public class MemberService {

    private final MemberRepositroy memberRepositroy;

    public MemberService(MemberRepositroy memberRepositroy) {
        this.memberRepositroy = memberRepositroy;
    }

    /** 회원가입 **/
    public Long join (Member member) {
        // 같은 이름 중복 회원 x
        validateDuplicateMember(member);

        memberRepositroy.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        memberRepositroy.findByName(member.getName())
            .ifPresent(m -> {
                 throw new IllegalStateException("가입된 회원입니다.");
             });
    }

    /** 전체 회원 조회 **/
    public List<Member> findMembers() {
        return memberRepositroy.findAll();
    }

    public  Optional<Member> findOne(Long memberId){
        return memberRepositroy.findById(memberId);
    }
}
