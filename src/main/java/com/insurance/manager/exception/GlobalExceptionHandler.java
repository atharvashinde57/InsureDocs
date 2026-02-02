package com.insurance.manager.exception;

import com.insurance.manager.controller.DocumentController; // For logging context if needed
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(MaxUploadSizeExceededException exc, RedirectAttributes redirectAttributes) {
        log.warn("File upload exceeded size limit: {}", exc.getMessage());
        redirectAttributes.addFlashAttribute("error", "File too large! User limit is 10MB.");
        return "redirect:/dashboard";
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public String handleStorageFileNotFound(StorageFileNotFoundException exc, RedirectAttributes redirectAttributes) {
        log.error("Storage file not found: {}", exc.getMessage());
        redirectAttributes.addFlashAttribute("error", "The requested file could not be found.");
        return "redirect:/dashboard";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public String handleIllegalArgument(IllegalArgumentException exc, RedirectAttributes redirectAttributes) {
        log.warn("Invalid argument: {}", exc.getMessage());
        redirectAttributes.addFlashAttribute("error", exc.getMessage());
        return "redirect:/dashboard";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneralException(Exception exc, Model model) {
        log.error("An unexpected error occurred", exc);
        model.addAttribute("error", "An unexpected error occurred. Please contact support.");
        model.addAttribute("details", exc.getMessage());
        return "error"; // We will create this template
    }
}
