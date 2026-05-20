package com.example.fitness.service;

import com.example.fitness.dto.TrainerRequest;
import com.example.fitness.dto.TrainerResponse;
import com.example.fitness.exception.ResourceNotFoundException;
import com.example.fitness.model.Trainer;
import com.example.fitness.repository.TrainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TrainerService {

    private final TrainerRepository trainerRepository;

    public List<TrainerResponse> getAll() {
        return trainerRepository.findAllByOrderByLastNameAscFirstNameAsc()
                .stream()
                .map(this::toResponse)
                .toList();
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