package com.example.fitness.service;

import java.util.List;

import com.example.fitness.dto.CapacityResponse;
import com.example.fitness.dto.CheckinRequest;
import com.example.fitness.dto.CheckinResponse;
import com.example.fitness.model.MemberStatus;
import com.example.fitness.exception.BusinessException;
import com.example.fitness.exception.ResourceNotFoundException;
import com.example.fitness.model.Checkin;
import com.example.fitness.model.Member;
import com.example.fitness.repository.CheckinRepository;
import com.example.fitness.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CheckinService {

    private static final int MAX_CAPACITY = 50;

    private final CheckinRepository checkinRepository;
    private final MemberRepository memberRepository;

    public CapacityResponse getCapacity() {
        int current = checkinRepository.countCurrentlyCheckedIn();
        double percent = (double) current / MAX_CAPACITY * 100;

        String indicator;
        if (percent < 50) indicator = "GREEN";
        else if (percent <= 80) indicator = "YELLOW";
        else indicator = "RED";

        List<CheckinResponse> checkedIn = checkinRepository.findCurrentlyCheckedIn()
                .stream()
                .map(this::toResponse)
                .toList();

        return new CapacityResponse(current, MAX_CAPACITY, percent, indicator, checkedIn);
    }

    @Transactional
    public CheckinResponse checkIn(CheckinRequest request) {
        Member member = memberRepository.findByMemberNumber(request.getMemberNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Member not found."));

        if (member.getStatus() != MemberStatus.ACTIVE) {
            throw new BusinessException("Member is not active.");
        }

        if (checkinRepository.existsByMemberIdAndCheckedOutAtIsNull(member.getId())) {
            throw new BusinessException("Member is already checked in.");
        }

        Checkin checkin = Checkin.builder()
                .member(member)
                .build();

        return toResponse(checkinRepository.save(checkin));
    }

    @Transactional
    public CheckinResponse checkOut(Long checkinId) {
        Checkin checkin = checkinRepository.findById(checkinId)
                .orElseThrow(() -> new ResourceNotFoundException("Check-in record not found."));

        if (checkin.getCheckedOutAt() != null) {
            throw new BusinessException("Member already checked out.");
        }

        checkin.setCheckedOutAt(LocalDateTime.now());
        return toResponse(checkinRepository.save(checkin));
    }

    private CheckinResponse toResponse(Checkin c) {
        CheckinResponse r = new CheckinResponse();
        r.setId(c.getId());
        r.setCheckedInAt(c.getCheckedInAt());
        r.setCheckedOutAt(c.getCheckedOutAt());

        if (c.getMember() != null) {
            r.setMemberId(c.getMember().getId());
            r.setMemberNumber(c.getMember().getMemberNumber());
            r.setMemberFullName(c.getMember().getFirstName() + " " + c.getMember().getLastName());
        }

        return r;
    }
}