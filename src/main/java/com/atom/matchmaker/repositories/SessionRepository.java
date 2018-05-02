package com.atom.matchmaker.repositories;

import com.atom.matchmaker.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {
}
