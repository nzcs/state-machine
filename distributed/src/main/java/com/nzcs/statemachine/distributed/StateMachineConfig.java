package com.nzcs.statemachine.distributed;

import com.nzcs.statemachine.common.LogListener;
import com.nzcs.statemachine.distributed.redis.RedisLockListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachineFactory;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;


@Configuration
@EnableStateMachineFactory
@RequiredArgsConstructor
public class StateMachineConfig extends StateMachineConfigurerAdapter<String, String> {

    final LogListener logListener;
    final RedisLockListener redisLockListener;
    final StateMachineRuntimePersister<String, String, Object> persister;


    @Override
    public void configure(StateMachineConfigurationConfigurer<String, String> config) throws Exception {
        config
                .withConfiguration()
                .autoStartup(true)
                .listener(logListener)
                .listener(redisLockListener)
                .and()
                .withPersistence()
                .runtimePersister(persister);
        ;
    }

    @Override
    public void configure(StateMachineStateConfigurer<String, String> states) throws Exception {
        states
                .withStates()
                .initial("START")
                .state("S1")
                .state("S2")
                .state("S3")
                .state("S4")
                .state("S5");
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<String, String> transitions) throws Exception {
        transitions
                .withExternal()
                .source("START").target("S1")
                .event("E1")

                .and()

                .withExternal()
                .source("S1").target("S2")
                .event("E2")

                .and()

                .withExternal()
                .source("S2").target("S3")
                .event("E3")

                .and()

                .withExternal()
                .source("S3").target("S4")
                .event("E4")

                .and()

                .withExternal()
                .source("S4").target("S5")
                .event("E5");
    }
}
