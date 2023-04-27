package com.example.demo.event.hotel;

import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class HotelEventListener {
    @Async
//    @Order(1) Order는 싱글 스레드인 경우
    @EventListener(value=HotelCreateEvent.class)
    public void handleHotelCreateEvent(HotelCreateEvent hotelCreateEvent){
        // ..
    }
    @Async
//    @Order(2)
    @EventListener(value=HotelCreateEvent.class)
    public void handleResourceCreateEvent(HotelCreateEvent hotelCreateEvent){
        // ..
    }

}
