package com.demidrolll.rpws.rx.service;

import com.demidrolll.rpws.rx.domain.Temperature;
import java.io.IOException;
import lombok.Getter;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import rx.Subscriber;

@Getter
public class RxSseEmitter extends SseEmitter {
    private static final Long SSE_SESSION_TIMEOUT = 30 * 60 * 1000L;
    private final Subscriber<Temperature> subscriber;

    public RxSseEmitter() {
        super(SSE_SESSION_TIMEOUT);

        this.subscriber = new Subscriber<>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(Temperature temperature) {
                try {
                    RxSseEmitter.this.send(temperature);
                } catch (IOException e) {
                    unsubscribe();
                }
            }
        };
        onCompletion(subscriber::unsubscribe);
        onTimeout(subscriber::unsubscribe);
    }
}
