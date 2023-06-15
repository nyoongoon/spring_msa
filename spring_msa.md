# 샤딩
- 샤딩은 데이터를 샤드라는 딘위로 여러 데이터 베이스 인스턴스에 나누어서 관리
- 데이터는 샤드의 숫자만큼 분류하여 저장됨 -> 샤딩 분류 기준에 따라 데이터를 균등하게 분해. -> 스케일업 한계까 있는 데이터베이스를 스케일 아웃 할 수 있음.
- 

# Spring Boot Dev Tools는 로컬환경에서 애플리케이션 실행하고 개발할때 필요한  Maven의 scope 설정을 반드시 runtime으로 강제.
# Spring Configuration Processor
- Spring Configuration Processor는 자바 애너테이션 프로세서로 @ConfigurationProperties나 애너테이션 분석하여 메타데이터 생성
- application.properties파일을 편집할때 편리한 기능.
# Lombok
- 코드 자동완성해주는 라이브러리
- Lombok의 @Getter와 @EqualsAndHashCode를 선언부에 사용하면 자동으로 getter메서드들과 equals(), hashCode()메서드 생성해줌.

# Maven
- maven은 계층 구조를 가질 수 있으며, 자식 POM 파일을 부모 POM 파일에 설정된 의존관계를 그대로 상속 받을 수 있음
- 이때 자식 POM 파일에 부모 관계를 설정하는 데 사용되는 속성은 <parent></parent>
- 자식POM 파일의 dependency 설정에 개발에 필요한 groupId와 artifactId 설정만 하면 부모 POM에 미리 설정된 version이나 exclusion 설정을 상속받는다.
- 하지만 자식 POM 파일에 설정된 의존관계가 우선권을 갖음.
- spring-boot-maven-plugin을 사용하면 실행 가능한 jar나 war파일을 패키징할 수 있음


# @SpringBootApplication 애너테이션과 메인 클래스
- main() 메서드의 핵심 코드는 @SpringBootApplication 과 o.s.boot.SpringApplication클래스.
- 아래는 자동생성된 코드
- @SpringBootApplication은 @SpringBootApplication, @EnableAutoConfiguration, @ComponentScan 세개의 주요 애너테이션을 포함함.

```java
import org.springframework.boot.autoconfigure.AutoConfigurationExcludeFilter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.TypeExcludeFilter;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;

import java.lang.annotation.*;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(
        excludeFilters = {@Filter(
                type = FilterType.CUSTOM,
                classs = {TypeExcludeFilter.class}
        ), @Filter(
                type = FilterType.CUSTOM,
                classes = {AutoConfigurationExcludeFilter.class}
        )})
public @interface SpringBootApplication{}
```
## @SpringBootApplication
- 내부에는 스프링프레임워크에서 제공하는 @Configuration을 포함함.
- @Configuration 애너테이션이 정의된 클래스는 자바 설정 클래스.
- 자바 설정 클래스는 스프링 부트 애플리케이션을 설정할 수 있으며, 별도의 스프링 빈을 정의할 수 있음.
## @EnableAutoConfiguration
- **@Configuration 애너테이션과 같이 사용**하면 스프링 부트 프레임워크의 **자동 설정 기능을 활성화**하는 기능 제공함.
- 내부에는 o.s.boot.autoconfigure 패키지의 AutoConfigurationImportSelector를 가져오며 이 클래스가 자동 설정 기능을 활성화 하는 기능 담당.
### @AutoConfigurationImportSelector
- 자동 설정 기능 활성화 담당.
- SpringFactoriesLoader 클래스를 사용하여 META-INF 폴더에 spring.factories에 정의된 데이터를 읽음.
- spring.factories에는 스프링 프레임워크를 자동 설정하는 자동 설정 클래스들이 정의되어 있음.
- 결국 SpringFactoriesLoader로 로딩된 자동 설정 클래스들이 실행되며, 각각의 자동 설정 클래스들은 관례와 조건에 따라 애플리케이션을 설정함.
## @ComponentScan
- 클래스 패스에 포함되어 있는 @Configuration으로 정의된 자바 설정 클래스와 스테레오 타입 애너테이션으로 정의된 클래스를 스캔
- 스프링 빈 설정을 스캔하며, 찾아낸 것들은 스프링 빈 컨테이너가 스프링 빈으로 로딩하고 관리함.


# SpringApplication.run()
```
public ConfigurableApplicationContext run(String... arg){
    //생략
    ConfigurableApplicationContext context = null; // 1
    //생략
    SpringApplicationRunListeners listeners = getRunListeners(args);
    listeners.starting();
    try{
    //
        context = createApplicationContext(); // 2
        refreshContext(context);
        afterRefresh(context, applicationArguments);
        //
    }
    return context;
}
```
# 의존성 주입
- 기존 코드
```java
public class NotificationService{
    public boolean sendNotification(User user, String message){
        SmsSender smsSender = new SmsSender();
        smsSender.setEndPoint();
        smsSender.setPort();
        smsSender.setTimeout(4000L);
        return smsSender.sendText(user.getPthoneNumber(), message);
    }
}
```
- 코드 수정된 경우(appPush)
```java
public class NotificationService{
    public boolean sendNotification(User user, String message){
        AppPushSender appPushSender = new AppPushSender();
        appPushSender.setEndPoint();
        appPushSender.setPort();
        appPushSender.setTimeout(4000L);
        return appPushSender.sendText(user.getPthoneNumber(), message);
    }
}
```
- **인터페이스로 리팩터링한 경우**
```java
public class NotificationService{
    
    private Sender messageSender;
    
    public NotificationService(Sender messageSender){
        this.messageSender = messageSender;
    }
    
    public boolean sendNotification(User user, String message){
        return messageSender.sendText(user.getPhonNumber(), message);
    }
}

```

## 애너테이션 기반 설정의 의존성 주입
- 의존성 주입 과정에서 필요한 두 객체(주입되고, 주입받고) 모두 스프링 빈 객체여야함
### @Autowired
- 의존성이 필요한 클래스 내부에 의존성 주입을 받는 곳을 표시.
- 스테레오 타입 애너테이션이 정의된클래스에 public 생성자가 하나만 있을 경우 @Autowired생략 가능
### @Qualifier
- 의존성을 주입할 스프링 빈 이름을 정의하는 역할.
- 클래스 타입은 같지만 이름이 각자 다른 여러 스프링 빈 중 정의된 이름의 스프링 빈을 주입받기위해 사용.
- 주입 대상 스프링 빈 중 클래스 타입은 같지만 스프링 빈 이름이 다른 스프링 빈들이 있을 때,
- @Autuwired하나만 정의하는 것보다 @Qualifer를 같이 사용하여 스프링 빈 이름까지 명시적으로 지정하는 것이 좋음.
### 생성자 주입 방식(권장)
- 코드가 간결
- 기본 설정으로 생성된 스프링 빈은 싱글턴이므로 하나의 객체가 여러 객체에 공유됨
- 런타입 도중 객체 상태가 바뀌는 것은 잠재적 버그가 될 수 있음.
- setter메서드는 런타임 도중 개발자 의도와 다르게 호출될 가능성 있음.
- 테스트 코드 작성할 떄 목 객체 주입하기 편리. -> 필드 주입 방식은 외부에서 목객체 주입 쉽지 않음

## 자바 설정의 의존성 주입
### 스프링 빈 사이의 참조
- 자바 설정에서는 한 개의 설정 클래스 안에 정의된 스프링 빈들끼리 의존성을 주입하는 경우
- @Bean 애너테이션이 선언된 메서드를 그대로 사용하면 의존성 주입이 됨.
```java
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.text.SimpleDateFormat;

@Configuration
public class ServerConfig {
    @Bean
    public String datePattern() {
        return "yyyy-MM-dd'T'HH:mm:ss.XXX";
    }

    @Bean
    public DateFormatter defaultDateFormatter() {
        return new DateFormatter(datePattern()); //스프링 빈 사이의 참조
    }
}

public class DateFormatter implements Formatter<Date> {
    private SimpleDateFormat sdf;
    pubilc DateFormatter(String pattern) {
        this.sdf = new SimpleDateFormat(pattern);
    }
    @Override
    public String of(Date target){
        return sdf.format(target);
    }
}
```
### 의존성이 다른 설정 클래스에 있는 경우 (스프링 빈 사이의 참조(메서드 참조) 방식 사용x)

```java
import org.springframework.context.annotation.Bean;

@Configuraion
public class DividePatternConfig {
    @Bean
    public String localDatePattern(){
        return "yyyy-MM-dd'T'HH:mm:ss";
    }
}
@Configuration
public class DivideServerConfig{
    @Bean
    public DateFormatter localDateFormatter(String localDatePattern){
        return new DateFormatter(localDatePattern);
    }
}
```
- 스프링은 @Bean으로 선언된 메서드의 인자를 분석하여 타입은 스프링 빈의 타입, 인자 이름은 빈의 이름으로 인식함.
- @Autowired나 @Qualifier사용도 가능

# ApplicationContext 인터페이스
- ApplicationContext는 스프링 프레임워크에서 가장 중요한 역할
- spring-context 모듈에서 제공하는 기능임, 스프링 부트를 포함하는 모든 스프링 프로젝트는 spring-context모듈을 포함함.
- ApplicationContext는 인터페이스고, 다양한 구현 클래스가 기본으로 제공됨.
- ApplicationContext는 spring-beans 모듈의 os.beans.factory에 포함된 BeanFactory 인터페이스를 상속함.
- 스프링 컨테이너의 기능은 BeanFactory 인터페이스에 정의되어 있음-> ApplicationContext도 스프링 빈 컨테이너의 기능을 제공함.
- 이외에도 다른 기능 제공, 많은 개발자들이 ApplicationContext를 기본 스프링 빈 컨테이너로 사용함.
``` ApplicationContext 일부
public interface ApplicationContext extends
    EnvironmentCapable,
    ListableBeanFactory,
    HierarchicalBeanFactory,
    MessageSource,
    ApplicationEventPublisher,
    ResourcePatternResolver{
```
- EnvitonmentCapable은 환경 변수를 추상화한 Environment 객체를 제공하는 getter메서드가 포함된 인터페이스
- 빈 컨테이너인 BeanFactory를 상속받는 ListableBeanFactory. 스프링 빈 리스트를 리턴하는 여러 메서드가 포함되어 있음.
- HierarchicalBeanFactory도 BeanFactory를 상속받음. 부모 자식간 관계를 맺으며 부모 BeanFactory를 리턴 받을 수 있음.
- MessageSource -> 국제화 메시지 처리할 수 있는 메서드 제공
- ApplicationEventPublisher는 스프링 프레임 워크에서 사용할 수 있는 이벤트들을 생성할 수 있는 메서드를 제공
- ResourcePatternResolver -> 패턴을 이용하여 Resource를 다룰 수 있는 메서드를 제공.
## 컨테이너 기능
- ApplicationContext를 지칭하는 이름은 스프링 빈 컨테이너, IoC컨테이너, DI 컨테이너 등인데, 부모 인터페이스 중
- -> ListableBenFactory와 HierearchicalBeanFactory가 BeanFactory를 상속 받았기 떄문.
## 이벤트 게시 및 구독 기능
- ApplicationContext가 상속하는 ApplicationPublisher 인터페이스는 이벤트를 게시하는 기능을 제공pulblishEvent()를 사용하면 됨
- 게시된 이벤트를 구독하려면 별도의 애너테이션이나 인터페이스를 구현.
## 계층 구조 기능
- ApplicationContext들은 서로 계층 구조를 가질 수 있음.
- 하위 ApplicationContext에서 관리하지 않는 스프링 빈은 상위 ApplicationContext에서 받아올 수 있음.
## 외부 환경변수 사용 기능
- EnvironmentCapable 인터페이스는 getEnvironment() 추상메서드를 제공. ApplicationContext는 이를 구현한 메서드를 제공.

# 스프링 빈 스코프
- 스프링 프레임워크는 여섯가지의 빈 스코프 설정을 제공.
- 각 스코프에 따라 스프링 빈이 생성되는 시점과 종료되는 시점이 다름
- singleton -> 일반적으로는 기본 singleton 만 사용
- prototype
- request
- session
- application
- Websocket
## singleton
- 일반적인 싱글턴과 다름
- singleton 스코프로 설정된 스프링 빈은 스프링 빈 컨테이너에서 유일한 객체이지만 JVM에서 여러개 있을 수 있음
- 스프링 빈은 순수 자바 객체이기 때문에 의존성 주입 하지 않고 new로 여러 객체 생성 가능.

# 스프링 빈 생명주기 관리o
- 스프링은 인터페이스와 애너테이션 같은 여러 방법을 사용하여 스프링 빈 생명주기에 개발자가 관여할 수 있게 해줌
- 스프링 빈을 생성, 소멸하는 과정에서 개발자가 작성한 코드를 특정 시점이 호출할 수 있는데, 이를 콜백함수라고 함.
- BeanPostProcessor.postProcessBeforeInitialization(), postProcessAfterInitialization()
- InitializingBean.afterPropertiesSet()
- DisposableBean.destory()
- @PostConstruct, @PreDestroy
- @Bean의 initMethod, destroyMethod 속성

# 스프링 빈 고급 정의
- 스프링 빈을 정의할 때 기본 설정, 지연 로딩 설정이 필요한 경우가 있음.
- 스프링 프레임워크는 이를 위해 각각 @Prime, @Lazy 애너테이션을 제공함.
## @Primary
- @Qualifier를 사용하지 않고 의존성 주입 할 수 있게 해줌.
- 같은 클래스 타입인 여러 스프링 빈 중 @Primary 애너테이션이 선언된 스프링 빈이 의존성 주입됨.
- 자동설정된 스프링 빈 중 일부분을 변경할 때도 Priamry 애너테이션을 사용할 수도 있음.
## @Lazy
- @Lazy가 정의된 스프링 빈은 스프링 빈 컨테이너가 설정만 로딩하고 의존성 주입 시점에 빈 객체를 생성.

# 스프링 빈 VS 자바 빈 VS DTO VS 값 객체
## 스프링 빈
- 객체와 이름, 클래스 타입 정보가 스프링 컨테이너로 관리되는 객체
## 자바 빈
- 기본 생성자가 선언되어 있고, getter/setter패턴, java.io.Serialicable을 구현하고 있어야함.
## DTO
- 소프트웨어 사이에 데이터를 전달하는 객체를 의미
- 비즈니스 로직이 없어야함.
- getter는 필요.
## 값 객체(VO)
- 특정 테이터를 추상화하여 데이터를 표현하는 객체
- equals를 재정의 해서 클래스가 표현하는 값을 거로 비교하면 좋음
- DDD에서 VO는 불변속성을 가지고 있어야한다.

### 불변 클래스를 설계하는 방법
- 클래스를 반드시 final로 선언
- 클래스의 멤버변수들을 반드시 final로 선언
- 생성자를 직접 선언하여 기본 생성자가 있지 않도록 함
- 멤버 변수에서 setter 메서드를 만들지 말고 getter 메서드를 만들어서 사용.

# 스프링 웹 MVC
## HTTP 프로토콜
### 요청 메시지
- 요청 라인 : HTTP메서드, HTTP리소스(요청 대상을 의미), HTTP 프로토콜의 버전
```
// 요청라인 예시
- GET?hotel.html?id=129 HTTP1.1
- 메서드?리소스 프로토콜 버전
- 리소스는 HTTP 요청 대상을 의미하며, HTML 같은 문서나 이미지 같은 멀티미티더 파일도 될 수 있음.
- 각 리소스는 URI로 구분됨.
```
- HTTP 헤더
- HTTP 바디
### 응답 메시지
- 상태 라인 : HTTP 프로토콜 버전과 상태코드(응답 메시지의 상태를 의미)를 포함
```
// 상태라인 예시
- HTTP1.1 200 Ok
// 응답 상태는 5가지로 구분 -> 첫 번째 자리 숫자는 대분류, 두,세번째 자리 숫자 조합을 상세 분류 코드라 함.
```
- HTTP 헤더
- HTTP 바디
### HTTP 특징
- 비연결성 : -> 비연결성을 이용하여, 로드밸런서 등으로 서버를 쉽게 수평확장, 축소 가능.
- 무상태 : -> 여러 요청을 해도 각 요청은 하나씩 독립적으로 처리됨. 인증과 인가가 문제되지만 쿠키나 세션으로 해결.

## 스프링 웹 MVC 프레임워크
### 서블릿
- J2EE 스펙 중 서블릿은 HTTP 프로토콜을 사용하여 데이터를 주고받는 서버용 프로그래밍 스펙을 의미함
- 서브릿 애플리케이션을 관리하고 실행하는 서버를 서블릿 컨테이너 또는 WAS라고함.
- 서블릿 애플리케이션은 설정 파일에 서블릿 정보를 작성하고 톰캣이 실행할 때 이를 읽어 서블릿 애플리케이션을 로딩하는 방식으로 설정함.
- 설정 파일에 등록되는 정보는 어떤 서블릿 애플리케이션이 어떤 URL을 처리할 지 설정함.
#### HttpServletRequest & HttpServletResponse
- 서블릿 애플리케이션 내부에서 HttpServletRequest 와 HttpServletResponse 객체를 이용하여 클라이언트의 요청과 응답을 처리함.

### DispatcherServlet
- 프런트 컨트롤러 패턴으로 디자인 -> 요청과 응답에 대해 공통 기능을 일괄적으로 쉽게 추가 가능
- -> 이런반식으로 인증과 인가 기능을 쉽게 구현 -> 스프링 시큐리티
- 컴포넌트에 정보를 질의하거나 각자 역할을 담당하는 컴포넌트에 사용자 요청을 전달함
### 서블릿 스택과 스레드 모델
- 서블릿 스택 vs 리액티브 모델 == 동기식 vs 비동기식
#### 서블릿 스택 특징
- was는 스레드를 효율적으로 관리하고자 스레드들을 관리하는 스레드 풀을 포함한다.
- 사용자 요청부터 응답까지 하나의 스레드에서 모든 작업이 실행된다.
- 사용자 요청과 스레드 생면주기가 일치하므로 쉽게 개발 및 운영할 수 있다.


# REAT-API 설계
- 리소스 : 리소스(자원)은 HTTP URI로 표현한다.
- 행위 : 리소스에 대한 행위는 HTTP 메서드로 표현함.
- 표현 : 리소스에 대한 행위 내용은 HTTP 메시지 내용으로 표현한다. HTTP 메시지 포맷은 JSON을 사용
- 행위는 GET,POST,PUT,DELTE를 기본으로 사용
## 리소스
- 리소스 이름은 가능하면 동사보다 명사를 사용
- 리소스는 계층적 구조를 가질 수 있다. 따라서 복수 형태 사용
- ex) /hotels/{uniqueId} /hotels/123412 => 리소스는 복수형, 하위는 고유 아이디값.
- REST-API 명세서에서 URI에 포함된 리소스 변수는 중괄호를 사용하여 표현함.
- 리소스들도 계층 구조로 설계 가능
- ex) /hotels/123412/rooms/41231 -> 호텔리소스/호텔아이디/객실리소스/객실아이디
## 응답코드
- 일반적으로 사용하는 응답 코드
- 200 OK
- 201 Created : 클라이언트 요청을 성공적으로 생성했을 때 -> 주로 POST
- 400 Bad Request : 클라이언트 요청이 유효하지 않음
- 401 Unauthorized : 인증받지 않은 클라이언트가 요청할 때 응답하는 상태 코드 -> ex)로그인 인증 필요
- 402 Forbidden : 인가받지 않은 클라이언트가 요청할 때 응답하는 상태코드  -> ex) 해당 계정 권한 없음
- 500 Error: 서버 오류로 정상적으로 클라이언트 요청을 처리할 수 없을 때
## REST-API 특성과 설계
- 세가지 특성 : 무상태성, 일관성, 멱등성
- 무상태성 : 상태를 저장하면 안됨 -> 하나의 API가 하나의 기능을 완전히 실행해야함.
- 일관성 : 정해진 규칙과 반복되는 패턴으로
- 멱등성 : 여러 번 적용한 결과와 한 번 적용한 결과가 같음 -> POST 제외 메서드들은 멱등성이 있어야함.

# @ResponseBody와 HttpMessageConverter
- @ResponseBody가 선언된 메서드가 리턴하는 객체는 메시지 컨버터 중 적합한 메시지 컨버터가 JSON메시지로 마셜링됨.
- HttpMessageConverter는 Http메시지를 객체로 읽고 객체를 HTTP 메시지로 쓸 수 있는 보편적인 메서드를 제공.
## Http 프로토콜
- Http 프로토콜은 요청 메시지나 응답 메시지의 바디에 내용을 포함할 수 있음
- Http 헤더에 Conttent-type을 사용하여 바디의 포맷을 정의
- 요청메시지에는 클라이언트가 처리할 수 있는 바디의 포맷을 Accept헤더를 사용하여 서버에 전달할 수 잇음.
- Accept헤더 값은 Content-type의 헤더값과 호환됨.
## o.s.http.MediaType
- 일반적으로 사용되는 Content-type 헤더 값을 정의하여 제공
- 스프링 프레임워크는 헤더값을 MediaType으로 변환하고 HttpMessageConverter의 canRead(), canWrite() 메서드를 사용
- -> 적절한 HttpMessateConverter객체를 찾아냄
## MappingJackson2HttpMessageConverter
- REST-API에서 JSON 형식을 사용하여 사용자에게 응답.
- -> Content-type 헤더 값이 application/json이 되고, MediaType 에 대응하는 MappingJackson2HttpMessageConverter 가 응답 메시지를 마셜링
- HttpMessageConverter 구현체 중 하나이며 자바 객체를 JSON 객체로 변환함.

# REST-API 예시
## Controller 클래스 구현

### @GetMapping 애너테이션
- 컨트롤러 클래스의 핸들러 메서드와 사용자 요청을 매핑하는 역할
- RequestMappingHandlerMapping 컴포넌트는 @GetMapping에 정의된 속성을 분석하여 사용자 요청과 핸들러 매핑하는 정보를 관리
- DispatcherServlet이 클라이언트 요청을 처리할 때는 RequestMappingHandlerMapping 컴포넌트의 메서드를 사용하여 클라이언트 요청과 매칭되는 핸들러 메서드를 조회
### @PathVariable
- 사용자가 요청한 rest-api의 uri에서 특정 위치에 있는 데이터를 뽑을 때 사용. -> 핸들러 메서드의 인자로 주입
- @GetMapping의 path 속성에는 리소스 변수가 선언되어 있어야함. (중괄호 사용)
- ex) @GettMapping(path="/hotels/{hotelId}/rooms/{roomNumber}")
### @RequsetParam
- HTTP 파라미터는 이름과 값으로 구성되고 &을 사용하여 여러 파라미터를 조합하여 전달함
- @RequsetParam 은 설정한 파라미터의 이름과 매칭되는 값을 핸들러 메서드의 인자에 주입하는 기능 제공
### @RequestHeader
- Http 헤더를 사용하여 클라이언트에서 데이터를 받아야할 때 사용.
- @RequestParam과 사용방법이 같음.

### @JsonProperty, @Jsonserialize, @JsonFormat
- @JsonProperty : 마셜링 과정에서 다른 속성 이름을 사용하고 싶다면 사용.
- @JsonSerializer : 마셜링 과정에서 타입 바꾸고 싶을 때 사용
- -> using 속성에는 JsonSerializer 구현체 클래스 속성 값으로 설정. -> 반대는 @JsonDeserialize
- @JsonFormat : 데이터를 사용자 정의 포맷으로 변경할 때 사용. -> java.util.Date나 java.util.Calendar 객체를 사용자가 원하는 포맷으로 변경
- -> @JsonSerialize와 다른점은 구현체 클래스 설정 대신 간단한 속성 설정으로 데이터 변경 가능.

### cf) 자바에서 Long 타입 값을 마셜링할 땐 문자열로
- 자바스크립트 32비트 정수 -> 자바 Long이 64비트이므로 오버플로우 될수가 있음






































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
#### 이벤트 메시지 클래스는 불변 클래스
- 한번 생성된 이벤트 메시지 객체의 속성은 변경할 수 없도록 해야함
- 여러 클래스에 전달되는 이벤트는 항상 동일한 내용을 보장해야함
- 불변 클래스로 설계 방법
- 클래스 속성을 private 으로 선언
- setter 금지

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

```java
@Slf4j
@Component
public class UserEventListener implements ApplicationListener<UserEvent> {
    private final EventService eventService;

    public UserEventListener(EventService eventService){
        this.eventService = eventService;
    }

    @Override
    public void onApplicationEvent(UserEvent event) {
        if(UserEvent.Type.CREATE == event.getType()){
            log.info("Listen CREATE event. {}, {},", event.getUserId(), event.getEmailAddress());
            eventService.sendEventMail(event.getEmailAddress()); // 실제 이벤트 처리 영역
        }else if(UserEvent.Type.DELETE == event.getType()){
            log.info("Listen DELETE event");
        }else {
            log.error("Unsupported event type. {}", event.getType());
        }
    }
}
```
- 이벤트 객체의 타입에 따라 기능을 실행할 EventService 스프링 빈을 주입
- -> UserEventListener는 UserEvent를 구독하는 기능만 담당하고, 실제 이벤트를 처리하는 기능은 별도에 클래스에 위임.

## 비동기 사용자 정의 이벤트 처리
- ApplicationContext 내부에서는 이벤트를 게시하기위해 ApplicationEventMulticaster를 사용
- 별도의 설정이 없다면 싱글 스레드로 동작함.
- @Configuration 클래스를 통해 멀티 스레드 이벤트 설정 가능

### @Async 애너테이션을 사용한 비동기 이벤트 처리
- 메서드와 클래스 선언부에 선언 가능
- @Async 대상은 스프링 빈으로 정의되어야 한다.
- public 메서드만 비동기로 동작
- this 키워드가 아닌 자기 주입을 사용하여 자신의 메서드를 호출
- @EnableAsync라 설정 클래스에 등록 되어 있어야함.
- 위의 ApplicationEventMulticaster는 모든 이벤트 구독 클래스 비동기,
- @Async는 원하는 이벤트 구독 클래스 선택적 비동기 

## @EventListener
- ApplicationEventListener 인터페이스를 구현하지 않아도
- @EventListener 애너테이션 사용 가능함.