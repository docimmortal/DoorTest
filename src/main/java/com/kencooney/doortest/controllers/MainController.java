package com.kencooney.doortest.controllers;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import com.kencooney.doortest.utilities.ConfigReader;

import jakarta.servlet.http.HttpSession;

@Controller
public class MainController {

	@PostMapping("/returnToBBS")
	public ModelAndView returnToBBS(@RequestParam(required = false, defaultValue="") String bbsUrl,
			@RequestParam(required = false, defaultValue="") String token) {
		ModelAndView mav = new ModelAndView();
		System.out.println("Token: "+token);
		System.out.println("bbsUrl: "+bbsUrl);
		mav.setViewName("redirect:"+bbsUrl);
		mav.addObject("token", token);
		return mav;
	}
	
	// Landing Page
	@GetMapping("/main")
	public String landingPage(HttpSession session, @RequestParam(required = false, defaultValue="") String doorId,
			@RequestParam(required = false, defaultValue="") String token, Model model) {
		// This will somehow come from BBS
		if (doorId == "") {
			System.out.println("Door Id not found!");
			doorId="$2a$10$THG95h3cpE0U0kZJT5Z/Lu6e/CJbmG.ieOjskApgCKcYegRN6Tp2O";
		}
		System.out.println("Token: "+token);
		String redirectUrl="error";
		
		RestTemplate restTemplate = new RestTemplate();
		
		ConfigReader configReader = new ConfigReader("config.properties");
        String url = configReader.getProperty("bbs.usernameUrl");
        String returnUrl = configReader.getProperty("bbs.returnUrl");
        
        String requestBody = "{\"doorId\":\""+doorId+"\"}";
        
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        HttpEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);

        System.out.println("Response body: " + response.getBody());
        String username=response.getBody();
        if (username.length()>0) {
        	model.addAttribute("username",username);
        	model.addAttribute("bbsUrl",returnUrl);
        	session.setAttribute("token",token);
        	redirectUrl="welcome";
        }
		return redirectUrl;
	}
}
