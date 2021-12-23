package com.demidrolll.rpws.temp.controller;

import com.demidrolll.rpws.temp.domain.Temperature;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class TemperatureController {

    private final Set<SseEmitter> clients = new CopyOnWriteArraySet<>();

    @GetMapping(value = "/temperature-stream")
    public SseEmitter events() {
        SseEmitter emitter = new SseEmitter();
        clients.add(emitter);

        emitter.onTimeout(() -> clients.remove(emitter));
        emitter.onCompletion(() -> clients.remove(emitter));

        return emitter;
    }

    @Async
    @EventListener
    public void handleMessage(Temperature temperature) {
        List<SseEmitter> deadEmmiters = new ArrayList<>();
        clients.forEach(emitter -> {
            try {
                emitter.send(temperature);
            } catch (Exception e) {
                deadEmmiters.add(emitter);
            }
        });
        deadEmmiters.forEach(clients::remove);
    }
}
