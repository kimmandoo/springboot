package com.kimmandoo.springboot.servlet;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;
import org.springframework.context.support.GenericApplicationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.context.support.GenericWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import java.io.IOException;

//@SpringBootApplication
public class ManualSpringbootApplication { // springboot 없이 webcontainer 띄우고 처리하는 방법 알아보기

    public static void main(String[] args) {
        // 이것만해도 ServletContainer(tomcat)이 뜬다.
//		SpringApplication.run(SpringbootApplication.class, args);

        // 내부 동작에 접근할 수 있도록 ApplicationContext가 필요하다.
//        GenericApplicationContext genericApplicationContext = new GenericApplicationContext(); // SpringContainer에서는 이게 자동으로 돌아간다
        GenericWebApplicationContext genericApplicationContext = new GenericWebApplicationContext(); // SpringContainer에서는 이게 자동으로 돌아간다
        genericApplicationContext.registerBean(HelloManualController.class); // bean 등록하기
        genericApplicationContext.registerBean(SimpleHelloService.class); // Bean에 등록하면 의존관계를 runtime에 맵핑해준다.
        // 이렇게 service를 등록해두면, controller에서 생성자 주입까지 해준다.
        // 스프링 컨테이너가 bean 구성 정보를 갖고있게 됨
        genericApplicationContext.refresh(); // bean 오브젝트 생성해주는 초기화
        // 이게 스프링컨테이너가 초기화되는시점이다.!

        // 직접 서블릿 컨테이너 띄우기
        // tomcat도 자바임
//		new Tomcat().start(); -> 단순히 이렇게는 안된다.
//		TomcatServletWebServerFactory serverFactory = new TomcatServletWebServerFactory(); // Factory -> 이 자체가 서블릿 웹서버 생성과정을 지원해준다는 의미
        ServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
        WebServer webServer = serverFactory.getWebServer(new ServletContextInitializer() { // 익명클래스를 parameter로 넣기

            // Controller 초기화
//            HelloManualController helloManualController = new HelloManualController(); // Bean으로 등록한 시점에서 이제 필요가 없다.

            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                // functional interface이기 때문에 lambda로 전환가능 -> SAM

//                servletContext.addServlet("hello", new HttpServlet() {
//                    @Override
//                    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//                        // 웹 요청을 가져오고 응답을 보내는데 필요한 것들을 파라미터로 받는다.
////                        super.service(req, resp);
//                        // 웹 응답의 3 요소, Status Line, Headers, Message Body
//                        // -> 이걸 코드로 만들어서 반환해주면 된다.
////                        resp.setStatus(200); // 응답코드 설정
//                        resp.setStatus(HttpStatus.OK.value()); // 응답코드 설정
////                        resp.setHeader("Content-Type", "text/plain"); // 헤더
//                        resp.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE); // 헤더 <- 주어진 enum을 잘 쓰자.
//                        // body는 양이많다.
//                        String name = req.getParameter("name"); // query string 에서 받아오기
//                        resp.getWriter().println("Hello Servlet! Nice to meet you "+name); // 문자열응답만드는데 굉장히 편리한 PrintWriter클래스
//                    }
//
//
//                }).addMapping("/hello"); // /hello로 들어오는 것을 여기서 만든 HttpServlet 익명 오브젝트에서 처리하겠다는 의미
////                }).addMapping("/*"); // 모든 요청을 처리하겠다
//                }

//                servletContext.addServlet("frontcontroller", new HttpServlet() {
//                    @Override
//                    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//                        // 인증, 보안, 다국어, 보안기능
//                        // mapping은 요청을 가지고 한다.! -> 헤더에 많은 정보가 있기 때문
//                        // Binding을 매번 서블릿 코드에 넣을 수는 없다 -> DispatcherServlet을 쓰자
//                        if (req.getRequestURI().equals("/hello") && req.getMethod().equals(HttpMethod.GET.name())) { // GETMapping과 동일한 효과가 일어난다.
//                            String name = req.getParameter("name");
//
//                            HelloManualController helloManualControllerBean = genericApplicationContext.getBean(HelloManualController.class); // context에서 bean 가져오기
//                            // 매우 중요한 부분 -> 서블릿컨테이너 쪽에서는 controller 오브젝트의 생성과정은 아예 모른다.
////                            String hello = helloManualController.hello(name); // Controller로 로직분리
//                            String hello = helloManualControllerBean.hello(name); // Controller로 로직분리
//
//
//                            // 요청 받아서 처리하는 중요한 단계 -> 매핑과 바인딩
//                            // 웹요청에 들어있는 정보로 어떤 작업을 수행할 것인지 판단하는 과정 -> 매핑
//                            // 바인딩 -> 컨트롤러가 Web에 바로 노출되어있지않는데, 이걸 분리해서 사용함.
//                            // String Type으로 hello를 반환하는 게 binding이라고 할 수 있다.
//                            resp.setStatus(HttpStatus.OK.value()); // 응답코드 설정
////                            resp.setHeader(HttpHeaders.CONTENT_TYPE, MediaType.TEXT_PLAIN_VALUE);
//                            resp.setContentType(MediaType.TEXT_PLAIN_VALUE);
//
//                            resp.getWriter().println(hello);
//                        } else if (req.getRequestURI().equals("/user")) {
//                            //
//                        } else {
//                            resp.setStatus(HttpStatus.NOT_FOUND.value());
//                        }
//
//                    }
//                }).addMapping("/*"); // 모든 요청을 처리하겠다

                servletContext.addServlet("dispatcherServlet",
                        // Spring의 오래전부터 들어있던 frontcontroller의많은 기능을 수행해주는 놈을 쓸것임
//                        new DispatcherServlet(genericApplicationContext) // dispatcher servlet은 WebApplicationContext만 parameter로 받는다!
                        new DispatcherServlet(genericApplicationContext)
                ).addMapping("/*"); // 모든 요청을 처리하겠다, 근데 맵핑 정보가 없기 때문에 그냥 이렇게 쓰면 무조건 404 에러 발생
            }

        }); // 진짜 웹 서버(컨테이너)를 만드는 생성함수
        // tomcat이외에도 쓸수있게 추상화가 되어있음
        webServer.start(); // TomcatServletContainer가 이제 동작함

    }

}
