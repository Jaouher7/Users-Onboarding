package com.leoni.controllers;


import com.leoni.models.ERole;
import com.leoni.models.Image;
import com.leoni.models.Role;
import com.leoni.models.User;
import com.leoni.payload.request.LoginRequest;
import com.leoni.payload.request.SignupRequest;
import com.leoni.payload.request.UpdateUserRequest;
import com.leoni.payload.response.JwtResponse;
import com.leoni.payload.response.MessageResponse;
import com.leoni.payload.response.UserResponse;
import com.leoni.repository.ImageRepository;
import com.leoni.repository.RoleRepository;
import com.leoni.repository.UserRepository;
import com.leoni.security.jwt.JwtUtils;
import com.leoni.security.services.UserDetailsImpl;
import com.leoni.security.services.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import java.util.*;
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
            imageData));
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
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(imageFile.getOriginalFilename()));
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
  public ResponseEntity<?> getAllUsers() {
    List<User> users = userRepository.findAll(); // Assuming you have a UserRepository
    List<UserResponse> userResponses = users.stream()
            .map(UserDetailsImpl::build) // Use UserDetailsImpl.build method
            .map(userDetails -> {
              List<String> roles = userDetails.getAuthorities().stream()
                      .map(item -> item.getAuthority())
                      .collect(Collectors.toList());
              byte[] imageData = userDetails.getImageData(); // Fetch image data
              return new UserResponse(
                      userDetails.getId(),
                      userDetails.getUsername(),
                      userDetails.getEmail(),
                      roles,
                      imageData
              );
            })
            .collect(Collectors.toList());
    return ResponseEntity.ok(userResponses);
  }

  @GetMapping("/users/{id}")
  public ResponseEntity<?> getUserById(@PathVariable Long id) {
    Optional<User> userOptional = userRepository.findById(id); // Assuming you have a UserRepository
    if (userOptional.isPresent()) {
      UserDetailsImpl userDetails = UserDetailsImpl.build(userOptional.get()); // Use UserDetailsImpl.build method
      List<String> roles = userDetails.getAuthorities().stream()
              .map(item -> item.getAuthority())
              .collect(Collectors.toList());
      byte[] imageData = userDetails.getImageData(); // Fetch image data
      UserResponse userResponse = new UserResponse(
              userDetails.getId(),
              userDetails.getUsername(),
              userDetails.getEmail(),
              roles,
              imageData
      );
      return ResponseEntity.ok(userResponse);
    } else {
      return ResponseEntity.notFound().build();
    }
  }
/*
  @PutMapping("/{id}")
  public ResponseEntity<?> updateUser(@PathVariable Long id,
                                      @RequestParam(required = false) String username,
                                      @RequestParam(required = false) String email,
                                      @RequestParam(required = false) String password,
                                      @RequestParam(required = false) Set<String> roles,
                                      @RequestPart(value = "image", required = false) MultipartFile imageFile) {
    Optional<User> userOptional = userRepository.findById(id);
    if (userOptional.isPresent()) {
      User user = userOptional.get();

      // Update username, email, and password if provided
      if (username != null) {
        user.setUsername(username);
      }
      if (email != null) {
        user.setEmail(email);
      }
      if (password != null) {
        user.setPassword(Encoder.encode(password));
      }

      // Update roles if provided
      if (roles != null) {
        Set<Role> updatedRoles = new HashSet<>();
        for (String roleName : roles) {
          Optional<Role> roleOptional = roleRepository.findByName(ERole.valueOf(roleName));
          roleOptional.ifPresent(updatedRoles::add);
        }
        user.setRoles(updatedRoles);
      }

      // Handle image upload if provided
      if (imageFile != null && !imageFile.isEmpty()) {
        try {
          String fileName = StringUtils.cleanPath(imageFile.getOriginalFilename());
          Image image = new Image(fileName, imageFile.getBytes());
          Image savedImage = imageRepository.save(image);
          user.setImage(savedImage);
        } catch (IOException e) {
          e.printStackTrace();
          return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
      }

      // Save the updated user
      userRepository.save(user);

      return ResponseEntity.ok("User updated successfully");
    } else {
      return ResponseEntity.notFound().build();
    }
  }*/

  @PostMapping("/update/{id}")
  public ResponseEntity<?> updateUser(@PathVariable("id") Long id,
                                      @Valid @ModelAttribute UpdateUserRequest updateRequest,
                                      @RequestPart(value = "image", required = false) MultipartFile imageFile) {
    Optional<User> userOptional = userRepository.findById(id);
    if (!userOptional.isPresent()) {
      return ResponseEntity.notFound().build();
    }

    User user = userOptional.get();

    // Update username if provided and not taken by another user
    if (updateRequest.getUsername() != null && !updateRequest.getUsername().isEmpty()
            && !user.getUsername().equals(updateRequest.getUsername())
            && userRepository.existsByUsername(updateRequest.getUsername())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
    }

    // Update email if provided and not already in use by another user
    if (updateRequest.getEmail() != null && !updateRequest.getEmail().isEmpty()
            && !user.getEmail().equals(updateRequest.getEmail())
            && userRepository.existsByEmail(updateRequest.getEmail())) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
    }

    try {
      // Update profile image if provided
      Image savedImage = null;
      if (imageFile != null) {
        String fileName = StringUtils.cleanPath(Objects.requireNonNull(imageFile.getOriginalFilename()));
        Image image = new Image(fileName, imageFile.getBytes());
        savedImage = imageRepository.save(image);
        // Remove old image if exists
        if (user.getImage() != null) {
          imageRepository.delete(user.getImage());
        }
      }

      // Update user's information
      if (updateRequest.getUsername() != null && !updateRequest.getUsername().isEmpty()) {
        user.setUsername(updateRequest.getUsername());
      }
      if (updateRequest.getEmail() != null && !updateRequest.getEmail().isEmpty()) {
        user.setEmail(updateRequest.getEmail());
      }
      if (updateRequest.getPassword() != null && !updateRequest.getPassword().isEmpty()) {
        user.setPassword(encoder.encode(updateRequest.getPassword()));
      }
      if (savedImage != null) {
        user.setImage(savedImage);
      }

      userRepository.save(user);

      // Prepare response DTO
      UserResponse userResponse = new UserResponse(
              user.getId(),
              user.getUsername(),
              user.getEmail(),
              user.getRoles().stream().map(role -> role.getName().name()).collect(Collectors.toList()),
              user.getImage() != null ? user.getImage().getImageData() : null
      );

      return ResponseEntity.ok(userResponse);
    } catch (IOException e) {
      return ResponseEntity.badRequest().body(new MessageResponse("Error: Failed to upload image!"));
    }
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
