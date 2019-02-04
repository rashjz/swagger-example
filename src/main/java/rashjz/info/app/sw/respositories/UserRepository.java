package rashjz.info.app.sw.respositories;

import org.springframework.data.repository.CrudRepository;
import rashjz.info.app.sw.domain.User;

import java.util.List;

public interface UserRepository extends CrudRepository<User,Long> {
    List<User> findByUsernameAndPassword(String username,String password);
}
