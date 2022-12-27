package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/credentials")
public class CredentialController {
    private CredentialService credentialService;
    private UserService userService;

    public CredentialController(CredentialService credentialService, UserService userService) {
        this.credentialService = credentialService;
        this.userService = userService;
    }

    @PostMapping
    private String uploadCredential(
            Authentication authentication,
            @RequestParam("credentialId") String CredId,
            @RequestParam("url") String CredUrl,
            @RequestParam("username") String CredUsername,
            @RequestParam("password") String CredPassword,
            Model model) {
        List<String> errorMessages = new ArrayList<>();
        if (CredUrl.isEmpty()) errorMessages.add("Credential URL cannot be empty.");
        if (CredUsername.isEmpty()) errorMessages.add("Credential username cannot be empty.");
        if (CredPassword.isEmpty()) errorMessages.add("Credential password cannot be empty.");
        if (!errorMessages.isEmpty()) {
            model.addAttribute("success", false);
            model.addAttribute("errors", errorMessages);
            return "result";
        }
        int userId = userService.getUser(authentication.getName()).getUserid();
        Credential credential = new Credential(CredUrl, CredUsername, CredPassword, userId);

        try {
            if (CredId.isEmpty()) {
                credentialService.createCredential(credential);
            } else {
                credential.setId(Integer.valueOf(CredId));
                credentialService.updateCredential(credential);
            }
            model.addAttribute("success", true);
            return "result";
        } catch (Exception e) {
            errorMessages.add("Exception occurred when attempting to upload.");
        }
        model.addAttribute("success", false);
        model.addAttribute("errors", errorMessages);
        return "result";
    }
     @GetMapping("/delete")
        private String deleteCredential(@RequestParam("credentialId") int id, Model model) {
            try {
                credentialService.removeCredential(id);
                model.addAttribute("success", true);
                return "result";
            } catch (Exception e) {
                model.addAttribute("errors", "Exception occurred when attempting to delete.");
                model.addAttribute("success", false);
                return "result";
            }
        }
}
