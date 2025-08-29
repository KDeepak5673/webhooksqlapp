package com.example.webhooksqlapp.repository;

import com.example.webhooksqlapp.model.Solution;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolutionRepository extends CrudRepository<Solution, Long> {
}