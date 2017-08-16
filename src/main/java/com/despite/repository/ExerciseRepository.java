package com.despite.repository;

import com.despite.entities.Exercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    @Query("select e from Exercise e where e.creator.id = ?#{principal.id}")
    List<Exercise> findAllByPrincipal();
}
