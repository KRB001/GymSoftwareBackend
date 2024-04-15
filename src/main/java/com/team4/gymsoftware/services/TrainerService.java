package com.team4.gymsoftware.services;

import com.team4.gymsoftware.db.models.Trainer;
import com.team4.gymsoftware.dto.CreateTrainerRequest;

import java.util.Optional;

public interface TrainerService {

    public Optional<Trainer> saveTrainer(CreateTrainerRequest createTrainerRequest);

}