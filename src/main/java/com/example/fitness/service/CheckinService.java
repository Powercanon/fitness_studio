package com.example.fitness.service;

import java.util.Comparator;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Service
@RequiredArgsConstructor
public class CheckinService {

    private static final int MAX_CAPACITY = 50;

    private final CheckinRepository checkinRepository;
    private final MemberRepository memberRepository;

    public List<CheckinResponse> getAll(Boolean checkedOut, LocalDate dateFrom, LocalDate dateTo,
                                        String search, String sortBy, String sortDir) {
        LocalDateTime dateTimeFrom = dateFrom != null ? dateFrom.atStartOfDay() : null;
        LocalDateTime dateTimeTo = dateTo != null ? dateTo.atTime(LocalTime.MAX) : null;

        List<Checkin> checkins = checkinRepository.findFiltered(checkedOut, dateTimeFrom, dateTimeTo, search);

        Comparator<Checkin> comparator = getComparator(sortBy);
        if ("desc".equalsIgnoreCase(sortDir)) {
            comparator = comparator.reversed();
        }

        return checkins.stream()
                .sorted(comparator)
                .map(this::toResponse)
                .toList();
    }

    private Comparator<Checkin> getComparator(String sortBy) {
        if (sortBy == null) sortBy = "checkedInAt";
        return switch (sortBy) {
            case "memberName" -> Comparator.comparing(c -> c.getMember().getLastName(), String.CASE_INSENSITIVE_ORDER);
            case "memberNumber" -> Comparator.comparing(c -> c.getMember().getMemberNumber());
            case "checkedOutAt" -> Comparator.comparing(Checkin::getCheckedOutAt, Comparator.nullsLast(Comparator.naturalOrder()));
            default -> Comparator.comparing(Checkin::getCheckedInAt);
        };
    }

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