package com.kimmandoo.springboot.servlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

//@RestController // DispatcherServlet과는 직접적인 상관이 없음
//@RequestMapping(value = "/") // 이래야 Bean을 찾을 때 맵핑정보 탐색 가능
@RequestMapping(value = "/hello") // Spring MVC가 부트3.0으로 올라오면서 이방법은 안통하게 됐다.
// 스프링부트 3.0은 Spring 6를 쓰는데, 타입레벨 RequestMapping을 DispatcherServlet이 인식하지 못한다.
@Controller // Controller 애노테이션을 꼭 붙여줘야한다. 그래야 타입레벨 RequestMapping을 알아챈다.
public class HelloManualController {

    private final HelloService helloService; // final로 한번 할당되면 재할당 안되게 막고, private로 외부에서 접근하지 못하게 막는다.

    public HelloManualController(HelloService helloService) { // auto-wired 문제,. private으로 해두면 spring이 자동 빈 생성을 안해줌
        this.helloService = helloService;
    }

    @GetMapping// -> web 요청 Mapping정보를 갖고있는 Bean을 싹 찾는다. 근데 Method 레벨에서는 DispatcherServlet이 못찾음 -> Class Level에도 알려줘야된다.
    // getMapping이 나오기 전에는 requestMapping이 있었다. parameter로 http method를 지정해줘야했다.
//    @RequestMapping(value = "/hello", method = RequestMethod.GET)
    @ResponseBody // 이래야 text/plain으로 String이 return 된다.
    // 근데 RestController라고 class레벨에서 지정을하면 모든 메서드가 ResponseBody 애노테이션이 붙어있다고 가정하게 된다.
    public String hello(String name) {
//        HelloService helloService = new SimpleHelloService();
        // 컨트롤러가 해야되는 중요한 점은 "검증"이다.
        if (name == null) {
            // throw
        }
//        return helloService.sayHello(name);
        return helloService.sayHello(Objects.requireNonNull(name)); // null이면 예외를 던지고, null이 아니면 exception 발생
    }
    // 문제는 String 반환!
    // String반환값으로 이름이 지어진 view가 있는지 먼저 체크하기 때문에 여전히 404 not found가 발생한다

}
