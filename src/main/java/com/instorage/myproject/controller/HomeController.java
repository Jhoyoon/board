package com.instorage.myproject.controller;

import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.Date;
import java.util.Locale;

import com.instorage.myproject.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {
	@Autowired
	UserService userService;
	@GetMapping(value="/")
	public String main(HttpSession session, RedirectAttributes rda,Model m) throws Exception{
		String id = (String)session.getAttribute("id");
		if("".equals(id) || id == null){
			return "home";
		}
		try{
			String nickname=userService.readUserById(id).getNickname();
			m.addAttribute("nickname",nickname);
			return "home";
		}catch (Exception e){
			e.printStackTrace();
			rda.addAttribute("error","별명을 확인할수 없습니다.");
			return "home";
		}
	}

	
}
