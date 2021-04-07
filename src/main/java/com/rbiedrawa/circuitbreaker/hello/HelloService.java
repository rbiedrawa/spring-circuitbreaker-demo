package com.rbiedrawa.circuitbreaker.hello;

import java.time.Duration;
import java.util.Optional;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class HelloService {

	private static final String CB_SAY_HELLO = "sayHello";
	private static final String CB_SUCCESS = "success";

	// TODO: make better examples
	@TimeLimiter(name = CB_SAY_HELLO)
	@Retry(name = CB_SAY_HELLO)
	@CircuitBreaker(name = CB_SAY_HELLO, fallbackMethod = "sayHelloFallback")
	@Bulkhead(name = CB_SAY_HELLO)
	Mono<String> sayHello(Optional<String> name) {
		var seconds = (long) (Math.random() * 5);

		return name
			.map(remoteCall(seconds))
			.orElse(Mono.error(new RuntimeException("Missing name parameter!!!")))
			.delayElement(Duration.ofSeconds(seconds));
	}

	@CircuitBreaker(name = CB_SUCCESS, fallbackMethod = "sayHelloFallback")
	Mono<String> success() {
		return Mono.just("Success");
	}

	public Mono<String> sayHelloFallback(Exception ex) {
		log.info("Recovered from failure {}", ex.getMessage());
		return Mono.just("Hello World!");
	}

	private Function<String, Mono<String>> remoteCall(long delayInSeconds) {
		return str -> {
			var msg = String.format("Hello %s! (completed in %d sec.)", str, delayInSeconds);
			log.info("Remote call: {}", msg);
			return Mono.just(msg);
		};
	}
}
