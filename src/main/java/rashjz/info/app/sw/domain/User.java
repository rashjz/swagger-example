package rashjz.info.app.sw.domain;

import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;

@Entity(name = "USERS")
@Data
@Builder
@AllArgsConstructor@NoArgsConstructor@ToString
public class User implements UserDetails {
    @Id
    @GeneratedValue
    private Long id;
    private String username;
    private String password;
    private boolean isEnabled;
    private boolean isAccountNonExpired;
    private boolean isCredentialsNonExpired;
    private boolean isAccountNonLocked;
    @OneToMany(fetch = FetchType.EAGER, cascade= CascadeType.ALL)
    private Collection<Authority> authorities;

}
