package com.nzcs.statemachine.simple_uml_choice;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class StateMachineTests {

    @Autowired
    StateMachine<String, String> stateMachine;


    @Test
    void uml_choice() {
        StateMachineEventResult<String, String> result = stateMachine
                .sendEvent(Mono.just(MessageBuilder
                        .withPayload("E1").build()))
                .blockLast();

        assert result != null;
        assertEquals("B", result.getRegion().getState().getId());
    }
}
