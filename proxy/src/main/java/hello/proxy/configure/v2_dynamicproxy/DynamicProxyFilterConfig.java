package hello.proxy.configure.v2_dynamicproxy;

import hello.proxy.app.v1.*;
import hello.proxy.configure.v2_dynamicproxy.handler.LogTraceBasicHandler;
import hello.proxy.configure.v2_dynamicproxy.handler.LogTraceFilterHandler;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.lang.reflect.Proxy;

@Configuration
public class DynamicProxyFilterConfig {

    private static final String[] PATTERNS = {"request*", "order*", "save*"};

    @Bean
    public OrderControllerV1 orderControllerV1(OrderServiceV1 orderService, LogTrace trace) {
        OrderControllerV1 orderController = new OrderControllerV1Impl(orderService);

        return (OrderControllerV1)
            Proxy.newProxyInstance(
                OrderControllerV1.class.getClassLoader(),
                new Class[]{OrderControllerV1.class},
                new LogTraceFilterHandler(orderController, trace, PATTERNS));
    }

    @Bean
    public OrderServiceV1 orderServiceV1(OrderRepositoryV1 orderRepository, LogTrace trace) {
        OrderServiceV1 orderService = new OrderServiceV1Impl(orderRepository);

        return (OrderServiceV1)
            Proxy.newProxyInstance(
                OrderServiceV1.class.getClassLoader(),
                new Class[]{OrderServiceV1.class},
                new LogTraceFilterHandler(orderService, trace, PATTERNS));
    }

    @Bean
    public OrderRepositoryV1 orderRepositoryV1(LogTrace trace) {

        OrderRepositoryV1 orderRepository = new OrderRepositoryV1Impl();

        return (OrderRepositoryV1)
            Proxy.newProxyInstance(
                OrderRepositoryV1.class.getClassLoader(),
                new Class[]{OrderRepositoryV1.class},
                new LogTraceFilterHandler(orderRepository, trace, PATTERNS));
    }
}
