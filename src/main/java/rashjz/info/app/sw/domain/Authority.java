package rashjz.info.app.sw.domain;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Data
@Builder
@AllArgsConstructor@NoArgsConstructor@ToString
public class Authority implements GrantedAuthority {
    @Id
    @GeneratedValue
    private Long id;
    private String authority;
}
