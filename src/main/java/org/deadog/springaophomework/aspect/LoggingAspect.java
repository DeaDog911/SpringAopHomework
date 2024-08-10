package org.deadog.springaophomework.aspect;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Aspect
@Component
public class LoggingAspect {
    private static Logger logger = LogManager.getLogger(LoggingAspect.class);

    @Pointcut("@within(org.deadog.springaophomework.annotations.Logging)")
    public void loggingPointcut() {}

    @Pointcut("loggingPointcut() && @annotation(org.deadog.springaophomework.annotations.Timer)")
    public void timerPointcut() {}

    @Before("loggingPointcut()")
    public void loggingBeforeAdvice(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        Object[] methodArgs = joinPoint.getArgs();
        logger.info("Выполняется метод: {} с параметрами: {}", methodName, Arrays.toString(methodArgs));
    }

    @AfterReturning(pointcut = "loggingPointcut() && execution(!void *.*(..))", returning = "result")
    public void loggingAfterReturningAdvice(JoinPoint joinPoint, Object result) {
        String methodName = joinPoint.getSignature().getName();
        logger.info("Метод {} успешно выполнен. Возвращаемое значение: {}", methodName, result);
    }

    @AfterReturning(pointcut = "loggingPointcut() && execution(void *.*(..))")
    public void loggingAfterVoidAdvice(JoinPoint joinPoint) {
        String methodName = joinPoint.getSignature().getName();
        logger.info("Метод {} успешно выполнен без возвращаемого значения", methodName);
    }

    @AfterThrowing(pointcut = "loggingPointcut()", throwing = "ex")
    public void loggingAfterThrowingAdvice(JoinPoint joinPoint, Throwable ex) {
        String methodName = joinPoint.getSignature().getName();
        logger.error("Метод {} выбросил исключение: {}", methodName, ex.getMessage());
    }

    @Around("timerPointcut()")
    public Object timerAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();

        String methodName = proceedingJoinPoint.getSignature().getName();
        logger.info("Время выполнения метода {} : {} мс", methodName, endTime - startTime);
        return result;
    }
}
