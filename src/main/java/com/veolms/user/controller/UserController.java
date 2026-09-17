//package com.veolms.user.controller;
//
//
//import org.springframework.http.HttpStatus;
//import org.springframework.web.bind.annotation.*;
//import com.veolms.user.dto.CreateUserRequest;
//import com.veolms.user.dto.UserResponse;
//import com.veolms.user.service.UserService;
//
//@RestController
//@RequestMapping("/api/v1/users")
//public class UserController {
//
//    private final UserService userService;
//
//    public UserController(UserService userService) {
//        this.userService = userService;
//    }
//
//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public UserResponse createUser(
//            @RequestBody CreateUserRequest request
//    ) {
//        return userService.createUser(request);
//    }
//
//    @GetMapping("/{id}")
//    public UserResponse getUser(@PathVariable Long id) {
//        return userService.getUser(id);
//    }
//}
