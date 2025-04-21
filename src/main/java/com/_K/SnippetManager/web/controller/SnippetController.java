package com._K.SnippetManager.web.controller;


import com._K.SnippetManager.persistence.dao.LanguageDao;
import com._K.SnippetManager.persistence.dao.SnippetDao;
import com._K.SnippetManager.persistence.dao.UserDao;
import com._K.SnippetManager.persistence.entity.Language;
import com._K.SnippetManager.persistence.entity.User;
import com._K.SnippetManager.service.SnippetService;
import com._K.SnippetManager.web.form.SnippetForm;
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
    public String snippetAdd(@AuthenticationPrincipal UserDetails userDetail,@Valid @ModelAttribute("snippetForm")SnippetForm snippetForm, BindingResult bindingResult ,Model model ){
        String email = userDetail.getUsername();
        Optional<User> user = userDao.findByEmailAndIsDeletedFalse(email);
        List<Language> lang = languageDao.findAll();
        if(bindingResult.hasErrors()){
            return "user/dashboard";
        }else {
            snippetService.saveSnippet(snippetForm,user.orElse(null));
            model.addAttribute("lang",lang);
            model.addAttribute("user",user.orElse(null));
            model.addAttribute("showSuccessModal",true);
            return "user/usersnippet";
        }
    }

    @RequestMapping(value = "/user/snippet/list" , method = RequestMethod.GET)
    public String snippetList(@RequestParam(defaultValue = "0") int page,
                              @RequestParam(defaultValue = "5") int size,
                              @RequestParam(required = false)String keyword, @AuthenticationPrincipal UserDetails userDetails, Model model){
        String email = userDetails.getUsername();
        Optional<User> user = userDao.findByEmailAndIsDeletedFalse(email);
        Page<SnippetForm> snippetForms = snippetService.getAllSnippets(page,size,keyword);
        model.addAttribute("snippetForms",snippetForms.getContent());
        for(SnippetForm snippetForm : snippetForms.getContent()){
            System.out.println("User: " + snippetForm.getUser());
            System.out.println("Language: " + snippetForm.getLanguage());
        }
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPage", snippetForms.getTotalElements());
        model.addAttribute("keyword", keyword);
        model.addAttribute("user",user.orElse(null));
        model.addAttribute("page", "snippetList");

        return "user/usersnippetlist";
    }

}
