package com.atom.matchmaker.repositories;

import com.atom.models.Player;
import com.atom.models.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionJPARepository extends JpaRepository<Session, Long> {
}
