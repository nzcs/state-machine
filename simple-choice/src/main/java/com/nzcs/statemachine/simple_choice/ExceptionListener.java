package com.nzcs.statemachine.simple_choice;

import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.listener.StateMachineListenerAdapter;
import org.springframework.stereotype.Component;

@Component
public class ExceptionListener extends StateMachineListenerAdapter<String, String> {

    @Override
    public void stateMachineError(StateMachine<String, String> stateMachine, Exception exception) {
        System.out.println("stateMachineError");
        throw new RuntimeException(exception);
    }
}
