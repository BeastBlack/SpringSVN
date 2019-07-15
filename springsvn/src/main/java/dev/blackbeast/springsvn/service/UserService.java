package dev.blackbeast.springsvn.service;

import dev.blackbeast.springsvn.domain.User;
import dev.blackbeast.springsvn.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public void save(User user) {
        if(user.getIsAdmin() == null)
            user.setIsAdmin(false);

        if(user.getIsEnabled() == null)
            if(user.getIsAdmin() != null)
                if(user.getIsAdmin())
                    user.setIsEnabled(true);
                else
                    user.setIsEnabled(false);

        PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        user.setPassword(pe.encode(user.getPassword()));

        userRepository.save(user);
    }

    public Boolean exists(String username) {
        return !userRepository.findByUsername(username).isEmpty();
    }

    public User getLoggedUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null){
            String username = auth.getName();
            List<User> users = userRepository.findByUsername(username);

            if(!users.isEmpty())
                return users.get(0);
            else
                return null;
        }else
            return null;
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }
}
