package com.nzcs.statemachine.distributed.redis;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.statemachine.StateContext;
import org.springframework.statemachine.StateMachineException;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.stereotype.Component;

import static org.springframework.statemachine.StateContext.Stage.TRANSITION_END;
import static org.springframework.statemachine.StateContext.Stage.TRANSITION_START;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisLockListener extends StateMachineListenerAdapter<String, String> {

    final RedisLockService lockService;
    final StateMachineRestoreService restoreService;


    @Override
    public void stateContext(StateContext<String, String> stateContext) {
        if (TRANSITION_START.equals(stateContext.getStage())) {
            if (!lockService.lock(stateContext.getStateMachine())) {
                throw new StateMachineException("Unable to lock state machine: " + stateContext.getStateMachine().getId());
            }
            restoreService.restoreIfPresent(stateContext.getStateMachine());
        }

        if (TRANSITION_END.equals(stateContext.getStage())) {
            lockService.unLock(stateContext.getStateMachine());
        }
    }

}

