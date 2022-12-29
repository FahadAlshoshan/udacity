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
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/files")
public class FileController implements HandlerExceptionResolver {
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
        try {
            fileService.removeFile(id);
            model.addAttribute("success", true);
            return "result";
        } catch (Exception e) {
            model.addAttribute("errors", "Exception occurred when attempting to delete.");
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

//    @ExceptionHandler({MultipartException.class, MaxUploadSizeExceededException.class})
    public String handleFileSizeException(RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute(
                "errors", "Exception occurred when attempting to upload. file size exceeded the limit");
        redirectAttributes.addAttribute("success", false);
        return "redirect:/result";
    }

    @Override
    public ModelAndView resolveException(
            HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) {
        ModelAndView modelAndView = new ModelAndView("result");
        if (e instanceof MaxUploadSizeExceededException) {
            modelAndView
                    .getModel()
                    .put("errors", "Exception occurred when attempting to upload. file size exceeded the limit");
            modelAndView
                    .getModel()
                    .put("success", false);
        }
        return modelAndView;
    }
}
