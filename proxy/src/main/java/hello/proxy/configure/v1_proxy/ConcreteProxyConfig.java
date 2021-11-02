package hello.proxy.configure.v1_proxy;

import hello.proxy.app.v2.OrderControllerV2;
import hello.proxy.app.v2.OrderRepositoryV2;
import hello.proxy.app.v2.OrderServiceV2;
import hello.proxy.configure.v1_proxy.concrete_proxy.OrderControllerConcreteProxy;
import hello.proxy.configure.v1_proxy.concrete_proxy.OrderRepositoryConcreteProxy;
import hello.proxy.configure.v1_proxy.concrete_proxy.OrderServiceConcreteProxy;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConcreteProxyConfig {

  //  @Bean
  //  public OrderControllerV2 orderControllerV2(LogTrace trace) {
  //
  //    OrderControllerV2 controller = new OrderControllerV2(orderServiceV2(trace));
  //
  //    return new OrderControllerConcreteProxy(controller, trace);
  //  }

  @Bean
  public OrderControllerV2 orderControllerV2(OrderServiceV2 orderService, LogTrace trace) {

    OrderControllerV2 controller = new OrderControllerV2(orderService);

    return new OrderControllerConcreteProxy(controller, trace);
  }

  //  @Bean
  //  public OrderServiceV2 orderServiceV2(LogTrace trace) {
  //
  //    OrderServiceV2 service = new OrderServiceV2(orderRepositoryV2(trace));
  //
  //    return new OrderServiceConcreteProxy(service, trace);
  //  }

  @Bean
  public OrderServiceV2 orderServiceV2(OrderRepositoryV2 orderRepository, LogTrace trace) {

    OrderServiceV2 service = new OrderServiceV2(orderRepository);

    return new OrderServiceConcreteProxy(service, trace);
  }

  @Bean
  public OrderRepositoryV2 orderRepositoryV2(LogTrace trace) {
    return new OrderRepositoryConcreteProxy(new OrderRepositoryV2(), trace);
  }
}
