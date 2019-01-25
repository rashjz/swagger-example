package rashjz.info.app.sw.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import rashjz.info.app.sw.domain.Person;

import javax.validation.Valid;

@Slf4j
@Controller
public class FileUploadController {


    @GetMapping(value = "/upload")
    public String listUploadedFiles(Model model)  {
        model.addAttribute("person",new Person());
        return "uploadForm";
    }

    @PostMapping(value = "/upload")
    public String handleFileUpload(@Valid @RequestParam("file") MultipartFile file) {
        log.info("file upload content type = {} filename = {} name = {} ",file.getContentType(),file.getOriginalFilename(),file.getName());
        return "uploadForm";
    }
}