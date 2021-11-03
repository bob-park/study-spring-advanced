package hello.proxy.jdkdynamic;

import hello.proxy.jdkdynamic.code.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Proxy;

@Slf4j
class JdkDynamicProxyTest {

  @Test
  void dynamicA() {
    AInterface target = new AImpl();

    TimeInvocationHandler handler = new TimeInvocationHandler(target);

    // * interface 가 필수
    // proxy 동적 생성
    // proxy 의 call() 를 호출하게되면, 무조건 InvocationHandler 의 invoke() 가 실행된다.
    // 부가 기능 로직은 동적 proxy 를 사용하여 한번만 개발해서 공통으로 적용할 수 있음
    AInterface proxy =
        (AInterface)
            Proxy.newProxyInstance(
                AInterface.class.getClassLoader(), new Class[] {AInterface.class}, handler);

    String result = proxy.call();

    log.info("targetClass={}", target.getClass());
    log.info("proxyClass={}", proxy.getClass());
    log.info("result={}", result);
  }

  @Test
  void dynamicB() {
    BInterface target = new BImpl();

    TimeInvocationHandler handler = new TimeInvocationHandler(target);

    // proxy 동적 생성
    // proxy 의 call() 를 호출하게되면, 무조건 InvocationHandler.invoke() 가 실행된다.
    BInterface proxy =
        (BInterface)
            Proxy.newProxyInstance(
                BInterface.class.getClassLoader(), new Class[] {BInterface.class}, handler);

    String result = proxy.call();

    log.info("targetClass={}", target.getClass());
    log.info("proxyClass={}", proxy.getClass());
    log.info("result={}", result);
  }
}
