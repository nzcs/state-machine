package com.nzcs.statemachine.simple_uml_choice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import reactor.core.publisher.Mono;

@SpringBootTest
public class StateMachineTests {

    @Autowired
    StateMachine<String, String> stateMachine;


    @Test
    void uml_choice() {
        stateMachine
                .sendEvent(Mono.just(MessageBuilder
                        .withPayload("E1").build()))
                .subscribe();
    }
}
