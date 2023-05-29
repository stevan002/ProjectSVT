package com.example.projectsvt.repository;

import com.example.projectsvt.model.React;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionRepository extends JpaRepository<React, Long> {
}
