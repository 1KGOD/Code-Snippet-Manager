package com._K.SnippetManager.web.controller;

import com._K.SnippetManager.persistence.dao.UserDao;
import com._K.SnippetManager.persistence.entity.User;
import com._K.SnippetManager.service.UserService;
import com._K.SnippetManager.web.form.UserForm;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;
    private final UserDao userDao;

    public UserController(UserService userService, UserDao userDao) {
        this.userService = userService;
        this.userDao = userDao;
    }

    @RequestMapping(value = "/register" , method = RequestMethod.GET)
    public String registerForm(Model model){
        model.addAttribute("userForm",new UserForm());
        return "home/register";
    }

    @RequestMapping(value = "/register" , method = RequestMethod.POST)
    public String register(@Valid @ModelAttribute("userForm")UserForm userForm, Model model, RedirectAttributes redirectAttributes, BindingResult bindingResult){
            if(bindingResult.hasErrors()){
                return "home/register";
            }else {
                userService.saveUser(userForm);
                redirectAttributes.addFlashAttribute("showSuccessModal", true);
                return "redirect:/login";
            }
    }

    @RequestMapping(value = "/login" , method = RequestMethod.GET)
    public String loginForm(){
        return "home/login";
    }




    @RequestMapping(value = "/user/dashboard" , method = RequestMethod.GET)
    public String user(@AuthenticationPrincipal UserDetails userDetails, Model model){
        String email = userDetails.getUsername();
        Optional<User> user = userDao.findByEmailAndIsDeletedFalse(email);
        model.addAttribute("user",user.orElse(null));
        return "user/userdashboard";
    }
}
