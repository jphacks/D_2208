package dev.abelab.smartpointer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.abelab.smartpointer.domain.model.TimerModel;
import dev.abelab.smartpointer.enums.SlideControl;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;
import reactor.util.concurrent.Queues;

/**
 * GraphQL Subscriptionの設定
 */
@Configuration
public class GraphQLSubscriptionConfig {

    @Bean
    public Sinks.Many<TimerModel> timerSink() {
        return Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);
    }

    @Bean
    public Flux<TimerModel> timerFlux(final Sinks.Many<TimerModel> timerSink) {
        return timerSink.asFlux();
    }

    @Bean
    public Sinks.Many<SlideControl> slideControlSink() {
        return Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);
    }

    @Bean
    public Flux<SlideControl> slideControlFlux(final Sinks.Many<SlideControl> slideControlSink) {
        return slideControlSink.asFlux();
    }

}
