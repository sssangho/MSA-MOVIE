package com.example.controller;

import com.example.config.RabbitConfig;
import com.example.model.UserMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final RabbitTemplate rabbitTemplate;

    // CREATE
    @PostMapping
    public ResponseEntity<UserMessage> create(@RequestBody UserMessage user) {
        try {
            user.setAction("CREATE");
            Object response = rabbitTemplate.convertSendAndReceive(RabbitConfig.USER_REQUEST_QUEUE, user);

            if (response instanceof UserMessage) {
                return ResponseEntity.ok((UserMessage) response);
            }
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    // READ ALL
    @GetMapping
    public ResponseEntity<List<UserMessage>> readAll() {
        UserMessage msg = new UserMessage("READ", null, null, null);
        Object response = rabbitTemplate.convertSendAndReceive(RabbitConfig.USER_REQUEST_QUEUE, msg);

        if (response instanceof List) {
            return ResponseEntity.ok((List<UserMessage>) response);
        } else if (response instanceof UserMessage) {
            return ResponseEntity.ok(Collections.singletonList((UserMessage) response));
        } else {
            return ResponseEntity.ok(Collections.emptyList());
        }
    }

    // READ ONE
    @GetMapping("/{id}")
    public ResponseEntity<UserMessage> readOne(@PathVariable Long id) {
        UserMessage msg = new UserMessage("READ", id, null, null);
        Object response = rabbitTemplate.convertSendAndReceive(RabbitConfig.USER_REQUEST_QUEUE, msg);

        if (response instanceof UserMessage) {
            return ResponseEntity.ok((UserMessage) response);
        }
        return ResponseEntity.notFound().build();
    }

    // UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<UserMessage> update(@PathVariable Long id, @RequestBody UserMessage user) {
        user.setAction("UPDATE");
        user.setId(id);
        Object response = rabbitTemplate.convertSendAndReceive(RabbitConfig.USER_REQUEST_QUEUE, user);

        if (response instanceof UserMessage) {
            return ResponseEntity.ok((UserMessage) response);
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
    }

    // DELETE
    @DeleteMapping("/{id}")
    public ResponseEntity<UserMessage> delete(@PathVariable Long id) {
        UserMessage msg = new UserMessage("DELETE", id, null, null);
        Object response = rabbitTemplate.convertSendAndReceive(RabbitConfig.USER_REQUEST_QUEUE, msg);

        if (response instanceof UserMessage) {
            return ResponseEntity.ok((UserMessage) response);
        }
        return ResponseEntity.notFound().build();
    }
}
