package com.nzcs.statemachine.simple_choice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import reactor.core.publisher.Mono;

@SpringBootTest
public class StateMachineApplicationTests {

    @Autowired
    StateMachine<String, String> stateMachine;


    @Test
    void choice() {
        stateMachine
                .sendEvent(Mono.just(MessageBuilder
                        .withPayload("RUN").build()))
                .subscribe();

        stateMachine
                .sendEvent(Mono.just(MessageBuilder
                        .withPayload("CHOOSE").build()))
                .subscribe();
    }
}
