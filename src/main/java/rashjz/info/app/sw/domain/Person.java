package rashjz.info.app.sw.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rashjz.info.app.sw.domain.validator.ValidPassword;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Person  implements Serializable {
    private String fullName;
    private String lastName;
    @ValidPassword
    private String userPassword;
    private String uid;
}
