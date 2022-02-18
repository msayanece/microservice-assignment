package com.sayan.userauth.services;

import com.sayan.userauth.models.MyUserDetails;
import com.sayan.userauth.entities.User;
import com.sayan.userauth.models.PasswordModel;
import com.sayan.userauth.models.UserModel;
import com.sayan.userauth.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        return new MyUserDetails(user);
    }

    public User insertNewUser(UserModel user){
        final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(11);

        //create the User entity
        User userEntity = User.builder()
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role("USER")
                .username(user.getUsername())
                .password(bCryptPasswordEncoder.encode(user.getPassword()))
                .build();
        return userRepository.save(userEntity);
    }

    public User updateUser(UserModel user, String username) {
        User foundUser = userRepository.findByUsername(username);
        if (foundUser == null) {
            throw new UsernameNotFoundException("User not found");
        }
        foundUser.setFirstName(user.getFirstName());
        foundUser.setLastName(user.getLastName());
        foundUser.setEmail(user.getEmail());
        foundUser.setPhone(user.getPhone());
        return userRepository.save(foundUser);
    }

    public User changePassword(PasswordModel passwordModel, String username){
        final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(11);
        //find user
        User user = userRepository.findByUsername(username);
        //validate password
        if (!bCryptPasswordEncoder.matches(passwordModel.getOldPassword(), user.getPassword())){
            throw new RuntimeException("Old Password does not match");
        }
        if (!passwordModel.getNewPassword().equals(passwordModel.getConfirmPassword())){
            throw new RuntimeException("New Password does not match with Confirm Password");
        }
        //update password
        user.setPassword(passwordModel.getNewPassword());
        return userRepository.save(user);
    }

    public Boolean validateUserEmail(String emailId) {
        return true;
    }
}
