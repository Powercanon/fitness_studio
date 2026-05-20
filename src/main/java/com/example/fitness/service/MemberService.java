package com.example.fitness.service;

import com.example.fitness.dto.MemberRequest;
import com.example.fitness.dto.MemberResponse;
import com.example.fitness.dto.MemberStatusRequest;
import com.example.fitness.exception.BusinessException;
import com.example.fitness.exception.ResourceNotFoundException;
import com.example.fitness.model.ContractModel;
import com.example.fitness.model.Gender;
import com.example.fitness.model.Member;
import com.example.fitness.model.MemberStatus;
import com.example.fitness.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public List<MemberResponse> getAll(MemberStatus status, Gender gender, ContractModel contractModel,
                                       LocalDate contractFrom, LocalDate contractTo, String search,
                                       String sortBy, String sortDir) {
        List<Member> members = memberRepository.findFiltered(status, gender, contractModel, contractFrom, contractTo, search);

        Comparator<Member> comparator = getComparator(sortBy);
        if ("desc".equalsIgnoreCase(sortDir)) {
            comparator = comparator.reversed();
        }

        return members.stream()
                .sorted(comparator)
                .map(this::toResponse)
                .toList();
    }

    private Comparator<Member> getComparator(String sortBy) {
        if (sortBy == null) sortBy = "lastName";
        return switch (sortBy) {
            case "firstName" -> Comparator.comparing(Member::getFirstName, String.CASE_INSENSITIVE_ORDER);
            case "email" -> Comparator.comparing(Member::getEmail, String.CASE_INSENSITIVE_ORDER);
            case "memberNumber" -> Comparator.comparing(Member::getMemberNumber);
            case "contractStart" -> Comparator.comparing(Member::getContractStart);
            case "birthDate" -> Comparator.comparing(Member::getBirthDate);
            case "status" -> Comparator.comparing(m -> m.getStatus().name());
            default -> Comparator.comparing(Member::getLastName, String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(Member::getFirstName, String.CASE_INSENSITIVE_ORDER);
        };
    }

    public MemberResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Transactional
    public MemberResponse create(MemberRequest request) {
        if (memberRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email already in use.");
        }

        Member member = new Member();
        applyRequest(member, request);
        member.setMemberNumber(generateMemberNumber());
        member.setStatus(MemberStatus.ACTIVE);

        return toResponse(memberRepository.save(member));
    }

    @Transactional
    public MemberResponse update(Long id, MemberRequest request) {
        Member member = findOrThrow(id);

        if (!member.getEmail().equals(request.getEmail())
                && memberRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException("Email already in use.");
        }

        applyRequest(member, request);
        return toResponse(memberRepository.save(member));
    }

    @Transactional
    public MemberResponse updateStatus(Long id, MemberStatusRequest request) {
        Member member = findOrThrow(id);
        member.setStatus(request.getStatus());
        return toResponse(memberRepository.save(member));
    }

    @Transactional
    public void delete(Long id) {
        if (!memberRepository.existsById(id)) {
            throw new ResourceNotFoundException("Member not found.");
        }
        memberRepository.deleteById(id);
    }

    private Member findOrThrow(Long id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Member not found."));
    }

    private void applyRequest(Member member, MemberRequest req) {
        member.setFirstName(req.getFirstName());
        member.setLastName(req.getLastName());
        member.setEmail(req.getEmail());
        member.setPhone(req.getPhone());
        member.setBirthDate(req.getBirthDate());
        member.setGender(req.getGender());
        member.setContractModel(req.getContractModel());
        member.setContractStart(req.getContractStart());
    }

    private String generateMemberNumber() {
        int year = LocalDate.now().getYear();
        String prefix = "M-" + year + "-";
        int max = memberRepository.findMaxSequenceForYear(prefix);
        return prefix + String.format("%05d", max + 1);
    }

    private MemberResponse toResponse(Member m) {
        MemberResponse r = new MemberResponse();
        r.setId(m.getId());
        r.setMemberNumber(m.getMemberNumber());
        r.setFirstName(m.getFirstName());
        r.setLastName(m.getLastName());
        r.setEmail(m.getEmail());
        r.setPhone(m.getPhone());
        r.setBirthDate(m.getBirthDate());
        r.setGender(m.getGender());
        r.setContractModel(m.getContractModel());
        r.setContractStart(m.getContractStart());
        r.setStatus(m.getStatus());
        return r;
    }
}