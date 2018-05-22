package com.atom.matchmaker.repositories;

import com.atom.matchmaker.models.Session;
import lombok.Data;
import org.springframework.stereotype.Repository;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Repository
@Data
public class IdleSessions {
    BlockingQueue<Session> sessions = new LinkedBlockingQueue<>();
}
