package com.nzcs.statemachine.distributed.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.Lifecycle;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineException;
import org.springframework.statemachine.config.StateMachineFactory;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
@Component
public class RedisStateMachineService implements StateMachineService<String, String>, DisposableBean {

    final RedisLockService lockService;
    final StateMachineRestoreService restoreService;
    final StateMachineFactory<String, String> stateMachineFactory;
    final Map<String, StateMachine<String, String>> machines = new ConcurrentHashMap<>();


    @Override
    public final void destroy() {
        doStop();
    }

    void doStop() {
        log.info("Entering stop sequence, stopping all managed machines");
        synchronized (machines) {
            List<String> machineIds = new ArrayList<>(machines.keySet());
            for (String machineId : machineIds) {
                releaseStateMachine(machineId, true);
            }
        }
    }

    @Override
    public void releaseStateMachine(String machineId) {
        log.info("Releasing machine with id " + machineId);
        synchronized (machines) {
            StateMachine<String, String> stateMachine = machines.remove(machineId);
            if (stateMachine != null) {
                log.info("Found machine with id " + machineId);
                stateMachine.stopReactively().block();
            }
        }
    }

    @Override
    public void releaseStateMachine(String machineId, boolean stop) {
        log.info("Releasing machine with id " + machineId);
        synchronized (machines) {
            StateMachine<String, String> stateMachine = machines.remove(machineId);
            if (stateMachine != null) {
                log.info("Found machine with id " + machineId);
                handleStop(stateMachine, stop);
            }
        }
    }

    void handleStop(StateMachine<String, String> stateMachine, boolean stop) {
        if (stop) {
            if (((Lifecycle) stateMachine).isRunning()) {
                StopListener listener = new StopListener(stateMachine);
                stateMachine.addStateListener(listener);
                stateMachine.stopReactively().block();
                try {
                    listener.latch.await();
                } catch (InterruptedException ignored) {
                }
            }
        }
    }


    @Override
    public StateMachine<String, String> acquireStateMachine(String machineId) {
        return acquireStateMachine(machineId, true);
    }

    @Override
    public StateMachine<String, String> acquireStateMachine(String machineId, boolean start) {
        log.info("Acquiring machine with id {}", machineId);

        StateMachine<String, String> stateMachine = null;

        try {
            if (!lockService.lock(machineId)) {
                throw new StateMachineException("Unable to lock state machine: " + machineId);
            }

            stateMachine = machines.get(machineId);
            stateMachine = restoreService.restoreIfAbsent(machineId, stateMachine,
                    (context) -> {
                        if (context == null) {
                            return stateMachineFactory.getStateMachine(machineId);
                        } else {
                            return stateMachineFactory.getStateMachine(RedisStateMachineRuntimePersister.ID);
                        }
                    });

            machines.put(machineId, stateMachine);

        } finally {
            lockService.unLock(stateMachine);
        }

        return handleStart(stateMachine, start);
    }


    StateMachine<String, String> handleStart(StateMachine<String, String> stateMachine, boolean start) {
        if (start) {
            if (!((Lifecycle) stateMachine).isRunning()) {
                StartListener listener = new StartListener(stateMachine);
                stateMachine.addStateListener(listener);
                stateMachine.startReactively().block();
                try {
                    listener.latch.await();
                } catch (InterruptedException ignored) {
                }
            }
        }
        return stateMachine;
    }
}
