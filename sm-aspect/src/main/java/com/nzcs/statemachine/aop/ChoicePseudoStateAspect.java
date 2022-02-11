package com.nzcs.statemachine.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

@Aspect
//@Component
public class ChoicePseudoStateAspect {

//    @Pointcut("within(org.springframework.statemachine.state..*)")
//    public void choicePseudoStateClass() {
//    }

//    @Pointcut("execution(public * *(..))")
//    public void entryMethods() {
//    }

//    @Around("choicePseudoStateClass()")
//    public Object measureMethodExecutionTime(ProceedingJoinPoint pjp) throws Throwable {
//        System.out.println();
//        return null;
//    }


    @Pointcut("within(org.springframework.statemachine.state..*) && execution(* org.springframework.statemachine.state.ChoicePseudoState.*(..))")
    public void entryMethods() {
    }

    @AfterReturning(value = "entryMethods()", returning = "entity")
    public void logMethodCall(JoinPoint jp, Object entity) throws Throwable {
        System.out.println();
    }


    //    @Pointcut("within(org.springframework..*) && execution(* org.springframework.statemachine.state.ChoicePseudoState.*(..))")
    @Pointcut("within(com.nzcs..*) && execution(* com.nzcs.statemachine.simple_choice.ActionB.*(..))")
    public void repositoryClassMethods() {
    }

    @Around("repositoryClassMethods()")
    public Object measureMethodExecutionTime(ProceedingJoinPoint pjp) throws Throwable {
        System.out.println("pocs: aspect");
        return pjp.proceed();
    }
}
