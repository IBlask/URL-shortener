package requests;

import com.shorty.shorty.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class RequestRegister {
    //TODO Check RequestRegister
    private String accountID;

    public RequestRegister(String accountID) {
        this.accountID = accountID;
    }

    public String getAccountID() {
        return accountID;
    }

    public boolean isEmpty() {
        return accountID == null;
    }

    public boolean accountIdIsNotSent() {
        return accountID == "";
    }

    public boolean accountIdExists(UserRepository userRepository) {
        return !userRepository.findByUsername(this.accountID).isEmpty();
    }
}
