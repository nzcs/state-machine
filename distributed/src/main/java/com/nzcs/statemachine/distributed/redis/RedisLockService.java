package com.nzcs.statemachine.distributed.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.statemachine.StateMachine;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;


@Slf4j
@Component
@RequiredArgsConstructor
public class RedisLockService {

    final static String LOCK_PREFIX = "sm_lock:";
    final static int LOCK_AT_MOST_UNTIL = 10;
    final RedissonClient redissonClient;
    final Map<String, RLock> locks = new ConcurrentHashMap<>();
    final Map<String, Integer> lockNumbers = new ConcurrentHashMap<>();


    public boolean lock(StateMachine<String, String> stateMachine) {
        return lock(stateMachine.getId());
    }

    public boolean lock(String machineId) {
        String id = buildLockId(machineId);

        if (this.lockNumbers.compute(id + Thread.currentThread().getId(), (s, i) -> i == null ? 0 : ++i) > 1) {
            return true;
        }

        RLock lock = this.redissonClient.getLock(id);
        boolean result = false;
        try {
            result = lock.tryLock(0, LOCK_AT_MOST_UNTIL, TimeUnit.SECONDS);
            log.debug("Lock acquired for state machine with id {}: {}", id, result);
            this.locks.put(id, lock);
            this.lockNumbers.put(id + Thread.currentThread().getId(), 1);
        } catch (InterruptedException e) {
            log.warn("Cannot acquire lock for state machine with id {}", id);
        }
        return result;
    }

    public void unLock(StateMachine<String, String> stateMachine) {
        if (stateMachine == null) {
            return;
        }
        String id = buildLockId(stateMachine.getId());
        Integer n = this.lockNumbers.computeIfPresent(id + Thread.currentThread().getId(), (s, i) -> --i);
        if (n != null && n == 0) {
            RLock lock = locks.remove(id);
            if (lock != null) {
                log.debug("Unlock on {}", id);
                lock.unlock();
            }
        }
    }


    private String buildLockId(String machineId) {
        return LOCK_PREFIX + machineId;
    }
}
