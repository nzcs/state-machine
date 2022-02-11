package com.nzcs.statemachine.simple_choice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.config.StateMachineFactory;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class StateMachineTests {

    @Autowired
    StateMachineFactory<String, String> factory;


    @Test
    void choice() throws InterruptedException {
        StateMachine<String, String> a = factory.getStateMachine("A");
        a.sendEvent(Mono.just(MessageBuilder
                        .withPayload("RUN").build()))
                .blockLast();

        StateMachine<String, String> b = factory.getStateMachine("B");
        b.sendEvent(Mono.just(MessageBuilder
                        .withPayload("RUN").build()))
                .blockLast();

        a.sendEvent(Mono.just(MessageBuilder
                        .withPayload("CHOOSE").setHeader("pocs", "abc").build()))
                .blockLast();


        assertEquals("B2", a.getState().getId());
        assertEquals("STEP_1", b.getState().getId());
    }

}
