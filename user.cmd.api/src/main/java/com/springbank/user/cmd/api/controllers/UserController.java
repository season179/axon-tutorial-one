package com.springbank.user.cmd.api.controllers;

import com.springbank.user.cmd.api.commands.RegisterUserCommand;
import com.springbank.user.cmd.api.commands.RemoveUserCommand;
import com.springbank.user.cmd.api.commands.UpdateUserCommand;
import com.springbank.user.cmd.api.dto.RegisterUserResponse;
import com.springbank.user.core.dto.BaseResponse;
import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping(path = "/api/v1/user")
public class UserController {

    private final CommandGateway commandGateway;

    @Autowired
    public UserController(CommandGateway commandGateway) {
        this.commandGateway = commandGateway;
    }


    @PostMapping
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity<RegisterUserResponse> registerUser(@Valid @RequestBody RegisterUserCommand command) {
        var id = UUID.randomUUID().toString();
        command.setId(id);

        try {
            commandGateway.send(command);

            return new ResponseEntity<>(new RegisterUserResponse(id, "User successfully registered!"), HttpStatus.CREATED);
        } catch (Exception ex) {
            var safeErrorMessage = "Error while processing register user request for id: " + id;
            System.out.println(ex.toString());

            return new ResponseEntity<>(new RegisterUserResponse(id, safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity<BaseResponse> updateUser(@PathVariable(value = "id") String id, @Valid @RequestBody UpdateUserCommand command) {
        try {
            command.setId(id);
            commandGateway.send(command);

            return new ResponseEntity<>(new BaseResponse("User successfully updated."), HttpStatus.OK);
        } catch (Exception exception) {
            var safeErrorMessage = "Error while processing update user request for id: " + id;
            System.out.println(exception.toString());

            return new ResponseEntity<>(new BaseResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('WRITE_PRIVILEGE')")
    public ResponseEntity<BaseResponse> removeUser(@PathVariable(value = "id") String id) {
        try {
            commandGateway.send(new RemoveUserCommand(id));

            return new ResponseEntity<>(new BaseResponse("User successfully removed"), HttpStatus.OK);
        } catch (Exception exception) {
            var safeErrorMessage = "Error while processing remove user request for id: " + id;
            System.out.println(exception.toString());

            return new ResponseEntity<>(new BaseResponse(safeErrorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
