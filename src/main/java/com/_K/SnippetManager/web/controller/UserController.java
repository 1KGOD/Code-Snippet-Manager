package com._K.SnippetManager.web.controller;

import com._K.SnippetManager.persistence.dao.PasswordResetTokenDao;
import com._K.SnippetManager.persistence.dao.SnippetDao;
import com._K.SnippetManager.persistence.dao.UserDao;
import com._K.SnippetManager.persistence.entity.Language;
import com._K.SnippetManager.persistence.entity.PasswordRestToken;
import com._K.SnippetManager.persistence.entity.Snippet;
import com._K.SnippetManager.persistence.entity.User;
import com._K.SnippetManager.service.LanguageService;
import com._K.SnippetManager.service.PasswordService;
import com._K.SnippetManager.service.UserService;
import com._K.SnippetManager.service.impl.EmailServiceImpl;
import com._K.SnippetManager.service.impl.FileUploadServiceImpl;
import com._K.SnippetManager.web.form.LanguageForm;
import com._K.SnippetManager.web.form.SnippetForm;
import com._K.SnippetManager.web.form.UserForm;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.swing.text.html.Option;
import java.security.PublicKey;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Controller
public class UserController {

    private final UserService userService;
    private final UserDao userDao;
    private final SnippetDao snippetDao;
    private final FileUploadServiceImpl fileUploadServiceImpl;
    private final EmailServiceImpl emailServiceImpl;
    private final PasswordEncoder encoder;
    private final PasswordResetTokenDao passwordResetTokenDao;
    private final PasswordService passwordService;
    private final LanguageService languageService;

    public UserController(UserService userService, UserDao userDao, SnippetDao snippetDao, FileUploadServiceImpl fileUploadServiceImpl, EmailServiceImpl emailServiceImpl, PasswordEncoder encoder, PasswordResetTokenDao passwordResetTokenDao, PasswordService passwordService, LanguageService languageService) {
        this.userService = userService;
        this.userDao = userDao;
        this.snippetDao = snippetDao;
        this.fileUploadServiceImpl = fileUploadServiceImpl;
        this.emailServiceImpl = emailServiceImpl;
        this.encoder = encoder;
        this.passwordResetTokenDao = passwordResetTokenDao;
        this.passwordService = passwordService;
        this.languageService = languageService;
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


    @RequestMapping(value = "/request-resetpassword" , method = RequestMethod.GET)
    public String resetPasswordForm(Model model){
        model.addAttribute("request", new UserForm());
        return "home/resetpassword";
    }



    @RequestMapping(value = "/resetsuccess" , method = RequestMethod.POST)
    public String resetform(@ModelAttribute("userForm")UserForm userForm,Model model){

        Optional<User> users = this.userDao.findByEmailAndIsDeletedFalse(userForm.getEmail());

        if(users.isPresent()){
            User user = users.get();
//            System.out.println("User found: " + user.getEmail());
            // Here you can implement the logic to send a reset password email
            String token = UUID.randomUUID().toString();
            PasswordRestToken resetToken = new PasswordRestToken();
            resetToken.setToken(token);
            resetToken.setExpired(LocalDateTime.now().plusMinutes(10));
            resetToken.setUser(user);
            passwordService.savePasswordToken(resetToken);

            String resetLink = "http://localhost:8080/resetpassword?token=" + token;
            emailServiceImpl.sendResetMail(user.getEmail(), resetLink);
            model.addAttribute("successMessage", "Password reset link has been sent to your email!");
        }
        return "home/resetsuccess";
    }

    @RequestMapping(value = "/resetpassword" , method = RequestMethod.GET)
    public String handleResetForm(@RequestParam String token ,Model model){
        Optional<PasswordRestToken> resetToken = passwordResetTokenDao.findByTokenAndUserIsDeletedFalse(token);
        if(resetToken.isPresent() && resetToken.get().getExpired().isAfter(LocalDateTime.now())){


            UserForm userForm = new UserForm();
            userForm.setToken(resetToken.get().getToken());
            userForm.setEmail(resetToken.get().getUser().getEmail());
            model.addAttribute("userForm", userForm );
        }

        return "home/resetform";
    }

    @RequestMapping(value = "/resetpassword" , method = RequestMethod.POST)
    public String handleReset(@RequestParam String token ,@Valid @ModelAttribute("userForm")UserForm userForm,BindingResult bindingResult, Model model){
        Optional<PasswordRestToken> resetToken = passwordResetTokenDao.findByTokenAndUserIsDeletedFalse(token);
        if(bindingResult.hasErrors()){
            System.out.println("Validation error: " + bindingResult.getAllErrors());
            return "home/resetform";
        }
        if (resetToken.isPresent() && resetToken.get().getExpired().isAfter(LocalDateTime.now())){

            // Token is valid, update the user's password
            User user = resetToken.get().getUser();

            user.setPassword(encoder.encode(userForm.getPassword()));
            user.setUpdateAt(LocalDateTime.now());
            user.setName(user.getName());
            user.setEmail(user.getEmail());
            userDao.save(user);

            System.out.println("Password updated for user: " + user.getEmail());
            System.out.println("New password: " + userForm.getPassword());
            System.out.println("Token expired at: " + resetToken.get().getExpired());
            System.out.println("User ID: " + user.getUserID());
            System.out.println("User name: " + user.getName());
            System.out.println("Updated at: " + user.getUpdateAt());


            // Delete the token after use
            passwordResetTokenDao.delete(resetToken.get());
        }
        System.out.println("Token -> "+resetToken.get().getToken());
        System.out.println("Email -> "+resetToken.get().getUser().getEmail());
        // Redirect to login page with success message
        return "redirect:/login?resetSuccess";
    }


    @RequestMapping(value = "/user/dashboard" , method = RequestMethod.GET)
    public String user(@AuthenticationPrincipal UserDetails userDetails, Model model){
        String email = userDetails.getUsername();
        Optional<User> userOpt = userDao.findByEmailAndIsDeletedFalse(email);
        if (userOpt.isPresent()) {
            User user = userOpt.get();

            List<Snippet> snippets = snippetDao.findByUser_UserIDAndIsDeletedFalseOrderByCreatedAtDesc(user.getUserID());
            long publishCount = snippetDao.countByUser_UserIDAndIsDeletedFalseAndIsPublishedTrue(user.getUserID());

            model.addAttribute("user", user);
            model.addAttribute("count", snippets.size());
            model.addAttribute("page", "dashboard");
            model.addAttribute("publishCount",publishCount);
            model.addAttribute("snippets", snippets);
        } else {

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
            userForm.setProfileImage(null);
            model.addAttribute("userForm", userForm);
            model.addAttribute("page", "profile");
            model.addAttribute("user", user);
            return "user/userprofile";
        }else {
            return "redirect:/user/dashboard";
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
                String uploadedFilePath = fileUploadServiceImpl.uploadFile(userForm.getProfileImage());
                user.setProfileImage(uploadedFilePath);
            }
            userService.updateUser(user);
            System.out.println("User updated: " + user.getName() + ", " + user.getEmail() + ", " + user.getProfileImage());
        }
        return "redirect:/user/profile";
    }

    @RequestMapping(value = "/admin/dashboard" , method = RequestMethod.GET)
    public String adminDashboard(@AuthenticationPrincipal UserDetails userDetails,
                                 Model model){
        String email = userDetails.getUsername();
        Optional<User> users = userDao.findByEmailAndIsDeletedFalse(email);
        if(users.isPresent()){
            User user  = users.get();
            List<User> user1 = userDao.findAll();
            int count = user1.size();
            int snippetCount = snippetDao.findAll().size();
            model.addAttribute("user",user);
            model.addAttribute("user1",user1.stream().sorted(Comparator.comparing(User::getUserID).reversed()));
            model.addAttribute("count",count);
            model.addAttribute("snippetCount",snippetCount);
        }
        return "admin/admindashboard";
    }




    @RequestMapping(value = "/admin/user/list", method = RequestMethod.GET)
    public String adminSearch(@RequestParam(defaultValue = "0") int page,
                            @RequestParam(defaultValue = "5") int size,
                            @RequestParam(required = false)String keyword,@AuthenticationPrincipal UserDetails userDetails , Model model){
        String email = userDetails.getUsername();
        Optional<User> users = userDao.findByEmailAndIsDeletedFalse(email);
        if(users.isPresent()){
            User user = users.get();
            Page<UserForm> userForms = userService.getAllUsers(page,size,keyword);
            model.addAttribute("userForms",userForms.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPage", userForms.getTotalElements());
            model.addAttribute("keyword", keyword);
            model.addAttribute("user",user);
        }
        return "admin/adminUserList";
    }

    @RequestMapping(value = "/admin/user/edit" , method = RequestMethod.GET)
    public String snippetEdit(@RequestParam("userId") Long userId, @AuthenticationPrincipal UserDetails userDetails, Model model){
        String email = userDetails.getUsername();
        Optional<User> user = userDao.findByEmailAndIsDeletedFalse(email);
        Optional<User> user1 = userDao.findById(userId);

        if(user1.isPresent()){
            UserForm userForm = new UserForm(user1.get());
            System.out.println("User ID: " + userForm.getUserId());
            model.addAttribute("userForm", userForm);
            model.addAttribute("user",user.orElse(null));
            model.addAttribute("page", "snippet");
        }

        return "admin/adminUserEdit";
    }

    @RequestMapping(value = "/admin/user/edit" , method = RequestMethod.POST)
    public String adminUserEdit(@RequestParam("userId") Long userId , @AuthenticationPrincipal UserDetails userDetails , @Valid @ModelAttribute("userForm") UserForm userForm , BindingResult bindingResult , Model model){
        String email = userDetails.getUsername();
        Optional<User> users = userDao.findByEmailAndIsDeletedFalse(email);
        if (bindingResult.hasErrors()) {
            System.out.println("Error: " + bindingResult.getAllErrors());
        }
        if(users.isPresent()){
            User user = users.get();
            userService.editUser(userForm);
        }
        return "redirect:/admin/user/list";
    }

    @RequestMapping(value = "/admin/user/delete" , method = RequestMethod.GET)
    public String adminUserDelete(@RequestParam("userId")Long userId , @AuthenticationPrincipal UserDetails userDetails ,@ModelAttribute("userForm")UserForm userForm ,Model model){
        String email = userDetails.getUsername();
        Optional<User> users = userDao.findByEmailAndIsDeletedFalse(email);
        if(users.isPresent()){
            User user = users.get();
            userService.deletedUser(userForm);
        }
        return "redirect:/admin/user/list";
    }

    @RequestMapping(value = "admin/user/language", method = RequestMethod.GET)
    public String adminUserLanguage(@AuthenticationPrincipal UserDetails userDetails, Model model){
        String email = userDetails.getUsername();
        Optional<User> users = userDao.findByEmailAndIsDeletedFalse(email);
        if(users.isPresent()){
            User user  = users.get();
            LanguageForm languageForm = new LanguageForm();
            model.addAttribute("languageForm", languageForm);
            model.addAttribute("user",user);
        }
        return "admin/adminLanguage";
    }

    @RequestMapping(value = "admin/user/language", method = RequestMethod.POST)
    public String adminUserLanguageAdd(@AuthenticationPrincipal UserDetails userDetails,@ModelAttribute("languageForm")LanguageForm languageForm , Model model){
        String email = userDetails.getUsername();
        Optional<User> users = userDao.findByEmailAndIsDeletedFalse(email);
        if(users.isPresent()){
            User user = users.get();
            languageService.saveLanguage(languageForm);
        }
        return "redirect:/admin/user/language";
    }

    @RequestMapping(value = "admin/profile" , method = RequestMethod.GET)
    public String AdminUserprofile(@AuthenticationPrincipal UserDetails userDetails,Model model){

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
            return "admin/adminProfile";
        }else {
            return "redirect:/user/dashboard";
        }
    }


    @RequestMapping(value = "/admin/user/updateProfile", method = RequestMethod.POST)
    public String AdminProfileUpdate(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @ModelAttribute("userForm") UserForm userForm,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes,
            Model model) {

        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(error -> System.out.println("Validation error: " + error.getDefaultMessage()));
            // If validation errors exist, return to the profile page with the form data
            model.addAttribute("userForm", userForm);
            return "admin/profile";
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
                String uploadedFilePath = fileUploadServiceImpl.uploadFile(userForm.getProfileImage());
                user.setProfileImage(uploadedFilePath); // Save the file path to the user entity
            }
            userService.updateUser(user);
            System.out.println("User updated: " + user.getName() + ", " + user.getEmail() + ", " + user.getProfileImage());
        }
        return "redirect:/admin/profile";
    }

    @RequestMapping("/user/**")
    public String handleInvalidUserSubPaths(HttpServletRequest request) {
        return "error/404User";
    }

    @RequestMapping("/admin/**")
    public String handleInvalidAdminSubPaths(HttpServletRequest request) {
        return "error/404Admin"; 
    }


}
