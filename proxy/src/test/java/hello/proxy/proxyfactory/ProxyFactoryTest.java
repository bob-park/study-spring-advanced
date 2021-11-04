package hello.proxy.proxyfactory;

import static org.assertj.core.api.Assertions.assertThat;

import hello.proxy.common.advice.TimeAdvice;
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
}
