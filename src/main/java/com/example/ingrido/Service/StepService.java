package com.example.ingrido.Service;

import com.example.ingrido.Model.Step;
import com.example.ingrido.Repository.StepRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StepService {
    private final StepRepository stepRepository;

    public StepService(StepRepository stepRepository) {
        this.stepRepository = stepRepository;
    }

    public List<Step> getAllSteps() {
        return stepRepository.findAll();
    }

    public Step saveStep(Step step) {
        return stepRepository.save(step);
    }

    public void deleteStep(Long id) {
        stepRepository.deleteById(id);
    }

    public Optional<Step> getStepById(Long id) {
        return stepRepository.findById(id);
    }
}
