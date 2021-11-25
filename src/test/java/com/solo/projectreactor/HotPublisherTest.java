package com.solo.projectreactor;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.stream.Stream;

public class HotPublisherTest {

    @Test
    void test1() {
        Flux<String> movieStream = Flux.fromStream(() -> getFlux()).delayElements(Duration.ofSeconds(1));

        movieStream.subscribe(input -> System.out.println("sam" + "is watching movie"+input));
      sleep(3);
        movieStream.subscribe(input -> System.out.println("solo" + "is watching movie"+input));
        sleep(20);
    }


    @Test
    void hotPublisher() {
        Flux<String> movieStream = Flux.fromStream(() -> getFlux()).delayElements(Duration.ofSeconds(1)).share();

        movieStream.subscribe(input -> System.out.println("sam" + "is watching movie"+input));
        sleep(3);
        movieStream.subscribe(input -> System.out.println("solo" + "is watching movie"+input));
        sleep(30);
    }

    @Test
    void hotPublisherrefcount() {
        Flux<String> movieStream = Flux.fromStream(() -> getFlux()).
                delayElements(Duration.ofSeconds(1)).publish().refCount(1);

        movieStream.subscribe(input -> System.out.println("sam" + "is watching movie"+input));
        sleep(10);
        movieStream.subscribe(input -> System.out.println("solo" + "is watching movie"+input));
        sleep(30);
    }

    @Test
    void hotPublisherautocount() {
        Flux<String> movieStream = Flux.fromStream(() -> getFlux()).
                delayElements(Duration.ofSeconds(1)).publish().autoConnect(1);

        movieStream.subscribe(input -> System.out.println("sam" + "is watching movie"+input));
        sleep(2);
        movieStream.subscribe(input -> System.out.println("solo" + "is watching movie"+input));
        sleep(30);
    }
    @Test
    void hotPublisherCache() {
        Flux<String> movieStream = Flux.fromStream(() -> getFlux()).
                delayElements(Duration.ofSeconds(1)).cache();

        movieStream.subscribe(input -> System.out.println("sam" + "is watching movie"+input));
        sleep(10);
        movieStream.subscribe(input -> System.out.println("solo" + "is watching movie"+input));
        sleep(30);
    }

    Stream<String> getFlux() {
        return Stream.of("scene1", "scene2", "scene3", "scene4", "scene5");
    }


    void sleep(int seconds) {
        try {
            Thread.sleep(seconds * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
