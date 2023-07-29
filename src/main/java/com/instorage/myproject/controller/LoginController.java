package com.instorage.myproject.controller;


import at.favre.lib.crypto.bcrypt.BCrypt;
import com.instorage.myproject.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping(value="/login")
public class LoginController {
    @Autowired
    UserService userService;

    @GetMapping(value="/logout")
    public String logoutGet(HttpSession session){
        session.invalidate();
        return "redirect:/";
    }
    @PostMapping(value="/login")
    public String loginPost(String id, String pwd, boolean rememberId, Model m, RedirectAttributes rda, HttpServletResponse res, HttpServletRequest req){
        boolean check = checkId(id);
        if(!check){
            rda.addFlashAttribute("error","id값이 유효하지 않습니다.다시 입력해주세요.");
            return "redirect:/";
        }
        boolean check2 = checkPwd(pwd);
        if(!check2){
            rda.addFlashAttribute("error","pwd 값이 유효하지 않습니다.다시 입력해주세요.");
            return "redirect:/";
        }
        try {
            String loginCheck = userService.loginCheck(id,pwd);
            if(loginCheck.equals("NonexistentID")){
                rda.addFlashAttribute("error","존재하지 않는 아이디 입니다.다시 확인해주세요.");
                return "redirect:/";
            }
            if(loginCheck.equals("MismatchedPassword")){
                rda.addFlashAttribute("error","비밀번호가 id와 일치하지 않습니다.다시 확인해주세요.");
                return "redirect:/";
            }
            // 아이디 기억하기 기능을 사용할시 쿠키를 한달동안 저장시킨다.
            // 체크하지 않았을 시 존재하는 쿠키를 제거한다.
            Cookie cookie;
            if(rememberId){
                cookie = new Cookie("id", id);
                cookie.setPath("/");
                cookie.setMaxAge(60*60*24*30);
            }else{
                cookie = new Cookie("id", id);
                cookie.setPath("/");
                cookie.setMaxAge(0);
            }
            res.addCookie(cookie);
            HttpSession session = req.getSession();
            session.setAttribute("id",id);
            // 세션 수명을 6시간으로 설정
            session.setMaxInactiveInterval(60*60*6);
            String nickname=userService.readUserById(id).getNickname();
            m.addAttribute("nickname",nickname);
            return "home";
        } catch (Exception e) {
            e.printStackTrace();
            rda.addFlashAttribute("error","에러가 발생했습니다.다시 시도해주세요");
            return "redirect:/";
        }
    }
    private boolean checkId(String id){
        if(id == null || "".equals(id)){
            return false;
        }
        if(id.contains(" ")){
           return false;
        }
        if(id.length() <= 4 || id.length() >= 20){
            return false;
        }
        return true;
    }
    private boolean checkPwd(String pwd){
        if(pwd == null || "".equals(pwd)){
            return false;
        }
        if(pwd.length() <= 7 || pwd.length() >= 51){
            return false;
        }

        if(pwd.contains(" ")){
           return false;
        }
        return true;
    }

}
