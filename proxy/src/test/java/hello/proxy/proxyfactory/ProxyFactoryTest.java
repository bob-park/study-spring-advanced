package hello.proxy.proxyfactory;

import static org.assertj.core.api.Assertions.assertThat;

import hello.proxy.common.advice.TimeAdvice;
import hello.proxy.common.service.ConcreteService;
import hello.proxy.common.service.ServiceImpl;
import hello.proxy.common.service.ServiceInterface;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.aop.framework.ProxyFactory;
import org.springframework.aop.support.AopUtils;

@Slf4j
class ProxyFactoryTest {

    @Test
    void interfaceProxy() {

        ServiceInterface target = new ServiceImpl();

        ProxyFactory proxyFactory = new ProxyFactory(target);

        proxyFactory.addAdvice(new TimeAdvice());

        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.save(); // proxy 실행

        assertThat(AopUtils.isAopProxy(proxy)).isTrue(); // ProxyFactory 사용할때만 사용가능
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isTrue(); // ProxyFactory 사용할때만 사용가능
        assertThat(AopUtils.isCglibProxy(proxy)).isFalse(); // ProxyFactory 사용할때만 사용가능
    }

    @Test
    void concreteProxy() {

        ConcreteService target = new ConcreteService();

        ProxyFactory proxyFactory = new ProxyFactory(target);

        proxyFactory.addAdvice(new TimeAdvice());

        ConcreteService proxy = (ConcreteService) proxyFactory.getProxy();

        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.call(); // proxy 실행

        assertThat(AopUtils.isAopProxy(proxy)).isTrue(); // ProxyFactory 사용할때만 사용가능
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse(); // ProxyFactory 사용할때만 사용가능
        assertThat(AopUtils.isCglibProxy(proxy)).isTrue(); // ProxyFactory 사용할때만 사용가능
    }

    @Test
    void proxyTargetClass() {

        // ! ProxyTargetClass 옵션을 사용하면 인터페이스가 있어도 CGLIB 를 사용하여 proxy 를 만든다.
        // ! 최근 Spring boot (2.0 이상) 의 AOP 에서는 ProxyTargetClass 를 항상 true 로 하여 사용된다.

        ServiceInterface target = new ServiceImpl();

        ProxyFactory proxyFactory = new ProxyFactory(target);
        proxyFactory.setProxyTargetClass(true);

        proxyFactory.addAdvice(new TimeAdvice());

        ServiceInterface proxy = (ServiceInterface) proxyFactory.getProxy();

        log.info("targetClass={}", target.getClass());
        log.info("proxyClass={}", proxy.getClass());

        proxy.save(); // proxy 실행

        assertThat(AopUtils.isAopProxy(proxy)).isTrue(); // ProxyFactory 사용할때만 사용가능
        assertThat(AopUtils.isJdkDynamicProxy(proxy)).isFalse(); // ProxyFactory 사용할때만 사용가능
        assertThat(AopUtils.isCglibProxy(proxy)).isTrue(); // ProxyFactory 사용할때만 사용가능
    }
}
