package com.example.zkspring;


import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@SpringBootApplication
@Controller
public class ZkspringApplication {

	public static void main(String[] args) throws Throwable{
		SpringApplication.run(ZkspringApplication.class, args);
	}
	@CrossOrigin(origins = "http://localhost:8080")
	@GetMapping("/login")
	public String login(@RequestBody ObjectNode JSONObject) {
		String username= JSONObject.get("username").asText();
		String password= JSONObject.get("password").asText();

		if (username.equals("user")&&password.equals("password"))
			return "zul/secure/main";
		return "login";
	}

	@GetMapping("/secure/{page}")
	public String secure(@PathVariable String page) {
		return "zul/secure/" + page;
	}
}
