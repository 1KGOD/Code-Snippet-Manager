package com._K.SnippetManager.web.controller;


import com._K.SnippetManager.persistence.dao.LanguageDao;
import com._K.SnippetManager.persistence.dao.SnippetDao;
import com._K.SnippetManager.persistence.dao.UserDao;
import com._K.SnippetManager.persistence.entity.Language;
import com._K.SnippetManager.persistence.entity.Snippet;
import com._K.SnippetManager.persistence.entity.User;
import com._K.SnippetManager.service.SnippetService;
import com._K.SnippetManager.web.form.SnippetForm;
import com._K.SnippetManager.web.form.UserForm;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Controller
public class SnippetController {

    @Autowired
    private UserDao userDao;

    @Autowired
    private SnippetDao snippetDao;

    @Autowired
    private SnippetService snippetService;

    @Autowired
    private LanguageDao languageDao;

    @RequestMapping(value = "/user/snippet" , method = RequestMethod.GET)
    public String userSnippet(@AuthenticationPrincipal UserDetails userDetails, Model model){
        String email = userDetails.getUsername();
        List<Language> lang = languageDao.findAll();
        Optional<User> user = userDao.findByEmailAndIsDeletedFalse(email);
        model.addAttribute("user",user.orElse(null));
        model.addAttribute("snippetForm", new SnippetForm());
        model.addAttribute("page", "snippet");
        model.addAttribute("lang",lang);
        return "user/usersnippet";
    }

    @RequestMapping(value = "/user/snippet/add" , method = RequestMethod.POST)
    public String snippetAdd(@AuthenticationPrincipal UserDetails userDetail, @Valid @ModelAttribute("snippetForm")SnippetForm snippetForm, BindingResult bindingResult , RedirectAttributes redirectAttributes , Model model){
        String email = userDetail.getUsername();
        Optional<User> user = userDao.findByEmailAndIsDeletedFalse(email);
        List<Language> lang = languageDao.findAll();
        if(bindingResult.hasErrors()){
            return "user/dashboard";
        }else {
            snippetService.saveSnippet(snippetForm,user.orElse(null));
            model.addAttribute("lang",lang);
            model.addAttribute("user",user.orElse(null));
            redirectAttributes.addFlashAttribute("showSuccessModal",true);
            return "redirect:/user/snippet/list";
        }
    }

    @RequestMapping(value = "/user/snippet/list" , method = RequestMethod.GET)
    public String snippetList(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "5") int size,
                              @RequestParam(required = false)String keyword, @AuthenticationPrincipal UserDetails userDetails, Model model){
        String email = userDetails.getUsername();
        Optional<User> user = userDao.findByEmailAndIsDeletedFalse(email);

        if(user.isPresent()){
            User user1 = user.get();
            System.out.println("User ID: " + user1.getUserID());
            System.out.println("User Email: " + user1.getEmail());
            System.out.println("User Name: " + user1.getName());
            Page<SnippetForm> snippetForms = snippetService.getAllSnippets(user1.getUserID(), page,size,keyword);
            System.out.println("Snippets found: " + snippetForms.getTotalElements());
            for (SnippetForm snippet : snippetForms) {
                System.out.println("Snippet Title: " + snippet.getTitle());
            }
            model.addAttribute("snippetForms",snippetForms.getContent());
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPage", snippetForms.getTotalElements());
            model.addAttribute("keyword", keyword);
            model.addAttribute("user",user.orElse(null));
            model.addAttribute("page", "snippets");
        }
        return "user/usersnippetlist";
    }

    @RequestMapping(value = "/user/snippet/view" , method = RequestMethod.GET)
    public String snippetView(@RequestParam("snippetId") Long snippetId, @AuthenticationPrincipal UserDetails userDetails, Model model){
        String email = userDetails.getUsername();
        Optional<User> user = userDao.findByEmailAndIsDeletedFalse(email);
        List<Language> lang = languageDao.findAll();
        Optional<Snippet> snippet = snippetDao.findById(snippetId);

        if(snippet.isPresent()){
            SnippetForm snippetForm = new SnippetForm(snippet.get());
            System.out.println("Snippet ID: " + snippetForm.getSnippetId());
            model.addAttribute("snippetForm", snippetForm);
            model.addAttribute("lang",lang);
            model.addAttribute("user",user.orElse(null));
            model.addAttribute("page", "snippet");
        }

        return "user/usersnippetdetails";
    }

    @RequestMapping(value = "/user/snippet/edit" , method = RequestMethod.GET)
    public String snippetEdit(@RequestParam("snippetId") Long snippetId, @AuthenticationPrincipal UserDetails userDetails, Model model){
        String email = userDetails.getUsername();
        Optional<User> user = userDao.findByEmailAndIsDeletedFalse(email);
        List<Language> lang = languageDao.findAll();
        Optional<Snippet> snippet = snippetDao.findById(snippetId);

        if(snippet.isPresent()){
            SnippetForm snippetForm = new SnippetForm(snippet.get());
            System.out.println("Snippet ID: " + snippetForm.getSnippetId());
            model.addAttribute("snippetForm", snippetForm);
            model.addAttribute("lang",lang);
            model.addAttribute("user",user.orElse(null));
            model.addAttribute("page", "snippet");
        }

        return "user/usersnippetedit";
    }

    @RequestMapping(value = "/user/snippet/edit" , method = RequestMethod.POST)
    public String snippetUpdate(@RequestParam("snippetId")Long snippetId , @AuthenticationPrincipal UserDetails userDetails, @Valid @ModelAttribute("snippetForm")SnippetForm snippetForm, BindingResult bindingResult ,Model model ) {
        String email = userDetails.getUsername();
        Optional<User> user = userDao.findByEmailAndIsDeletedFalse(email);
        List<Language> lang = languageDao.findAll();

        if (bindingResult.hasErrors()) {
            System.out.println("Error: " + bindingResult.getAllErrors());
        }
        if (user.isPresent()) {
            User user1 = user.get();
            snippetService.editSnippet(snippetForm,user1);
            System.out.println("Snippet Created: " + snippetForm.getCreatedAt());
        }
        return "redirect:/user/snippet/list";
    }

    @RequestMapping(value = "/user/snippet/delete" , method = RequestMethod.GET)
    public String snippetDelete(@RequestParam("snippetId") Long snippetId, @AuthenticationPrincipal UserDetails userDetails, Model model){
        String email = userDetails.getUsername();
        Optional<User> user = userDao.findByEmailAndIsDeletedFalse(email);
        List<Language> lang = languageDao.findAll();

        if(user.isPresent()){
            User user1 = user.get();
            snippetService.deleteSnippet(snippetId,user1);
            System.out.println("Snippet Deleted: " + LocalDateTime.now());
        }

        return "redirect:/user/snippet/list";
    }
}
