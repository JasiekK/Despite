package com.despite.repository;

import com.despite.entities.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    @Query("select e from Exercise e where e.creator.id = ?#{principal.id}")
    List<Exercise> findAllByPrincipal();

    @Query("select e from Exercise e where e.creator.id = ?#{principal.id} and e.id =:id")
    Exercise findOneByPrincipal(@Param("id") Long exerciseId);

    @Query(value = "SELECT e.id,e.name,e.creator_id FROM Exercise e JOIN WorkoutDetails wd ON e.id = wd.exercise_id AND wd.workout_id = ?1 AND e.id = ?2", nativeQuery = true)
    Exercise findExerciseByIdAndWorkoutId(Long workoutId, Long exercisesId);
}
