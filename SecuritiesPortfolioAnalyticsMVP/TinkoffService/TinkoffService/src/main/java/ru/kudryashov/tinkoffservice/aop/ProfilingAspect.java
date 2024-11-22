package ru.kudryashov.tinkoffservice.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ProfilingAspect {

    @Around("@annotation(ru.kudryashov.tinkoffservice.annotation.Profiled)")
    public Object profileMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        Object result;
        try {
            result = joinPoint.proceed();
        } finally {
            long duration = System.currentTimeMillis() - start;
            log.info("Method [{}] executed in {} ms", joinPoint.getSignature(), duration);
        }
        return result;
    }
}
