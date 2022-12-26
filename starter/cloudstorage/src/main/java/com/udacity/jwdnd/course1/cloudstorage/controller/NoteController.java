package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
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
@RequestMapping("/notes")
public class NoteController {
    private NoteService noteService;
    private UserService userService;

    public NoteController(NoteService noteService, UserService userService) {
        this.noteService = noteService;
        this.userService = userService;
    }

    @PostMapping
    private String uploadNote(
            Authentication authentication,
            @RequestParam("noteTitle") String noteTitle,
            @RequestParam("noteDescription") String noteDescription,
            Model model) {
        List<String> errorMessages = new ArrayList<>();

        if (noteTitle.isEmpty()) errorMessages.add("Note title cannot be empty.");
        if (noteDescription.isEmpty()) errorMessages.add("Note description cannot be empty.");

        if (errorMessages.isEmpty()) {
            try {
                int userId = userService.getUser(authentication.getName()).getUserid();
                Note note = new Note(noteTitle, noteDescription, userId);
                noteService.createNote(note);
            } catch (Exception e) {
                errorMessages.add("Exception occurred when attempting to upload.");
            }
        }
        if (errorMessages.isEmpty()) {
            model.addAttribute("success", true);
        } else {
            model.addAttribute("errors", errorMessages);
        }
        return "result";
    }

    @GetMapping("/delete")
    private String deleteNote(@RequestParam("noteId") int id, Model model) {
        List<String> errorMessages = new ArrayList<>();
        try {
            noteService.removeNote(id);
            model.addAttribute("success", true);
            return "result";
        } catch (Exception e) {
            errorMessages.add("Exception occurred when attempting to delete.");
            model.addAttribute("errors", errorMessages);
            model.addAttribute("success", false);
            return "result";
        }
    }
}
