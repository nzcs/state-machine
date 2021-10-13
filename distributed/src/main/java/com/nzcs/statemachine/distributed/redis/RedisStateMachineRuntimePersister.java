package com.nzcs.statemachine.distributed.redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.data.redis.RedisPersistingStateMachineInterceptor;
import org.springframework.statemachine.data.redis.RedisStateMachineRepository;
import org.springframework.statemachine.state.State;
import org.springframework.statemachine.transition.Transition;

import java.util.UUID;


@Slf4j
public class RedisStateMachineRuntimePersister extends RedisPersistingStateMachineInterceptor<String, String, Object> {

    static final UUID ID = UUID.randomUUID();

    public RedisStateMachineRuntimePersister(RedisStateMachineRepository redisStateMachineRepository) {
        super(redisStateMachineRepository);
    }

    @Override
    public void postStateChange(State<String, String> state, Message<String> message, Transition<String, String> transition, StateMachine<String, String> stateMachine, StateMachine<String, String> rootStateMachine) {
        if (!stateMachine.getUuid().equals(ID)) {
            log.debug("Persist state machine: {}", stateMachine.getId());
            super.postStateChange(state, message, transition, stateMachine, rootStateMachine);
        }
    }
}
