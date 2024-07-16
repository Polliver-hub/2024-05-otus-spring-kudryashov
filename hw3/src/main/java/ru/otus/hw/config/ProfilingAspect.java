package ru.otus.hw.config;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
@Slf4j
public class ProfilingAspect {

    @Around("@annotation(ru.otus.hw.config.annotations.Profiling)")
    public Object logBefore(ProceedingJoinPoint joinPoint) throws Throwable {
        long start = System.currentTimeMillis();
        try {
            log.info("Вызов метода {}", joinPoint.getSignature().getName());
            Object result = joinPoint.proceed();
            log.info("Вызов метода {} завершен за {} ms", joinPoint.getSignature().getName(),
                    System.currentTimeMillis() - start);
            return result;
        } catch (Throwable throwable) {
            log.error("Error executing method '{}': {}", joinPoint.getSignature().getName(), throwable.getMessage());
            throw throwable;
        }
    }
}
