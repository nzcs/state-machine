package com.nzcs.statemachine.distributed.redis;

import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;

import java.util.concurrent.CountDownLatch;

public class StopListener extends StateMachineListenerAdapter<String, String> {

    final CountDownLatch latch = new CountDownLatch(1);
    final StateMachine<String, String> stateMachine;

    public StopListener(StateMachine<String, String> stateMachine) {
        this.stateMachine = stateMachine;
    }

    @Override
    public void stateMachineStopped(StateMachine<String, String> stateMachine) {
        this.stateMachine.removeStateListener(this);
        latch.countDown();
    }
}
