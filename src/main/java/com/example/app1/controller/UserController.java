package com.example.app1.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.app1.records.UserRecordDTO;
import com.example.app1.service.UserService;


@RestController
public class UserController {
		
		
		
		 @Autowired
		    private UserService userservice;

		 @GetMapping("/cadastro")
		 public String showForm(Model model) {
		     model.addAttribute("userRecordDTO", new UserRecordDTO(null, null, null, null));
		     return "cadastro";
		 }
}
