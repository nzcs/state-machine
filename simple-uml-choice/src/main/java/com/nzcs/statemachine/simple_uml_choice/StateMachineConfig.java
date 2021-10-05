package com.nzcs.statemachine.simple_uml_choice;

import com.nzcs.statemachine.common.StateMachineListener;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineConfigurationConfigurer;
import org.springframework.statemachine.config.builders.StateMachineModelConfigurer;
import org.springframework.statemachine.config.model.StateMachineModelFactory;
import org.springframework.statemachine.uml.UmlStateMachineModelFactory;


@Configuration
@EnableStateMachine
@RequiredArgsConstructor
public class StateMachineConfig extends StateMachineConfigurerAdapter<String, String> {

    final StateMachineListener listener;


    @Override
    public void configure(StateMachineConfigurationConfigurer<String, String> config) throws Exception {
        config.withConfiguration()
                .autoStartup(true)
                .listener(listener);
    }

    @Override
    public void configure(StateMachineModelConfigurer<String, String> model) throws Exception {
        model
                .withModel()
                .factory(modelFactory());
    }


    @Bean
    public StateMachineModelFactory<String, String> modelFactory() {
        return new UmlStateMachineModelFactory("classpath:uml/simple-choice.uml");
    }
}
