# Отчет Spring AOP Logging Example

## Описание проекта

Этот проект демонстрирует использование Spring AOP (Aspect-Oriented Programming) для логирования выполнения методов с помощью библиотеки Log4j2. Проект управляет сущностями пользователей и заказов, обеспечивая их создание, обновление, получение и удаление. Логирование реализовано с использованием аспектов, которые перехватывают вызовы методов и записывают информацию о выполнении, включая параметры методов, возвращаемые значения, выброс исключений и время выполнения. 

## Инструкции по запуску

### Клонирование репозитория

```bash
git clone https://github.com/username/repository.git
```

### База данных
Проект использует базу данных для хранения данных о пользователях и заказах. По умолчанию используется база данных PostgreSQL.
Для настройки подключения к базе данных, переименуйте файл `application.properties.origin` из директории `src/main/resources/` в `application.properties` и укажите в нем следующие параметры:

```
spring.datasource.url=jdbc:ваша_база_данных_url
spring.datasource.username=ваш_пользователь
spring.datasource.password=ваш_пароль
```

### Сборка и запуск приложения
Перейдите в директорию проекта и выполните следующие команды:

```bash
mvn install
mvn spring-boot:run
```

### Тестирование
Для запуска тестов выполните команду:

```bash
mvn test
```

## Настройка логирования с помощью Log4j2

Для настройки Log4j2 используется XML-конфигурационный файл. В данном приложении используется конфигурация, которая выводит логи как в консоль, так и в файлы.

### Описание
#### Appenders:
- Console: выводит логи в консоль.
- RollingFile: сохраняет логи в файл logs/app.log с ротацией по дате (ежедневно) и размеру (250 MB). 
#### Loggers:
- Root: основной логер, обрабатывающий все логи уровня INFO и выше, с выводом в консоль и файл.

## Примеры логов
Примеры логов, генерируемых аспектами при выполнении методов:

Логирование начала выполнения метода:
```
2024-08-11 13:06:02,745 [INFO] o.d.s.a.LoggingAspect [main] - Выполняется метод: create с параметрами: [1, Order(id=null, description=null, status=null)]
```

Логирование успешного выполнения метода и возврата значения:
```
2024-08-11 13:06:02,759 [INFO] o.d.s.a.LoggingAspect [main] - Метод create успешно выполнен. Возвращаемое значение: Order(id=null, description=null, status=null)
```

Логирование метода без возвращаемого значения:
```
2024-08-11 13:06:02,823 [INFO] o.d.s.a.LoggingAspect [main] - Метод deleteOrder успешно выполнен без возвращаемого значения
```

Логирование выброса исключения:
```
2024-08-11 13:06:02,863 [ERROR] o.d.s.a.LoggingAspect [main] - Метод create выбросил исключение: User not found
```

Логирование времени выполнения метода:
```
2024-08-11 13:06:02,964 [INFO] o.d.s.a.LoggingAspect [main] - Время выполнения метода getUserWithOrders : 1 мс
```

## Примеры тестов
Тест успешного создания заказа:

```java
@Test
void create_shouldCreateOrder_whenUserExists() {
    User user = new User();
    user.setId(1L);

    Order order = new Order();
    when(userRepository.findById(1L)).thenReturn(Optional.of(user));
    when(orderRepository.save(order)).thenReturn(order);

    Order createdOrder = orderService.create(1L, order);

    assertNotNull(createdOrder);
    assertEquals(user, createdOrder.getUser());
    verify(orderRepository, times(1)).save(order);
}
```

Тест выброса исключения при попытке создания заказа для несуществующего пользователя:
```java 
@Test
void create_shouldThrowException_whenUserDoesNotExist() {
    when(userRepository.findById(1L)).thenReturn(Optional.empty());

    ApplicationException exception = assertThrows(ApplicationException.class, () -> orderService.create(1L, new Order()));
    assertEquals("User not found", exception.getMessage());
}
```

Тест получения всех заказов:
```java 
@Test
void getAllOrders_shouldReturnAllOrders() {
    Order order = new Order();
    when(orderRepository.findAll()).thenReturn(Collections.singletonList(order));

    assertEquals(1, orderService.getAllOrders().size());
}
```

Тест обновления заказа:
```java
@Test
void updateOrder_shouldUpdateOrder_whenOrderExists() {
    Order order = new Order();
    Order updatedDetails = new Order();
    updatedDetails.setDescription("New Description");
    updatedDetails.setStatus("Updated Status");

    when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
    when(orderRepository.save(order)).thenReturn(order);

    Order updatedOrder = orderService.updateOrder(1L, updatedDetails);

    assertNotNull(updatedOrder);
    assertEquals("New Description", updatedOrder.getDescription());
    assertEquals("Updated Status", updatedOrder.getStatus());
}

```



