package com.udacity.jwdnd.course1.cloudstorage.controller;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/files")
public class FileController {
    private FileService fileService;
    private UserService userService;

    public FileController(FileService fileService, UserService userService) {
        this.fileService = fileService;
        this.userService = userService;
    }

    @PostMapping
    private String uploadFile(
            Authentication authentication, @RequestParam(name = "file") MultipartFile multipartFile, Model model) {
        List<String> errorMessages = new ArrayList<>();
        if (multipartFile.isEmpty()) {
            errorMessages.add("File cannot be empty.");
            model.addAttribute("errors", errorMessages);
            model.addAttribute("success", false);
            return "result";
        }
        if (!fileService.isFilenameAvailable(multipartFile.getOriginalFilename())) {
            errorMessages.add("File name already exists.");
            model.addAttribute("errors", errorMessages);
            model.addAttribute("success", false);
            return "result";
        }
        try {
            int userId = userService.getUser(authentication.getName()).getUserid();
            File file = new File(
                    multipartFile.getOriginalFilename(),
                    multipartFile.getContentType(),
                    multipartFile.getSize(),
                    userId,
                    multipartFile.getBytes());
            fileService.createFile(file);
            model.addAttribute("success", true);
            return "result";

        } catch (Exception e) {
            errorMessages.add("Exception occurred when attempting to upload.");
            model.addAttribute("errors", errorMessages);
            model.addAttribute("success", false);
            return "result";
        }
    }

    @GetMapping("/delete")
    private String deleteFile(@RequestParam("fileId") int id, Model model) {
        List<String> errorMessages = new ArrayList<>();
        try {
            fileService.removeFile(id);
            model.addAttribute("success", true);
            return "result";
        } catch (Exception e) {
            errorMessages.add("Exception occurred when attempting to delete.");
            model.addAttribute("errors", errorMessages);
            model.addAttribute("success", false);
            return "result";
        }
    }

    @GetMapping("/download")
    private ResponseEntity<ByteArrayResource> downloadFile(@RequestParam("fileId") int id) {
        File file = fileService.getFile(id);
        if (file == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + file.getName())
                .contentType(MediaType.parseMediaType(file.getContentType()))
                .body(new ByteArrayResource(file.getData()));
    }
}
