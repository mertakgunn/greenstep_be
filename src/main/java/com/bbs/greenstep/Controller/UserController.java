package com.bbs.greenstep.Controller;



import com.bbs.greenstep.Entity.AuthRequest;
import com.bbs.greenstep.Entity.UserInfo;
import com.bbs.greenstep.Repository.UserInfoRepository;
import com.bbs.greenstep.Service.JwtService;
import com.bbs.greenstep.Service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@CrossOrigin
@RestController
@SecurityRequirement(name = "Bearer Authentication")
@RequestMapping("/auth")
public class UserController {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private UserInfoRepository userInfoRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public String addNewUser(@RequestBody UserInfo userInfo) {
        return userService.addUser(userInfo);
    }

    @PostMapping("/signin")
    public String authenticateAndGetToken(@RequestBody AuthRequest authRequest) {
        Optional<UserInfo> user = userInfoRepository.findByEmail(authRequest.getEmail());
        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found with email: " + authRequest.getEmail());
        }
        UserInfo userInfo = user.get();
        if (passwordEncoder.matches(authRequest.getPassword(), userInfo.getPassword())) {
            return jwtService.generateToken(authRequest.getEmail());
        } else {
            throw new UsernameNotFoundException("Invalid password for user: " + authRequest.getEmail());
        }
    }
    @GetMapping("/me")
    public ResponseEntity<UserInfo> getCurrentUser(@AuthenticationPrincipal UserDetails userDetails) {
        UserInfo currentUser = userInfoRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        currentUser.setPassword(null);
        return ResponseEntity.ok(currentUser);
    }

    @PutMapping("/update")
    public ResponseEntity<UserInfo> updateUser(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UserInfo updatedUserInfo
    ) {
        // Mevcut kullanıcıyı bul
        UserInfo currentUser = userInfoRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));
        // Güncellenebilir alanları set et
        currentUser.setName(updatedUserInfo.getName());
        currentUser.setUlname(updatedUserInfo.getUlname());
        currentUser.setPhone(updatedUserInfo.getPhone());
        currentUser.setAcademic_title(updatedUserInfo.getAcademic_title());
        currentUser.setUniversity_name(updatedUserInfo.getUniversity_name());
        // Şifre güncellemesi kontrol edilir
        if (updatedUserInfo.getPassword() != null && !updatedUserInfo.getPassword().isEmpty()) {
            currentUser.setPassword(passwordEncoder.encode(updatedUserInfo.getPassword()));
        }
        // Kullanıcıyı kaydet
        UserInfo savedUser = userInfoRepository.save(currentUser);

        // Şifreyi geri null yap güvenlik için
        savedUser.setPassword(null);
        return ResponseEntity.ok(savedUser);
    }




}