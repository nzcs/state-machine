package com.nzcs.statemachine.distributed;

import lombok.RequiredArgsConstructor;
import org.redisson.api.RedissonClient;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.service.StateMachineService;
import org.springframework.statemachine.state.State;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

@ShellComponent
@RequiredArgsConstructor
public class StateMachineCommands {

    final StateMachineService<String, String> stateMachineService;
    final StateMachineRuntimePersister<String, String, Object> persister;
    final RedissonClient client;


    @ShellMethod("List DB")
    public String clear_db() {
        client.getKeys().flushdb();
        return "Clear";
    }

    @ShellMethod("List DB")
    public String list_db() {
        return client.getKeys().getKeysStream().collect(Collectors.joining("\n"));
    }

    @ShellMethod("get SM")
    public String sm(String machineId) throws Exception {
        return "" + persister.read(machineId);
    }


    @ShellMethod("Send event")
    public String send(String machineId, String event) {
        stateMachineService.acquireStateMachine(machineId)
                .sendEvent(Mono.just(MessageBuilder
                        .withPayload(event).build()))
                .subscribe();
        return "Event " + event + " send";
    }

    @ShellMethod("Get current state")
    public String state(String machineId) {
        State<String, String> state = stateMachineService.acquireStateMachine(machineId).getState();
        return state != null ? state.getId() : "No state";
    }
}
