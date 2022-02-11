package com.nzcs.statemachine.simple_choice;

import org.springframework.context.ApplicationListener;
import org.springframework.statemachine.event.OnStateMachineError;
import org.springframework.stereotype.Component;

@Component
public class ErrorApplicationEventListener implements ApplicationListener<OnStateMachineError> {

    @Override
    public void onApplicationEvent(OnStateMachineError event) {
        System.out.println("OnStateMachineError");
    }
}
