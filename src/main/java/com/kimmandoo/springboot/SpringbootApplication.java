package com.kimmandoo.springboot;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.catalina.startup.Tomcat;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServer;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.boot.web.servlet.server.ServletWebServerFactory;

import java.io.IOException;

//@SpringBootApplication
public class SpringbootApplication {

    public static void main(String[] args) {
        // 이것만해도 ServletContainer(tomcat)이 뜬다.
//		SpringApplication.run(SpringbootApplication.class, args);

        // 직접 서블릿 컨테이너 띄우기
        // tomcat도 자바임
//		new Tomcat().start(); -> 단순히 이렇게는 안된다.
//		TomcatServletWebServerFactory serverFactory = new TomcatServletWebServerFactory(); // Factory -> 이 자체가 서블릿 웹서버 생성과정을 지원해준다는 의미
        ServletWebServerFactory serverFactory = new TomcatServletWebServerFactory();
        WebServer webServer = serverFactory.getWebServer(new ServletContextInitializer() { // 익명클래스를 parameter로 넣기
            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                // functional interface이기 때문에 lambda로 전환가능 -> SAM
                servletContext.addServlet("hello", new HttpServlet() {
                    @Override
                    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
                        // 웹 요청을 가져오고 응답을 보내는데 필요한 것들을 파라미터로 받는다.
//                        super.service(req, resp);
                        // 웹 응답의 3 요소, Status Line, Headers, Message Body
                        // -> 이걸 코드로 만들어서 반환해주면 된다.
                        resp.setStatus(200); // 응답코드 설정
                        resp.setHeader("Content-Type", "text/plain"); // 헤더
                        // body는 양이많다.
                        resp.getWriter().println("Hello Servlet!"); // 문자열응답만드는데 굉장히 편리한 PrintWriter클래스
                    }


                }).addMapping("/hello"); // /hello로 들어오는 것을 여기서 만든 HttpServlet 익명 오브젝트에서 처리하겠다는 의미
            }
        }); // 진짜 웹 서버(컨테이너)를 만드는 생성함수
        // tomcat이외에도 쓸수있게 추상화가 되어있음
        webServer.start(); // TomcatServletContainer가 이제 동작함

    }

}
