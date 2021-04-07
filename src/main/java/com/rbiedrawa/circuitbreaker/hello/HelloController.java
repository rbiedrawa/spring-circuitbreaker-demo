package com.rbiedrawa.circuitbreaker.hello;

import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/hello")
@RequiredArgsConstructor
public class HelloController {

	private final HelloService helloService;

	@GetMapping
	Mono<String> hello(@RequestParam Optional<String> name) {
		return helloService.sayHello(name);
	}

	@GetMapping("success")
	Mono<String> success() {
		return helloService.success();
	}
}
