package com.atom.matchmaker.repositories;

import com.atom.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PlayersRepository extends JpaRepository<Player, Long> {
    Optional<Player> findByUsername(String username);
}
