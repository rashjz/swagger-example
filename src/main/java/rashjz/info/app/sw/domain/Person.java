package rashjz.info.app.sw.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Person  implements Serializable {
    private String fullName;
    private String lastName;
    private String userPassword;
    private String uid;
}
