# Simple State machine demo


## State 정의

```java 
public enum States {
    DRAFT,
    DOC_SUBMITTED,
    DOC_UNDER_REVIEW,
    DOC_COMPLETED,
}
```

## Event 정의

```java 
public enum Events {
    SUBMIT,
    REVIEW,
    CONFIRM
}
```

## 상태머신 작성

@EnableStateMachine 을 사용하면 하나의 상태머신을 사용하게 된다.  
문서마다 상태가 다르므로 상태머신이 각각 필요하다  
그러므로 Factory 또는 Builder 를 이용한다.

### 상태 전이
DRAFT 에서 DOC_SUBMITTED 로 전이 하기 위해서는 이벤트(SUBMIT) 가 트리거 되어서  
적용된다 .. 를 아래와 같이 적용한다.

```java
trasitions
 .source(States.DRAFT).target(States.DOC_SUBMITTED)
                .event(Events.SUBMIT)
                .guard(guard())
```
guard는 상태전이 전에 실행되는 메서드 이다.  
단순히 false를 돌려주면 상태변이는 발생하지 않고 입력되는 상태로   
유지된다.

```java
@Slf4j
@EnableStateMachineFactory
public class StateMachineConfig extends StateMachineConfigurerAdapter<States, Events> {


    @Override
    public void configure(StateMachineConfigurationConfigurer<States, Events> config) throws Exception {
        config
                .withConfiguration()
                .autoStartup(true)
                .listener(new StateMachineEventListener());
    }


    @Override
    public void configure(StateMachineStateConfigurer<States, Events> states) throws Exception {
        states
                .withStates()
                .initial(States.DRAFT)
                .states(EnumSet.allOf(States.class));
    }

    @Override
    public void configure(StateMachineTransitionConfigurer<States, Events> transitions) throws Exception {
        transitions
                .withExternal()
                .source(States.DRAFT).target(States.DOC_SUBMITTED)
                .event(Events.SUBMIT)
                .guard(guard())
                .and()
                .withExternal()
                .source(States.DOC_SUBMITTED).target(States.DOC_UNDER_REVIEW).event(Events.REVIEW)
                .and()
                .withExternal()
                .source(States.DOC_UNDER_REVIEW).target(States.DOC_COMPLETED).event(Events.CONFIRM);

    }


    public Guard<States, Events> guard() {
        return context -> false;
    }
```

## 상태 머신 유지 

StateMachinePersist를 이용하여 상태를 저장 할 수 있다.

```java
public class JdbcStateMachinePersist implements StateMachinePersist<States, Events, String> {


    @Override
    public void write(StateMachineContext<States, Events> context, String machineId) throws Exception {
    }

    @Override
    public StateMachineContext<States, Events> read(String machineId) throws Exception {
    }
}
```

```java

 var pesister = new DefaultStateMachinePesister(jdbcStateMachinePersist);
 
  //상태 저장
  StateMachine<States, Events> stateMachine = factory.getStateMachine(machineId);
  stateMachine.sendEvent(Mono.just(MessageBuilder.withPayload(Events.SUBMIT).build()))
                .blockLast();

  persister.persist(stateMachine, machineId);

  //상태 복원
  var stateMachine = factory.getStateMachine(docState.getMachineId());
  persister.restore(stateMachine, docState.getMachineId());

```

## Schema

```sql


CREATE TABLE state_machine (
    machine_id VARCHAR(255) PRIMARY KEY NOT NULL,
    state      VARCHAR(255)             NOT NULL
);

CREATE TABLE document (
    id           INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    title        VARCHAR(255) NOT NULL,
    description  TEXT         NOT NULL,
    created_date TIMESTAMP
);


create table document_state (
    machine_id VARCHAR(255) NOT NULL REFERENCES state_machine (machine_id),
    doc_id     INT          NOT NULL REFERENCES document (id),
    CONSTRAINT uk_document_state UNIQUE (machine_id, doc_id)
);



```