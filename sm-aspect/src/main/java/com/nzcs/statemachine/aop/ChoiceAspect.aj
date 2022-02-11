package com.nzcs.statemachine.aop;

public aspect ChoiceAspect {

//    pointcut callExecute(StateContext<String, String> context, ActionB actionB):
//            call(boolean com.nzcs.statemachine.simple_choice.ActionB.execute(StateContext<String, String>)) && args(context) && target(actionB);

//    void around(StateContext<String, String> context, ActionB actionB) : callExecute(amount, account) {
////        if (account.balance < amount) {
////            logger.info("Withdrawal Rejected!");
////            return false;
////        }
//        System.out.println("pocs");
//        proceed(context, actionB);
//    }
}