package cqu.jsjds;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.web.bind.annotation.RequestMapping;

@SpringBootApplication
@ServletComponentScan  // 启用自动扫描 Web 组件（包括过滤器）
public class JsjdsApplication {

    public static void main(String[] args) {
        SpringApplication.run(JsjdsApplication.class, args);
    }

}
