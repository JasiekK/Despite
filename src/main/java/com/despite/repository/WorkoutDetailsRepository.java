package com.despite.repository;

import com.despite.entities.WorkoutDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkoutDetailsRepository extends JpaRepository<WorkoutDetails, Long> {


}
