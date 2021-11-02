package hello.proxy.configure.v1_proxy;

import hello.proxy.app.v1.*;
import hello.proxy.configure.v1_proxy.interface_proxy.OrderControllerInterfaceProxy;
import hello.proxy.configure.v1_proxy.interface_proxy.OrderRepositoryInterfaceProxy;
import hello.proxy.configure.v1_proxy.interface_proxy.OrderServiceInterfaceProxy;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InterfaceProxyConfig {

  @Bean
  public OrderControllerV1 orderController(LogTrace trace) {

    OrderControllerV1Impl controllerImpl = new OrderControllerV1Impl(orderService(trace));

    return new OrderControllerInterfaceProxy(controllerImpl, trace);
  }

  @Bean
  public OrderServiceV1 orderService(LogTrace trace) {

    OrderServiceV1Impl serviceImpl = new OrderServiceV1Impl(orderRepository(trace));

    return new OrderServiceInterfaceProxy(serviceImpl, trace);
  }

  @Bean
  public OrderRepositoryV1 orderRepository(LogTrace trace) {
    return new OrderRepositoryInterfaceProxy(new OrderRepositoryV1Impl(), trace);
  }
}
