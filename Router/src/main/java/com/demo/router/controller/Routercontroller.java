package com.demo.router.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.router.kafka_sender.KafkaMessageProducer;

@RestController
@RequestMapping("/router/api")
public class Routercontroller {
	
	
	private KafkaMessageProducer producer;
	
	public Routercontroller(KafkaMessageProducer producer) {
		this.producer=producer;
	}

	@PostMapping("/send")
	public String SendMessage(@RequestBody String XML) {
		
		String result=producer.sendMessage(XML);
		return result;
	}
	
	
}
