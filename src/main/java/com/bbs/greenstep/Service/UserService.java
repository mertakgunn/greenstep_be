package com.bbs.greenstep.Service;



import com.bbs.greenstep.Entity.UserInfo;
import com.bbs.greenstep.Repository.UserInfoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserInfoRepository repository;

    @Autowired
    private PasswordEncoder encoder;
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<UserInfo> userDetail = repository.findByEmail(email);
        return userDetail.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User not found " + email));
    }
    public Object getAllUsers() {
        return repository.findAll();
    }
    public String addUser(UserInfo userInfo) {
        if(repository.findByEmail(userInfo.getEmail()).isPresent()) {
            return "User Already Exists";
        }
        userInfo.setPassword(encoder.encode(userInfo.getPassword()));
        userInfo.setRoles("user");
        repository.save(userInfo);
        return "User Added Successfully";
    }
}
