package com.team4.gymsoftware.services;

import com.team4.gymsoftware.db.models.GymUser;
import com.team4.gymsoftware.db.models.RequestWorkout;
import com.team4.gymsoftware.db.models.Trainer;
import com.team4.gymsoftware.db.repositories.AuthSessionUserRepository;
import com.team4.gymsoftware.db.repositories.GymUserRepository;
import com.team4.gymsoftware.db.repositories.RequestWorkoutRepository;
import com.team4.gymsoftware.db.repositories.TrainerRepository;
import com.team4.gymsoftware.dto.AssignTrainerRequest;
import com.team4.gymsoftware.dto.CreateGymUserRequest;
import com.team4.gymsoftware.dto.RequestWorkoutRequest;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class GymUserServiceImpl implements GymUserService{

    private final GymUserRepository gymUserRepository;
    private final TrainerRepository trainerRepository;
    private final RequestWorkoutRepository requestWorkoutRepository;
    private final AuthSessionUserRepository authSessionUserRepository;

    public GymUserServiceImpl(GymUserRepository gymUserRepository,
                              TrainerRepository trainerRepository,
                              RequestWorkoutRepository requestWorkoutRepository,
                              AuthSessionUserRepository authSessionUserRepository){
        this.gymUserRepository = gymUserRepository;
        this.trainerRepository = trainerRepository;
        this.requestWorkoutRepository = requestWorkoutRepository;
        this.authSessionUserRepository = authSessionUserRepository;
    };

    @Override
    public Optional<GymUser> saveGymUser(CreateGymUserRequest createGymUserRequest) {

        if(gymUserRepository.findByName(createGymUserRequest.name()).isPresent()){
            return Optional.empty();
        }

        try{
            if(createGymUserRequest.password().isEmpty()){
                return Optional.empty();
            }
        }
        catch(NullPointerException e){
            return Optional.empty();
        }

        GymUser gymUser = new GymUser();
        gymUser.setName(createGymUserRequest.name());
        gymUser.setPassword(createGymUserRequest.password());

        if(createGymUserRequest.trainer_id() != null){
            Optional<Trainer> trainer = trainerRepository.findById(createGymUserRequest.trainer_id());

            if(trainer.isEmpty()){
                return Optional.empty();
            }

            gymUser.setTrainer(trainer.get());

        }

        return Optional.of(gymUserRepository.save(gymUser));

    }

    @Override
    public Optional<GymUser> assignTrainer(AssignTrainerRequest assignTrainerRequest){

        Optional<Trainer> trainer = trainerRepository.findById(assignTrainerRequest.trainer_id());
        Optional<GymUser> gymUser = gymUserRepository.findById(assignTrainerRequest.user_id());

        if(trainer.isEmpty() || gymUser.isEmpty()){
            return Optional.empty();
        }

        gymUser.get().setTrainer(trainer.get());

        return Optional.of(gymUserRepository.save(gymUser.get()));

    }

    @Override
    public Optional<RequestWorkout> requestWorkout(RequestWorkoutRequest requestWorkoutRequest) {
        GymUser gymUser = authSessionUserRepository.
                findByToken(requestWorkoutRequest.token()).get().getUser();
        Optional<Trainer> trainer = trainerRepository.findById(requestWorkoutRequest.trainer_id());

        RequestWorkout requestWorkout = new RequestWorkout();

        if(trainer.isEmpty()){
            return Optional.empty();
        }

        requestWorkout.setRequester(gymUser);
        requestWorkout.setReceiver(trainer.get());
        requestWorkout.setTitle(requestWorkoutRequest.title());
        requestWorkout.setDescription(requestWorkoutRequest.description());
        requestWorkout.setSent(Instant.now());

        return Optional.of(requestWorkoutRepository.save(requestWorkout));
    }

    @Override
    public Optional<GymUser> getUserByIdIfExists(long id) {
        return gymUserRepository.findById(id);
    }


    @Override
    public List<Trainer> getAllTrainers() {
        return trainerRepository.findAll();

    }



}
