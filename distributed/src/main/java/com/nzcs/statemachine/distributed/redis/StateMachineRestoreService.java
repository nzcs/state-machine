package com.nzcs.statemachine.distributed.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.StateMachineException;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Function;

@Slf4j
@Component
@RequiredArgsConstructor
public class StateMachineRestoreService {

    final StateMachineRuntimePersister<String, String, Object> persister;


    public StateMachine<String, String> restoreIfAbsent(String machineId, StateMachine<String, String> stateMachine, Function<StateMachineContext<String, String>, StateMachine<String, String>> stateMachineFactory) {

        StateMachineContext<String, String> stateMachineContext;
        try {
            stateMachineContext = persister.read(machineId);
        } catch (Exception e) {
            throw new StateMachineException("Unable to read context from store: " + machineId, e);
        }

        if (stateMachine == null || stateMachineContext == null) {
            log.info("Getting new machine from factory with id " + machineId);
            if (stateMachineFactory != null) {
                stateMachine = stateMachineFactory.apply(stateMachineContext);
            }
        }

        restore(stateMachine, stateMachineContext);

        return stateMachine;
    }


    public void restoreIfPresent(StateMachine<String, String> stateMachine) {
        Objects.requireNonNull(stateMachine);

        String machineId = stateMachine.getId();

        StateMachineContext<String, String> stateMachineContext;
        try {
            stateMachineContext = persister.read(machineId);
        } catch (Exception e) {
            throw new StateMachineException("Unable to read context from store: " + machineId, e);
        }

        restore(stateMachine, stateMachineContext);
    }


    private void restore(StateMachine<String, String> stateMachine, StateMachineContext<String, String> stateMachineContext) {
        if (stateMachineContext == null || stateMachine.getState().getId().equals(stateMachineContext.getState())) {
            return;
        }
        log.info("Restore state machine: " + stateMachine.getId());
        stateMachine.stopReactively().block();
        stateMachine.getStateMachineAccessor().doWithAllRegions(function -> function.resetStateMachineReactively(stateMachineContext).block());
    }
}
