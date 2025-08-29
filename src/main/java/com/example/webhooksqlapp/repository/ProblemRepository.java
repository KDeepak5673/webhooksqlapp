package com.example.webhooksqlapp.repository;

import com.example.webhooksqlapp.model.Problem;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

// CrudRepository provides basic CRUD (Create, Read, Update, Delete) operations
// for the Problem entity.
@Repository
public interface ProblemRepository extends CrudRepository<Problem, Long> {
}