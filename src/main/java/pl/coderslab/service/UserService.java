package pl.coderslab.service;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.entity.User;
import pl.coderslab.repository.UserRepository;
import javax.servlet.http.HttpSession;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public void registerUser(User user, String repeatedPassword) throws Exception {

        if(user.getPassword().equals(repeatedPassword)){
            user.setPassword(BCrypt.hashpw(user.getPassword(), BCrypt.gensalt()));
            userRepository.save(user);
            return;
        }
        throw new Exception("Password doesn't match");
    }

    public boolean loginUser(String login, String password, HttpSession session) {

        //force logout
        session.setAttribute("user",null);

        //start login user process..
        User user = userRepository.findByUsername(login);
        if(user != null && user.isEnabled() && BCrypt.checkpw(password, user.getPassword())) {
            session.setAttribute("user", user);
            return true;
        } else {
            session.setAttribute("user", null);
            return false;
        }
    }

    public boolean isLoggedIn(HttpSession session) {
        return (session.getAttribute("user") != null);
    }

}
