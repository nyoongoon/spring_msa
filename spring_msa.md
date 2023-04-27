# 스프링 이벤트
- 애플리케이션 내부에서 이벤트를 게시하고 구독할 목적으로 사용
- ApplicationContext는 메시지 큐처럼 이벤트를 전달하는 역할을 함.
- 객체와 객체 사이에 이벤트 메시지를 전달
- 멀티스레드로 이벤트 구독 처리 가능

## 스프링 이벤트 장점
- 이벤트 게시 클래스와 이벤트 구독 클래스의 의존 관계를 분리
- 비동기로 처리 가능
- 게시된 하나의 이벤트를 여러개의 구독 클래스가 수신 가능.
- 트랜잭션 효율적으로 사용

## 예시
- 기존 방식 (서비스 중첩)
```java
@Service
public class UserService{
    @Autowired
    private EventService eventService;
    public Boolean createUser(String userName, String emailAddress){
        // 사용자 생성 로직 전략
        eventService.sendEventMail(emailAddress);
        return Boolean.TRUE;
    }
}
```
- 두 서비스 클래스는 느슨하게 결합되어 있지만 기능적 측면에서 복잡도는 증가.
### 비동기처리
- 스레드 풀을 스프링 이벤트에 적용하면 이벤트 처리 부분을 비동기로 동작 가능
#### 트랜잭션
- 비동기처리로 트랜잭션 효과적으로 사용가능
- 비즈니스 로직상 하나의 트랜잭션을 분리하여 비동기처리한다면 리소스 효율적 사용 가능.
- sendEventMail() 메서드에 적용된 트랜잭션도 정교하게 사용 가능
- ApplicationContext는 트랜잭션 종료 시점을 여러 단계로 구분하고 정의된 단계에서 이벤트를 처리하는 기능을 제공

## 사용자 정의 이벤트 처리
- 개발자가 직접 정의란 이벤트 메시지를 게시하고 구독하려면 다음 세 개의 클래스가 필요
### 이벤트 메시지 클래스
- 게시 클래스와 구독 클래스 사이에 공유할 데이터를 포함하는 클래스
### 게시 클래스
- 구독 클래스에 전달할 이벤트 메시지를 생산하고 게시하는 클래스
- ApplicationContext 이벤트 메시지를 전달하는 과정을 게시하고 함
### 구독 클래스
- ApplicationContext에서 전달받은 이벤트 메시지를 구독하고 부가기능을 실행하는 클래스
#### 예시
```java
@Getter
public class UserEvent extends ApplicationEvent {
    private Type type;
    private Long userId;
    private String emailAddress;

    public UserEvent(Object source, Type type, Long userId, String emailAddress) {
        super(source);
        this.type = type;
        this.userId = userId;
        this.emailAddress = emailAddress;
    }

    public static UserEvent created(Object source, Long userId, String emailAddress){
        return new UserEvent(source, Type.CREATE, userId, emailAddress);
    }

    public enum Type{
        CREATE, DELETE
    }
}
```
- 부모 클래스 ApplicationEvent 생성자의 Object source 인자는 이벤트를 게시하는 클래스의 객체를 의미.