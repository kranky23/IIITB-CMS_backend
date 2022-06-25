package com.spe.iiitbcms.model;

import com.spe.iiitbcms.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class EmailValidator {


    private final UserRepository userRepository;public boolean isEmailPresent(String email)
    {
        Optional<User> byEmail = userRepository.findByEmail(email);

        if(byEmail.isPresent())
            return true;

        return false;
    }
}
