package com.example.demo.event.user;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

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
            eventService.sendEventMail(event.getEmailAddress());
        }else if(UserEvent.Type.DELETE == event.getType()){
            log.info("Listen DELETE event");
        }else {
            log.error("Unsupported event type. {}", event.getType());
        }
    }
}
