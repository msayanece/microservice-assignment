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
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        if (!optionalUser.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        return new MyUserDetails(optionalUser.get());
    }

    /**
     * create a user
     * @param user
     * @return
     */
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

    /**
     * update a user
     * @param user
     * @param username
     * @return
     */
    public User updateUser(UserModel user, String username) {
        Optional<User> foundOptionalUser = userRepository.findByUsername(username);
        if (!foundOptionalUser.isPresent()) {
            throw new UsernameNotFoundException("User not found");
        }
        User foundUser = foundOptionalUser.get();
        foundUser.setFirstName(user.getFirstName());
        foundUser.setLastName(user.getLastName());
        foundUser.setEmail(user.getEmail());
        foundUser.setPhone(user.getPhone());
        return userRepository.save(foundUser);
    }

    /**
     * reset/update password
     * @param passwordModel
     * @return
     */
    public boolean resetPassword(PasswordModel passwordModel){
        final BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder(11);
        //check password valid
        if (!passwordModel.getNewPassword().equals(passwordModel.getConfirmPassword())){
            throw new RuntimeException("New Password does not match with Confirm Password");
        }
        String encodedPassword = bCryptPasswordEncoder.encode(passwordModel.getNewPassword());
        //find user
        Optional<User> optionalUser = userRepository.findByUsername(passwordModel.getUsername());
        if (!optionalUser.isPresent()){
            throw new RuntimeException("User not found with email");
        }
        User user = optionalUser.get();
        //update password
        user.setPassword(encodedPassword);
        userRepository.save(user);
        return true;
    }

    /**
     * checks if a username is found in the dataase
     * @param username
     * @return
     */
    public Boolean validateUsername(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        return optionalUser.isPresent();
    }
}
