package com.shopsphere.user;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {
    private final UserRepository repository;
    public UserController(UserRepository repository){this.repository=repository;}

    @PostMapping("/register") @ResponseStatus(HttpStatus.CREATED)
    public UserResponse register(@Valid @RequestBody RegisterRequest request){
        if(repository.findByEmailIgnoreCase(request.email()).isPresent()) throw new IllegalArgumentException("Email already registered");
        User u=new User(); u.setName(request.name()); u.setEmail(request.email());
        // Demo-only hash placeholder. Authentication is intentionally a later security phase.
        u.setPasswordHash("DEMO:" + request.password());
        User saved=repository.save(u);
        return new UserResponse(saved.getId(), saved.getName(), saved.getEmail());
    }

    @GetMapping("/{id}")
    public UserResponse get(@PathVariable Long id){
        User u=repository.findById(id).orElseThrow();
        return new UserResponse(u.getId(),u.getName(),u.getEmail());
    }

    public record RegisterRequest(@NotBlank String name,@Email @NotBlank String email,@NotBlank String password){}
    public record UserResponse(Long id,String name,String email){}
}
