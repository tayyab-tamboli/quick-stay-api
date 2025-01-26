package com.quickstay.api;

import com.quickstay.exception.UserException;
import com.quickstay.model.*;
import com.quickstay.service.UserAppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/")
public class LoginController {

    @Autowired
    UserAppService userAppService;

    @PostMapping("/register")
    public UserResponse registerUser(@RequestBody User user) throws Exception {
        User registeredUser = userAppService.register(user);
        return getResponseObject(registeredUser, "USER CREATED");
    }

    @PostMapping("/authenticate")
    public UserResponse authenticateUser(@RequestBody UserOTPAuth otpAuth) throws UserException {
        User authenticateUser = userAppService.authenticateUser(otpAuth);
        return getResponseObject(authenticateUser, "USER AUTHENTICATED");
    }

    @PostMapping("/login")
    public AuthenticatedUser login(@RequestBody UserLogin userLogin) throws UserException {
        return userAppService.login(userLogin);
    }

    @PostMapping("/logout")
    public String logout() {
        return "LOGGED OUT";
    }

    @GetMapping("/profile/{username}")
    public UserResponse getProfileByUsername(@PathVariable(value = "username", required = true) String username) throws UserException {
        User user = userAppService.findUserByUsername(username);
        return getResponseObject(user, "USER FOUND");
    }

    private UserResponse getResponseObject(User user, String message) {
        UserResponse userResponse = new UserResponse();
        Response response = new Response(200, "SUCCESS", message);
        userResponse.setUser(user);
        userResponse.setResponse(response);
        return userResponse;
    }
}
