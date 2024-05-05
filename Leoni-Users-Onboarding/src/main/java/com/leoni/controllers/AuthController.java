package com.leoni.controllers;

import com.leoni.models.ERole;
import com.leoni.models.Image;
import com.leoni.models.Role;
import com.leoni.models.User;
import com.leoni.payload.request.LoginRequest;
import com.leoni.payload.request.SignupRequest;
import com.leoni.payload.response.JwtResponse;
import com.leoni.payload.response.MessageResponse;
import com.leoni.repository.ImageRepository;
import com.leoni.repository.RoleRepository;
import com.leoni.repository.UserRepository;
import com.leoni.security.jwt.JwtUtils;
import com.leoni.security.services.UserDetailsImpl;
import com.leoni.security.services.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  private ImageRepository imageRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;
    @Autowired
    private UserDetailsServiceImpl userDetailsServiceImpl;

  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
    SecurityContextHolder.getContext().setAuthentication(authentication);
    String jwt = jwtUtils.generateJwtToken(authentication);
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    List<String> roles = userDetails.getAuthorities().stream()
            .map(item -> item.getAuthority())
            .collect(Collectors.toList());
    byte[] imageData = userDetails.getImageData(); // Fetch image data
    return ResponseEntity.ok(new JwtResponse(jwt,
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getEmail(),
            roles,
            imageData)); // Pass image data to JwtResponse constructor
  }


  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @ModelAttribute SignupRequest signUpRequest,
                                        @RequestPart(value = "image", required = false) MultipartFile imageFile) {
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
    }

    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
    }

    try {
      Image savedImage = null;
      if (imageFile != null) {
        String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
        Image image = new Image(fileName, imageFile.getBytes());
        savedImage = imageRepository.save(image);
      }

      User user = new User(signUpRequest.getUsername(),
              signUpRequest.getEmail(),
              encoder.encode(signUpRequest.getPassword()));

      if (savedImage != null) {
        user.setImage(savedImage);
      }

      Set<String> strRoles = signUpRequest.getRole();
      Set<Role> roles = new HashSet<>();

      if (strRoles == null) {
        Role userRole = roleRepository.findByName(ERole.user)
                .orElseGet(() -> roleRepository.save(new Role(ERole.user)));
        roles.add(userRole);
      } else {
        strRoles.forEach(role -> {
          switch (role) {
            case "admin":
              Role adminRole = roleRepository.findByName(ERole.admin)
                      .orElseGet(() -> roleRepository.save(new Role(ERole.admin)));
              roles.add(adminRole);
              break;
            case "moderator":
              Role modRole = roleRepository.findByName(ERole.moderator)
                      .orElseGet(() -> roleRepository.save(new Role(ERole.moderator)));
              roles.add(modRole);
              break;
            default:
              Role userRole = roleRepository.findByName(ERole.user)
                      .orElseGet(() -> roleRepository.save(new Role(ERole.user)));
              roles.add(userRole);
          }
        });
      }

      user.setRoles(roles);
      userRepository.save(user);

      return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    } catch (IOException e) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Failed to upload image!"));
    }
  }

  @GetMapping("/users")
  public ResponseEntity<List<User>> getAllUsers() {
    List<User> users = userRepository.findAll();
    return ResponseEntity.ok(users);
  }

  @GetMapping("/users/{id}")
  public ResponseEntity<User> getUserById(@PathVariable Long id) {
    return userRepository.findById(id)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/users/{id}")
  public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
    return userRepository.findById(id)
            .map(user -> {
              user.setUsername(updatedUser.getUsername());
              user.setEmail(updatedUser.getEmail());
              user.setPassword(encoder.encode(updatedUser.getPassword()));

              // Retrieve roles from the database based on the names provided in the updated user
              Set<Role> updatedRoles = new HashSet<>();
              for (Role role : updatedUser.getRoles()) {
                Optional<Role> existingRole = roleRepository.findByName(role.getName());
                existingRole.ifPresent(updatedRoles::add);
              }
              user.setRoles(updatedRoles);

              Image updatedImage = updatedUser.getImage();
              if (updatedImage != null) {
                Image savedImage = imageRepository.save(updatedImage);
                user.setImage(savedImage);
              } else {
                user.setImage(null);
              }

              User savedUser = userRepository.save(user);
              return ResponseEntity.ok(savedUser);
            })
            .orElse(ResponseEntity.notFound().build());
  }


  @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable Long id) {
      Optional<User> userOptional = userRepository.findById(id);

      if (userOptional.isPresent()) {
        userRepository.delete(userOptional.get());
        return ResponseEntity.ok().body("User deleted successfully.");
      } else {
        return ResponseEntity.notFound().build();
      }
    }
}
