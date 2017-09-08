package com.despite.repository;

import com.despite.entities.Exercise;
import com.despite.entities.Workout;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout, Long> {

    @Query("select w from Workout w where w.creator.id = ?#{principal.id}")
    List<Workout> findAllByPrincipal();

    void deleteByIdAndCreatorId(Long Id, Long creatorId);
}
