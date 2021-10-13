package com.nzcs.statemachine.distributed;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.service.StateMachineService;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static org.mockito.Mockito.*;
import static org.springframework.statemachine.StateMachineEventResult.ResultType.ACCEPTED;


@SuppressWarnings("unchecked")
@SpringBootTest
public class LockTests {

    @SpyBean
    StateMachineRuntimePersister<String, String, Object> persister;
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
    void testLock() throws Exception {
        CountDownLatch latch = new CountDownLatch(2);
        List<Future<StateMachineEventResult<String, String>>> futures = new ArrayList<>();
        List<SMResult> results = new ArrayList<>();
        ExecutorService service = Executors.newFixedThreadPool(10);

        futures.add(
                service.submit(
                        () -> {
                            try {
                                StateMachineEventResult<String, String> result =
                                        stateMachineService.acquireStateMachine("A")
                                                .sendEvent(Mono.just(MessageBuilder
                                                        .withPayload("E1").build()))
                                                .blockLast();
                                System.out.println("AA");
                                return result;
                            } finally {
                                latch.countDown();
                            }
                        }
                )
        );

        futures.add(
                service.submit(
                        () -> {
                            try {
                                StateMachineEventResult<String, String> result =
                                        stateMachineService.acquireStateMachine("A")
                                                .sendEvent(Mono.just(MessageBuilder
                                                        .withPayload("E1").build()))
                                                .blockLast();
                                System.out.println("BB");
                                return result;
                            } finally {
                                latch.countDown();
                            }
                        }
                )
        );

        latch.await();


        for (Future<StateMachineEventResult<String, String>> f : futures) {
            results.add(new SMResult(f.get()));
        }

        verify(persister, times(2)).write(any(), any());

        assertThat(results)
                .extracting(SMResult.extractors())
                .containsExactlyInAnyOrder(
                        tuple("A", ACCEPTED, "S1"),
                        tuple("A", ACCEPTED, "S1")
                );
    }


    @Test
    void testSendEventPseudoLock() throws Exception {
        CountDownLatch latch = new CountDownLatch(2);
        List<Future<StateMachineEventResult<String, String>>> futures = new ArrayList<>();
        List<SMResult> results = new ArrayList<>();
        ExecutorService service = Executors.newFixedThreadPool(10);

        StateMachine<String, String> a = stateMachineService.acquireStateMachine("A");


        futures.add(
                service.submit(
                        () -> {
                            StateMachineEventResult<String, String> result =
                                    a.sendEvent(Mono.just(MessageBuilder
                                                    .withPayload("E1").build()))
                                            .blockLast();

                            latch.countDown();
                            return result;
                        }
                )
        );

        futures.add(
                service.submit(
                        () -> {
                            StateMachineEventResult<String, String> result =
                                    a.sendEvent(Mono.just(MessageBuilder
                                                    .withPayload("E1").build()))
                                            .blockLast();

                            latch.countDown();
                            return result;
                        }
                )
        );

        latch.await();


        for (Future<StateMachineEventResult<String, String>> f : futures) {
            results.add(new SMResult(f.get()));
        }

        verify(persister, times(2)).write(any(), any());

        assertThat(results)
                .extracting(SMResult.extractors())
                .containsExactlyInAnyOrder(
                        tuple("A", ACCEPTED, "S1"),
                        tuple("A", ACCEPTED, "S1")
                );
    }
}
