package rashjz.info.app.sw.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

@Data
@Builder
public class Authority implements GrantedAuthority {
    private String authority;
}
