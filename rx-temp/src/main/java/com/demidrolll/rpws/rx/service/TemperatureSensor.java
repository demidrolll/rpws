package com.demidrolll.rpws.rx.service;

import com.demidrolll.rpws.rx.domain.Temperature;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;
import rx.Observable;

@Component
public class TemperatureSensor {
    private final Random rnd = new Random();

    private final Observable<Temperature> dataStream = Observable
        .range(0, Integer.MAX_VALUE)
        .concatMap(tick -> Observable
            .just(tick)
            .delay(rnd.nextInt(5000), TimeUnit.MILLISECONDS)
            .map(tickValue -> this.probe())
        )
        .publish()
        .refCount();

    private Temperature probe() {
        return new Temperature(16 + rnd.nextGaussian() * 10);
    }

    public Observable<Temperature> temperatureStream() {
        return dataStream;
    }
}
