package dev.blackbeast.springsvn.service;

import dev.blackbeast.springsvn.domain.User;
import dev.blackbeast.springsvn.dto.UserProfileDto;
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

    public void activateUser(Long id) {
        userRepository.activateUser(id);
    }

    public void deactivateUser(Long id) {
        userRepository.deactivateUser(id);
    }

    public void grantAdminPermission(Long id) {
        userRepository.grantAdminPermission(id);
    }

    public void revokeAdminPermission(Long id) {
        userRepository.revokeAdminPermission(id);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    public User get(Long id) {
        return userRepository.findById(id).get();
    }

    public void updateUser(UserProfileDto profile) {
        if(profile != null)
            if(profile.getId() != null) {
                User user = get(profile.getId());
                user.setName(profile.getName());

                if(profile.getPassword() != null)
                    if(!profile.getPassword().isEmpty()) {
                        PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();
                        user.setPassword(pe.encode(profile.getPassword()));
                    }

                userRepository.save(user);
            }
    }
}
