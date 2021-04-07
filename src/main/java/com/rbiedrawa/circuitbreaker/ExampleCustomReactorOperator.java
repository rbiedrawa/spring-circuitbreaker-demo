package com.rbiedrawa.circuitbreaker;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.reactor.circuitbreaker.operator.CircuitBreakerOperator;
import reactor.core.publisher.Mono;

// example how to decorate a Mono by using the custom Reactor operator
// @Slf4j
// @Service
public class ExampleCustomReactorOperator {

	private final CircuitBreaker circuitBreaker;

	public ExampleCustomReactorOperator(CircuitBreakerRegistry circuitBreakerRegistry) {
		circuitBreaker = circuitBreakerRegistry.circuitBreaker("hello_world");
	}

	Mono<String> helloWorld() {
		return Mono.fromCallable(() -> "Hello World!")
				   .transformDeferred(CircuitBreakerOperator.of(circuitBreaker));
	}

}
