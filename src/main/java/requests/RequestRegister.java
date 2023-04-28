package requests;

import com.shorty.shorty.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RequestRegister {
    //TODO Check RequestRegister

    private String accountID;

    public String getAccountID() {
        return accountID;
    }




    public boolean accountIdExists(UserRepository userRepository) {
        List users = new ArrayList<>();
        userRepository.findByUsername(this.getAccountID()).forEach(user -> users.add(user));

        if (users.size() == 0) return false;
        return true;
    }
}
