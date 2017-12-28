package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import io.prometheus.client.Counter;

@Controller
@RequestMapping("/hello")
public class HelloService {

	private static final String template = "Hello, %s!";
	
	private static final Counter myCounter = Counter.build()
	        .name("CounterName")
	        .labelNames("status")
	        .help("Counter desc").register();

	@RequestMapping(method = RequestMethod.GET)
	public @ResponseBody Greeting sayHello(
			@RequestParam(value = "name", required = false, defaultValue = "Stranger") String name) {
		myCounter.labels("get").inc();
		return new Greeting(myCounter.labels("get").get(), String.format(template, name));
	}

	static class Greeting {
		private final double id;
		private final String content;

		public Greeting(double id, String content) {
			this.id = id;
			this.content = content;
		}

		public double getId() {
			return id;
		}

		public String getContent() {
			return content;
		}
	}

}
