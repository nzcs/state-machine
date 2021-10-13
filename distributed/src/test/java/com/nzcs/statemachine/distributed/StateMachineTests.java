package com.nzcs.statemachine.distributed;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.service.StateMachineService;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
public class StateMachineTests {

    @Autowired
    StateMachineService<String, String> stateMachineService;
    static RedissonClient client;


    @BeforeAll
    public static void setUp() {
        client = Redisson.create();
        client.getKeys().flushdb();
    }


    @AfterEach
    public void flush() {
        client.getKeys().getKeysStream().forEach(System.out::println);
        client.getKeys().flushdb();
    }


    @Test
    void test() {

        System.out.println("--- send A E1 ---------------");
        StateMachine<String, String> a = stateMachineService.acquireStateMachine("A");
        a.sendEvent(Mono.just(MessageBuilder
                        .withPayload("E1").build()))
                .blockLast();


        System.out.println("--- send B E1 ---------------");
        StateMachine<String, String> b = stateMachineService.acquireStateMachine("B");
        b.sendEvent(Mono.just(MessageBuilder
                        .withPayload("E1").build()))
                .blockLast();


        System.out.println("--- send A E2 ---------------");
        a.sendEvent(Mono.just(MessageBuilder
                        .withPayload("E2").build()))
                .blockLast();


        assertEquals("S2", a.getState().getId());
        assertEquals("S1", b.getState().getId());
    }
}
