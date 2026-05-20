package com.example.fitness.service;

import com.example.fitness.dto.TrainerRequest;
import com.example.fitness.dto.TrainerResponse;
import com.example.fitness.exception.ResourceNotFoundException;
import com.example.fitness.model.Trainer;
import com.example.fitness.repository.TrainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainerService {

    private final TrainerRepository trainerRepository;

    public List<TrainerResponse> getAll(Boolean active, String search, String sortBy, String sortDir) {
        List<Trainer> trainers = trainerRepository.findFiltered(active, search);

        Comparator<Trainer> comparator = getComparator(sortBy);
        if ("desc".equalsIgnoreCase(sortDir)) {
            comparator = comparator.reversed();
        }

        return trainers.stream()
                .sorted(comparator)
                .map(this::toResponse)
                .toList();
    }

    private Comparator<Trainer> getComparator(String sortBy) {
        if (sortBy == null) sortBy = "lastName";
        return switch (sortBy) {
            case "firstName" -> Comparator.comparing(Trainer::getFirstName, String.CASE_INSENSITIVE_ORDER);
            case "email" -> Comparator.comparing(Trainer::getEmail, String.CASE_INSENSITIVE_ORDER);
            case "active" -> Comparator.comparing(Trainer::isActive).reversed();
            default -> Comparator.comparing(Trainer::getLastName, String.CASE_INSENSITIVE_ORDER)
                    .thenComparing(Trainer::getFirstName, String.CASE_INSENSITIVE_ORDER);
        };
    }

    public TrainerResponse getById(Long id) {
        return toResponse(findOrThrow(id));
    }

    @Transactional
    public TrainerResponse create(TrainerRequest request) {
        Trainer trainer = new Trainer();
        applyRequest(trainer, request);
        trainer.setActive(true);
        return toResponse(trainerRepository.save(trainer));
    }

    @Transactional
    public TrainerResponse update(Long id, TrainerRequest request) {
        Trainer trainer = findOrThrow(id);
        applyRequest(trainer, request);
        return toResponse(trainerRepository.save(trainer));
    }

    @Transactional
    public TrainerResponse toggleActive(Long id) {
        Trainer trainer = findOrThrow(id);
        trainer.setActive(!trainer.isActive());
        return toResponse(trainerRepository.save(trainer));
    }


    @Transactional
    public void delete(Long id) {
        trainerRepository.delete(findOrThrow(id));
    }

    private Trainer findOrThrow(Long id) {
        return trainerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Trainer not found."));
    }

    private void applyRequest(Trainer trainer, TrainerRequest req) {
        trainer.setFirstName(req.getFirstName());
        trainer.setLastName(req.getLastName());
        trainer.setEmail(req.getEmail());
        trainer.setPhone(req.getPhone());
    }

    private TrainerResponse toResponse(Trainer t) {
        TrainerResponse r = new TrainerResponse();
        r.setId(t.getId());
        r.setFirstName(t.getFirstName());
        r.setLastName(t.getLastName());
        r.setEmail(t.getEmail());
        r.setPhone(t.getPhone());
        r.setActive(t.isActive());
        return r;
    }
}