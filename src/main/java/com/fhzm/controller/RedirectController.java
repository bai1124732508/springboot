package com.fhzm.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("redirect")
public class RedirectController {
	@RequestMapping(value="error")
	public String error(){
		return "error";
	}

}
