# Документация

## Введение

В этой документации описан подход к реализации логирования в приложении с использованием аспектов.

## Инфраструктура логирования

### Аспект `LoggingAspect`

Аспект `LoggingAspect` отвечает за перехват вызовов методов в классах, помеченных аннотацией `@Logging`, и выполняет следующие задачи:

- Логирование вызовов методов и их аргументов до выполнения метода.
- Логирование результата выполнения метода или его отсутствия (если метод `void`).
- Логирование выброшенных исключений.
- Измерение времени выполнения методов, помеченных аннотацией `@Timer`.

#### Пример использования

Чтобы использовать логирование в вашем классе, достаточно аннотировать его `@Logging`. Если вы хотите измерять время выполнения конкретного метода, используйте аннотацию `@Timer`.

```java
@Service
@Logging
public class OrderService {
    // Методы класса будут автоматически логироваться
}

@Service
@Logging
public class UserService {
    @Timer
    public User getUserWithOrders(Long id) {
        // Время выполнения этого метода будет измерено и залогировано
    }
}
```

## Реализация `LoggingAspect`

### Определение поинткатов

- `@Pointcut("@within(org.deadog.springaophomework.annotations.Logging)")` - поинткат для перехвата всех методов в классах, аннотированных `@Logging`.
- `@Pointcut("loggingPointcut() && @annotation(org.deadog.springaophomework.annotations.Timer)")` - поинткат для методов, аннотированных `@Timer`, которые дополнительно логируют время выполнения метода.

### Адвайсы

- `@Before("loggingPointcut()")` - Логирование перед выполнением метода. Записывает информацию о вызове метода и его параметрах.

    ```java
    @Before("loggingPointcut()")
    public void loggingBeforeAdvice(JoinPoint joinPoint) {
      String methodName = joinPoint.getSignature().getName();
      Object[] methodArgs = joinPoint.getArgs();
      logger.info("Выполняется метод: {} с параметрами: {}", methodName, Arrays.toString(methodArgs));
    }
  ```

- `@AfterReturning` - Логирование после успешного выполнения метода. Логирует возвращаемое значение или факт успешного завершения, если метод не возвращает значение.

    ```java
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
  ```

- `@AfterThrowing("loggingPointcut()", throwing = "ex")` - Логирование исключений. Записывает информацию о выбросе исключения.

    ```java 
    @AfterThrowing(pointcut = "loggingPointcut()", throwing = "ex")
    public void loggingAfterThrowingAdvice(JoinPoint joinPoint, Throwable ex) {
        String methodName = joinPoint.getSignature().getName();
        logger.error("Метод {} выбросил исключение: {}", methodName, ex.getMessage());
    }
    ```
- `@Around("timerPointcut()")` - Логирование времени выполнения метода. Записывает время, затраченное на выполнение метода.

    ```java 
    @Around("timerPointcut()")
    public Object timerAroundAdvice(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        Object result = proceedingJoinPoint.proceed();
        long endTime = System.currentTimeMillis();

        String methodName = proceedingJoinPoint.getSignature().getName();
        logger.info("Время выполнения метода {} : {} мс", methodName, endTime - startTime);
        return result;
    }
    ```
    
