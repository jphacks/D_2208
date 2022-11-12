package dev.abelab.smartpointer.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.abelab.smartpointer.domain.model.*;
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
    public Sinks.Many<SlideControlModel> slideControlSink() {
        return Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);
    }

    @Bean
    public Flux<SlideControlModel> slideControlFlux(final Sinks.Many<SlideControlModel> slideControlSink) {
        return slideControlSink.asFlux();
    }

    @Bean
    public Sinks.Many<PointerControlModel> pointerControlSink() {
        return Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);
    }

    @Bean
    public Flux<PointerControlModel> pointerControlFlux(final Sinks.Many<PointerControlModel> pointerControlSink) {
        return pointerControlSink.asFlux();
    }

    @Bean
    public Sinks.Many<UserModel> userSink() {
        return Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);
    }

    @Bean
    public Flux<UserModel> userFlux(final Sinks.Many<UserModel> userSink) {
        return userSink.asFlux();
    }

    @Bean
    public Sinks.Many<RoomUsersModel> roomUsersSink() {
        return Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);
    }

    @Bean
    public Flux<RoomUsersModel> roomUsersFlux(final Sinks.Many<RoomUsersModel> roomUsersSink) {
        return roomUsersSink.asFlux();
    }

    @Bean
    public Sinks.Many<RoomCustomPointersModel> roomCustomPointersSink() {
        return Sinks.many().multicast().onBackpressureBuffer(Queues.SMALL_BUFFER_SIZE, false);
    }

    @Bean
    public Flux<RoomCustomPointersModel> roomCustomPointersFlux(final Sinks.Many<RoomCustomPointersModel> roomCustomPointersSink) {
        return roomCustomPointersSink.asFlux();
    }

}
