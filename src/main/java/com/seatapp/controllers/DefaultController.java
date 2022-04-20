package com.seatapp.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Default restcontroller.
 */
@RestController()
@RequestMapping("/api")
public class DefaultController {

    /**
     * First test method.
     * @return returns "Hello World!"
     */
    @GetMapping("/test")
    public ResponseEntity<String> getTest() {
        return ResponseEntity.ok("Hello World!");
    }

}
