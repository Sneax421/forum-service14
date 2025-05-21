package ait.cohort55.accounting.controller;

import ait.cohort55.accounting.dto.RolesDto;
import ait.cohort55.accounting.dto.UserDto;
import ait.cohort55.accounting.dto.UserEditDto;
import ait.cohort55.accounting.dto.UserRegisterDto;
import ait.cohort55.accounting.service.UserAccountingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
public class UserAccountController {

    private final UserAccountingService userAccountingService;

    @PostMapping("/register")
    public UserDto register(@RequestBody UserRegisterDto userRegisterDto) {
        return userAccountingService.register(userRegisterDto);
    }

    // Not
    @PostMapping("login")
    public UserDto login(Principal principal) {
        // FIXME
        return userAccountingService.getUser(principal.getName());
    }


    @DeleteMapping("/user/{login}")
    public UserDto removeUser(@PathVariable String login) {
        return userAccountingService.removeUser(login);
    }

    @PatchMapping("/user/{login}")
    public UserDto updateUser(@PathVariable String login, @RequestBody UserEditDto userEditDto) {
        return userAccountingService.updateUser(login, userEditDto);
    }

    @PatchMapping("/user/{login}/role/{role}")
    public RolesDto addRole(@PathVariable String login, @PathVariable String role) {
        return userAccountingService.changeRolesList(login, role, true);
    }

    @DeleteMapping("/user/{login}/role/{role}")
    public RolesDto deleteRole(@PathVariable String login, @PathVariable String role) {
        return userAccountingService.changeRolesList(login, role, false);
    }

    //Not
    @PatchMapping("/password")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword( Principal principal, @RequestHeader("X_Password") String newPassword) {
        // FIXME
        userAccountingService.changePassword(principal.getName(), newPassword);
    }

    @GetMapping("/user/{login}")
    public UserDto getUser(@PathVariable String login) {
        return userAccountingService.getUser(login);
    }
}
