package com.spe.iiitbcms.JWT;

import com.spe.iiitbcms.model.User;
import com.spe.iiitbcms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@CrossOrigin(origins={"*"})
@RestController
public class JwtController {

    @Autowired
    private AuthenticationManager authenticationManager;


//    @Autowired
//    private Doctor doctor; //added component to admin model

    @Autowired
    private JwtUtil jwtUtil;


    @Autowired
    private UserRepository userRepository;


    @PostMapping("/token")
    public ResponseEntity<?> generateToken(@RequestBody JwtRequest jwtRequest) throws Exception
    {
        System.out.println("Obtained email is " + jwtRequest.getEmail());
        System.out.println("Obtained password is " + jwtRequest.getPassword());

        try
        {
            this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(jwtRequest.getEmail(),
                    jwtRequest.getPassword()));

        }catch(UsernameNotFoundException e){
            e.printStackTrace();

            throw new Exception("Bad Credentials - username not found");
        }catch(BadCredentialsException e){
            e.printStackTrace();

            throw new Exception("Bad Credentials");
        }catch(Exception e)
        {
            e.printStackTrace();
        }

        JwtResponse jwtResponse = new JwtResponse();


//        Optional<User> temp = userRepo.findUserName(jwtRequest.getUsername());

        Optional<User> user = userRepository.findByEmail(jwtRequest.getEmail());


        System.out.println("email is " + user.get().getEmail());
        System.out.println("User  id is " + user.get().getUserId());
        System.out.println("password is " + user.get().getPassword());
        System.out.println("Name of user is " + user.get().getName());


        String token = this.jwtUtil.generateToken(user.get());

        System.out.println("JWT token is "+token);

        jwtResponse.setToken(token);
        jwtResponse.setEmail(jwtRequest.getEmail());
        jwtResponse.setId(user.get().getUserId());
        jwtResponse.setRoll_no(user.get().getRollNo());
        jwtResponse.setExpiresAt(jwtUtil.extractExpiration(token));
        System.out.println("Expires at " + jwtResponse.getExpiresAt());
        return ResponseEntity.ok(jwtResponse);
    }
}

