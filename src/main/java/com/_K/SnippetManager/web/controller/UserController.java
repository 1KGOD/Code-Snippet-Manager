package com._K.SnippetManager.web.controller;

import com._K.SnippetManager.persistence.dao.SnippetDao;
import com._K.SnippetManager.persistence.dao.UserDao;
import com._K.SnippetManager.persistence.entity.Snippet;
import com._K.SnippetManager.persistence.entity.User;
import com._K.SnippetManager.service.UserService;
import com._K.SnippetManager.service.impl.FileUploadService;
import com._K.SnippetManager.web.form.UserForm;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
public class UserController {

    private final UserService userService;
    private final UserDao userDao;
    private final SnippetDao snippetDao;
    private final FileUploadService fileUploadService;


    public UserController(UserService userService, UserDao userDao, SnippetDao snippetDao, FileUploadService fileUploadService) {
        this.userService = userService;
        this.userDao = userDao;
        this.snippetDao = snippetDao;
        this.fileUploadService = fileUploadService;
    }

    @RequestMapping(value = "/register" , method = RequestMethod.GET)
    public String registerForm( Model model){
        model.addAttribute("userForm",new UserForm());
        return "home/register";
    }

    @RequestMapping(value = "/register" , method = RequestMethod.POST)
    public String register(@Valid @ModelAttribute("userForm")UserForm userForm,BindingResult bindingResult, Model model, RedirectAttributes redirectAttributes){
            if(bindingResult.hasErrors()){
                return "home/register";
            }
        userService.saveUser(userForm);
                redirectAttributes.addFlashAttribute("showSuccessModal", true);
                return "redirect:/login";

    }

    @RequestMapping(value = "/login" , method = RequestMethod.GET)
    public String loginForm(){
        return "home/login";
    }


    @RequestMapping(value = "/user/dashboard" , method = RequestMethod.GET)
    public String user(@AuthenticationPrincipal UserDetails userDetails, Model model){
        String email = userDetails.getUsername();
        Optional<User> userOpt = userDao.findByEmailAndIsDeletedFalse(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            List<Snippet> snippets = snippetDao.findByUser_UserIDAndIsDeletedFalseOrderByCreatedAtDesc(user.getUserID());

            //debug and log
            System.out.println("Snippets found: " + snippets.size());
            System.out.println("Logged in User ID: " + user.getUserID());
            for (Snippet snippet : snippets) {
                System.out.println(snippet.getTitle());
            }
            model.addAttribute("user", user);
            model.addAttribute("count", snippets.size());
            model.addAttribute("page", "dashboard");
            model.addAttribute("snippets", snippets);
        } else {
            // Handle case where user is not found (optional: redirect or error message)
            model.addAttribute("user", null);
            model.addAttribute("count", 0);
            model.addAttribute("snippets", List.of());
        }
        return "user/userdashboard";
    }

    @RequestMapping(value = "/user/profile" , method = RequestMethod.GET)
    public String profile(@AuthenticationPrincipal UserDetails userDetails,Model model){

        String email = userDetails.getUsername();
        Optional<User> userOpt = userDao.findByEmailAndIsDeletedFalse(email);
        if(userOpt.isPresent()){
            User user = userOpt.get();
            System.out.println("User found: " + user.getEmail());
            System.out.println("Image Path: " + user.getProfileImage());
            UserForm userForm = new UserForm();
            userForm.setName(user.getName());
            userForm.setEmail(user.getEmail());
            userForm.setProfileImage(null); // Initialize the file field
            model.addAttribute("userForm", userForm);
            model.addAttribute("page", "profile");
            model.addAttribute("user", user);
            return "user/userprofile";
        }else {
            return "redirect:/user/dashboard"; // or an error page
        }
    }

    @RequestMapping(value = "/user/updateProfile", method = RequestMethod.POST)
    public String profileUpdate(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @ModelAttribute("userForm") UserForm userForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> System.out.println("Validation error: " + error.getDefaultMessage()));
            // If validation errors exist, return to the profile page with the form data
            model.addAttribute("userForm", userForm);
            return "user/userprofile";
        }

        String email = userDetails.getUsername();
        Optional<User> userOpt = userDao.findByEmailAndIsDeletedFalse(email);

        if (userOpt.isPresent()) {
            User user = userOpt.get();
            System.out.println("User found: " + user.getEmail());
            user.setName(userForm.getName());
            user.setEmail(userForm.getEmail());
            user.setUpdateAt(LocalDateTime.now());

            // Handle file upload
            if (userForm.getProfileImage() != null && !userForm.getProfileImage().isEmpty()) {
                System.out.println("Profile image is present.");
                String uploadedFilePath = fileUploadService.uploadFile(userForm.getProfileImage());
                user.setProfileImage(uploadedFilePath); // Save the file path to the user entity
            }
            userService.updateUser(user);
            System.out.println("User updated: " + user.getName() + ", " + user.getEmail() + ", " + user.getProfileImage());
        }
        return "redirect:/user/profile";
    }
}
