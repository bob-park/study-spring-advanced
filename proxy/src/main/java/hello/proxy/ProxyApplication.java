package hello.proxy;

import hello.proxy.configure.AppV1Config;
import hello.proxy.configure.AppV2Config;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

//@Import(AppV1Config.class)
@Import(AppV2Config.class)
@SpringBootApplication(scanBasePackages = "hello.proxy.app")
public class ProxyApplication {

  public static void main(String[] args) {
    SpringApplication.run(ProxyApplication.class, args);
  }
}
