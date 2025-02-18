package com.kimmandoo.springboot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    // RestController는 HTML을 보내는 것 대신에, API 요청에 대한 응답을 body에 특정 타입으로 인코딩해서 보냄

    @GetMapping("/hello") // HTTP Method가 GET으로 되어있고, url이 hello인것 만 받겠다.
    public String hello(String name) { // controller method에 parameter로 지정해둠
        // -> /hello?name=민규 처럼 들어오는 것을 parameter로 받는다는 의미
        return "hello " + name;
    }
}
