package com.eura.web.controller;

import com.eura.web.model.DTO.*;
import com.eura.web.service.*;
import com.eura.web.util.CONSTANT;
import org.apache.ibatis.annotations.Param;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.List;
import java.util.Map;

@Controller
public class IndexController {

    @Resource(name = "userService")
    UserService userService;



    @RequestMapping(value ="/",method = RequestMethod.GET)
    public String home(HttpServletRequest request, HttpServletResponse response, Model model,@CookieValue(name = "user_id",required = false) String user_id){
        UserVO findUserVo = null;
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        if(flashMap!=null) {

            findUserVo =(UserVO)flashMap.get("userVo");
        }
        if(user_id!=null) {

            findUserVo =userService.findUserById(user_id);
        }

        if (findUserVo == null) {//로그인 필요


            //satProfile(model);
            return "login";
        }
        //List<UserVO> userList = userService.selectList();
        model.addAttribute("user",findUserVo);

        Cookie myCookie = new Cookie("user_id", findUserVo.getUser_id());
        myCookie.setMaxAge(1000);
        myCookie.setPath("/"); // 모든 경로에서 접근 가능 하도록 설정
        response.addCookie(myCookie);

        return "index";
    }



    @RequestMapping(value ="/index",method = RequestMethod.GET)
    public String index(HttpServletRequest request, HttpServletResponse response, Model model,@CookieValue(name = "user_id",required = false) String user_id){
        UserVO findUserVo = null;
        Map<String, ?> flashMap = RequestContextUtils.getInputFlashMap(request);

        if(flashMap!=null) {

            findUserVo =(UserVO)flashMap.get("userVo");
        }
        if(user_id!=null) {

            findUserVo =userService.findUserById(user_id);
        }

        if (findUserVo == null) {//로그인 필요


            //satProfile(model);
            return "login";
        }
        //List<UserVO> userList = userService.selectList();
        model.addAttribute("user",findUserVo);

        Cookie myCookie = new Cookie("user_id", findUserVo.getUser_id());
        myCookie.setMaxAge(1000);
        myCookie.setPath("/"); // 모든 경로에서 접근 가능 하도록 설정
        response.addCookie(myCookie);
        return "index";
    }


    @RequestMapping(value ="/login",method = RequestMethod.GET)
    public String login(){

        return "login";
    }
    @RequestMapping(value ="/join",method = RequestMethod.GET)
    public String join(){

        return "join";
    }

    @PostMapping(value ="/join_info")
    public String join_info(Model model,UserVO userVo){

        model.addAttribute("user",userVo);
        return "join_info";
    }

    @PostMapping(value ="/join_upload")
    public String join_upload(Model model,UserVO userVo){

        model.addAttribute("user",userVo);
        return "join_upload";
    }
    @PostMapping(value ="/join_photo")
    public String join_photo(Model model,UserVO userVo){

        model.addAttribute("user",userVo);
        return "join_photo";
    }

    @PostMapping(value ="/room_new")
    public String room_new(Model model,@CookieValue(name = "user_id",required = false) String user_id){

        if(user_id!=null){
            UserVO findUserVo = userService.findUserById(user_id);

            if(findUserVo!=null)
            {
                model.addAttribute("user",findUserVo);
            }
            else
                return "redirect:login";
        }

        return "room_new";
    }

    @PostMapping(value ="/join_mail")
    public String join_mail(Model model,UserVO userVo){



        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String hashedPassword = passwordEncoder.encode(userVo.getUser_pwd());
        userVo.setUser_pwd(hashedPassword);
        userService.join(userVo);

        model.addAttribute("user",userVo);
        return "join_mail";
    }
    @PostMapping(value ="/join_terms")
    public String join_terms(Model model,UserVO userVo){

        model.addAttribute("user",userVo);
        return "join_terms";
    }

    @RequestMapping(value ="/join_temporary",method = RequestMethod.GET)
    public String join_temporary(){

        return "join_temporary";
    }
    @RequestMapping(value = "/api_post_login",method = RequestMethod.POST)
    public String api_post_login(RedirectAttributes rttr,UserVO userVo){

        /*UserVO userVO = new UserVO();
        userVO.setUser_id(id);
        userVO.setUser_pw(pw);*/

        if(userVo.getUser_id() == null || userVo.getUser_pwd() == null){
            return "redirect:login";
        }
        UserVO findUser = userService.findUserById(userVo.getUser_id());

        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        if(findUser!=null){
            if(passwordEncoder.matches(userVo.getUser_pwd(),findUser.getUser_pwd()) ){
                rttr.addFlashAttribute("userVo", findUser);
                return "redirect:/";
            }

        }
        return "forward:login";
    }



}
