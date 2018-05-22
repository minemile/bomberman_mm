package com.atom.matchmaker.repositories;

import com.atom.models.GameSession;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;

@Repository
@Data
public class GameRepository {
    ConcurrentHashMap<Integer, GameSession> games = new ConcurrentHashMap<>();
}
