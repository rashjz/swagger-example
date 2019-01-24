package rashjz.info.app.sw.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import rashjz.info.app.sw.domain.Person;
import rashjz.info.app.sw.domain.validator.ImageSanitizer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
@Slf4j
@Controller
public class FileUploadController {

private final ImageSanitizer imageSanitizer;

    @Autowired
    public FileUploadController(ImageSanitizer imageSanitizer) {
        this.imageSanitizer = imageSanitizer;
    }

    @GetMapping(value = "/upload")
    public String listUploadedFiles(Model model) throws IOException {
        model.addAttribute("person",new Person());
        return "uploadForm";
    }



    @PostMapping(value = "/upload")
    public String handleFileUpload(@RequestParam("file") MultipartFile file) throws IOException {
        log.info("file upload {}",file.getBytes().length);
        log.info("file validation result {} ",imageSanitizer.madeSafe(convert(file)));

        return "uploadForm";
    }

    private File convert(MultipartFile file) {
     File convFile = new File(file.getOriginalFilename());
        try {
            convFile.createNewFile();

        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return convFile;
    }

}