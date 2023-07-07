package com.hoyoon.board.domain;

import jakarta.validation.ConstraintViolation;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDate;
import java.util.Set;

public class UserValidator implements Validator {
    public boolean supports(Class<?> clazz){
        return User.class.isAssignableFrom(clazz);
    }
    public void validate(Object target, Errors errors){
        User user = (User)target;
        String id = user.getId();
        String pwd = user.getPwd();
        String email = user.getEmail();
        String birth = user.getbirth();

        // null 검사 -> 길이 검사 -> 공백 검사 ->

        // id 유효성 검사
        if(id == null || "".equals(id)){
            errors.rejectValue("id","required");
            return;
        }
        if(id.length() <= 4 || id.length() >= 31){
            errors.rejectValue("id","invalidlength",new String[]{"5","30"},null);
            return;
        }
        if(id.contains(" ")){
            errors.rejectValue("id","blank");
            return;
        }

        // pwd 유효성 검사
        if(pwd == null || "".equals(pwd)){
            errors.rejectValue("pwd","required");
            return;
        }
        if(pwd.length() <= 7 || pwd.length() >= 51){
            errors.rejectValue("pwd","invalidlength",new String[]{"8","50"},null);
            return;
        }
        if(pwd.contains(" ")){
            errors.rejectValue("pwd","blank");
            return;
        }

        // email 유효성 검사
        if(email == null || "".equals(email)){
            errors.rejectValue("email","required");
            return;
        }
        if(!email.contains("@")){
            errors.rejectValue("email","@");
            return;
        }
        if(email.contains(" ")){
            errors.rejectValue("email","blank");
            return;
        }
        if(email.length() <= 3 || email.length()>= 101){
            errors.rejectValue("email","invalidlength",new String[]{"4","100"},null);
            return;
        }

        if(birth == null || "".equals(birth)){
            errors.rejectValue("birth","required");
            return;
        }
        if(birth.length() != 8){
            errors.rejectValue("birth","invalidlength");
            return;
        }
        if(!birth.matches("[0-9]+")){
            errors.rejectValue("birth","number");
            return;
        }
        if(birth.contains(" ")){
            errors.rejectValue("birth","blank");
            return;
        }
        int yy = Integer.parseInt(birth.substring(0,4));
        int mm = Integer.parseInt(birth.substring(4,6));
        int dd = Integer.parseInt(birth.substring(6));
        LocalDate today = LocalDate.now();
        int year = today.getYear();
        if(!(yy >= 1900 && yy <= year)){
            errors.rejectValue("birth","year");
            return;
        }
        if(!(mm>=1 && mm<=12)){
            errors.rejectValue("birth","month");
            return;
        }
        if(!(dd>=1 && dd<=31)){
            errors.rejectValue("birth","day");
            return;
        }
        LocalDate birthFinal = LocalDate.of(yy,mm,dd);
        if(birthFinal.isAfter(today)){
            errors.rejectValue("birth","future");
            return;
        }
        user.setBirthFinal(birthFinal);

    }

}
