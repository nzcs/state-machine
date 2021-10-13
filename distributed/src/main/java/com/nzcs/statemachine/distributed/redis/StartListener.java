package com.nzcs.statemachine.distributed.redis;

import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;

import java.util.concurrent.CountDownLatch;


public class StartListener extends StateMachineListenerAdapter<String, String> {

    final CountDownLatch latch = new CountDownLatch(1);
    final StateMachine<String, String> stateMachine;

    public StartListener(StateMachine<String, String> stateMachine) {
        this.stateMachine = stateMachine;
    }

    @Override
    public void stateMachineStarted(StateMachine<String, String> stateMachine) {
        this.stateMachine.removeStateListener(this);
        latch.countDown();
    }
}
