package com.example.demo001;


import javafx.application.Application;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


// https://www.vojtechruzicka.com/javafx-spring-boot/
// https://rgielen.net/posts/2019/introducing-fxweaver-dependency-injection-support-for-javafx-and-fxml/
// https://github.com/vojtechruz/javafx-weaver-example


@SpringBootApplication
public class Demo001SprinBootApplication {

	public static void main(String[] args) {
		//SpringApplication.run(Demo001SprinBootApplication.class, args);

		Application.launch(JavaFxApplication.class, args);
	}

}
