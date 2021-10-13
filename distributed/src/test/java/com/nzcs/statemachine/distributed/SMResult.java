package com.nzcs.statemachine.distributed;

import lombok.Getter;
import org.assertj.core.util.Lists;
import org.springframework.statemachine.StateMachineEventResult;
import org.springframework.statemachine.StateMachineEventResult.ResultType;

import java.util.function.Function;


@Getter
public class SMResult {

    final String id;
    final ResultType resultType;
    final String state;

    public SMResult(StateMachineEventResult<String, String> result) {
        this.resultType = result.getResultType();
        this.id = result.getRegion().getId();
        this.state = result.getRegion().getState().getId();
    }


    public static Function[] extractors() {
        return Lists.<Function<? extends SMResult, ?>>newArrayList(
                SMResult::getId,
                SMResult::getResultType,
                SMResult::getState
        ).toArray(new Function[0]);
    }
}
