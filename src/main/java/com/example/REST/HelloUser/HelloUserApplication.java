package com.example.REST.HelloUser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.web.bind.annotation.*;




@RestController
@SpringBootApplication
public class HelloUserApplication {

	public static void main(String[] args) {
		SpringApplication.run(HelloUserApplication.class, args);
	}


	@GetMapping("/")
	public String hello() {
		return "Hello World!";
	}

	@GetMapping("/f/")
	public String Func1() {
		return "FFFFFFFFFFFFFFFFFFF";
	}

	@RequestMapping("/greets")
	public String hello2(@RequestParam(name="name") String id) {


		return "{\"id\":1,\"content\":\"Hello, " + id + "}";
	}
}


