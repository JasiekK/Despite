package com.despite.repository;

import com.despite.entities.Exercise;
import com.despite.entities.WorkoutDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkoutDetailsRepository extends JpaRepository<WorkoutDetails, Long> {


}
