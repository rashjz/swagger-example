package rashjz.info.app.sw.domain.validator;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
@Component
public class ImageValidator  implements ConstraintValidator<ValidImage, MultipartFile> {


    @Override
    public void initialize(ValidImage constraintAnnotation) {
    }

    @Override
    public boolean isValid(MultipartFile multipartFile, ConstraintValidatorContext constraintValidatorContext) {
        return isImage(multipartFile);
    }

    private boolean isImage(MultipartFile file) {
        try {
            return ImageIO.read(file.getInputStream()) != null;
        } catch (Exception e) {
            log.error("Provided file is not valid image type");
            return false;
        }
    }

}