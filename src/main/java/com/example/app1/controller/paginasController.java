package com.example.app1.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.example.app1.records.UserDTO;






@Controller
public class paginasController {
	
	@GetMapping ("/")
	public String home () {
		return "login";
	}
	
	
	@GetMapping ("/home")
	public String mostrarhome(){
		return "home";
	}
	
	

}
