package com.nzcs.statemachine.distributed;

import com.nzcs.statemachine.distributed.redis.RedisStateMachineRuntimePersister;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.statemachine.data.redis.RedisStateMachineRepository;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;

@Configuration
public class RedisConfig {

    @Bean
    public StateMachineRuntimePersister<String, String, Object> stateMachineRuntimePersister(RedisStateMachineRepository redisStateMachineRepository) {
        return new RedisStateMachineRuntimePersister(redisStateMachineRepository);
    }

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        return new RedissonConnectionFactory();
    }
}
