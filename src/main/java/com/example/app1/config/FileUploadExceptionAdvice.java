package com.example.app1.config; 

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class FileUploadExceptionAdvice {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String handleMaxSizeException(MaxUploadSizeExceededException exc, 
                                         RedirectAttributes redirectAttributes) {
        
        redirectAttributes.addFlashAttribute("erro", "O arquivo enviado é muito grande! O limite máximo é 50MB.");
        
       
        return "redirect:/restaurantes"; 
    }
}
