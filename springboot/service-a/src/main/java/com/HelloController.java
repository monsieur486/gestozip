package com.mr486.servicea;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/service-a/hello")
public class HelloController {

    @GetMapping
    public ApiResponse<String> hello() {
        return new ApiResponse<>(true, "Hello from service-a !", "Hello from service-a !");
    }
}
