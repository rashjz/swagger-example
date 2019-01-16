package rashjz.info.app.sw.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor@NoArgsConstructor
public class Book {
    private int id;
    private String name;
}
