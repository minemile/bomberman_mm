package com.atom.matchmaker.repositories;

import com.atom.matchmaker.models.Session;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingQueue;

@Repository
@Data
public class SessionRepository {
    ConcurrentHashMap<Integer, Session> sessions = new ConcurrentHashMap<>();
}
