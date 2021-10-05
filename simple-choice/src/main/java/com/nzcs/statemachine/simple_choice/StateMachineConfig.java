package com.nzcs.statemachine.simple_choice;

import com.nzcs.statemachine.common.StateMachineListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

@Configuration
@EnableStateMachine
@RequiredArgsConstructor
public class StateMachineConfig extends StateMachineConfigurerAdapter<String, String> {

    final StateMachineListener listener;
    final ChoiceAction choiceAction;
    final GuardToA guardToA;
    final GuardToB guardToB;


    @Override
    public void configure(StateMachineConfigurationConfigurer<String, String> config) throws Exception {
        config.withConfiguration()
                .autoStartup(true)
                .listener(listener);
    }

    @Override
    public void configure(StateMachineStateConfigurer<String, String> states) throws Exception {
        states
                .withStates()
                .initial("START")
                .state("STEP_1")
                .choice("CHOICE")
                .state("A")
                .state("B")
                .state("C")
                .state("END");
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<String, String> transitions) throws Exception {
        transitions
                .withExternal()
                .name("RUN")
                .source("START").target("STEP_1")
                .event("RUN")

                .and()

                .withExternal()
                .name("CHOOSE")
                .source("STEP_1").target("CHOICE")
                .action(choiceAction)
                .event("CHOOSE")

                .and()

                .withChoice()
                .source("CHOICE")
                .first("A", guardToA)
                .then("B", guardToB)
                .last("C");
    }
}
