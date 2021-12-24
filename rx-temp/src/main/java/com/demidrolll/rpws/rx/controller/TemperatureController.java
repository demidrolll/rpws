package com.demidrolll.rpws.rx.controller;

import com.demidrolll.rpws.rx.service.RxSseEmitter;
import com.demidrolll.rpws.rx.service.TemperatureSensor;
import javax.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class TemperatureController {

    private final TemperatureSensor sensor;

    public TemperatureController(TemperatureSensor sensor) {
        this.sensor = sensor;
    }

    @GetMapping(value = "/temperature-stream")
    public SseEmitter events(HttpServletRequest request) {
        RxSseEmitter emitter = new RxSseEmitter();

        sensor.temperatureStream().subscribe(emitter.getSubscriber());

        return emitter;
    }
}
