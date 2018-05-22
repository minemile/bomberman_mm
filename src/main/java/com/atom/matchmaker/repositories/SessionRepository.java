package com.atom.matchmaker.repositories;

import com.atom.models.Session;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.concurrent.ConcurrentHashMap;

@Repository
@Data
public class SessionRepository {
    ConcurrentHashMap<Integer, Session> sessions = new ConcurrentHashMap<>();
}
