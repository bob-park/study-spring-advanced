package hello.proxy.jdkdynamic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;

@Slf4j
class ReflectionTest {

  /**
   * reflection 은 가급적 사용하지 말자
   *
   * <p>런타임 시점에만 오류를 확인할 수 있다.
   *
   * <p>framework 개발이나 매우 일반적인 공통 처리가 필요할때만 부분적으로 주의해서 사용해야한다.
   */
  @Test
  void reflectionV0() {

    Hello target = new Hello();

    // 호출 메소드만 다르고 구조는 똑같다.
    // 공통 로직1 시작
    log.info("start");
    String result1 = target.callA();
    log.info("result1={}", result1);

    // 공통 로직2 종료
    log.info("start");
    String result2 = target.callB();
    log.info("result2={}", result2);
  }

  @Test
  void reflectionV1() throws Exception {
    // 클래스 정보 획득
    Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");

    Hello target = new Hello();

    // callA 메소드 정보
    Method methodCallA = classHello.getMethod("callA");

    Object result1 = methodCallA.invoke(target);

    log.info("result1={}", result1);

    // callB 메소드 정보
    Method methodCallB = classHello.getMethod("callB");

    Object result2 = methodCallB.invoke(target);

    log.info("result2={}", result2);
  }

  @Test
  void reflectionV2() throws Exception {
    // 클래스 정보 획득
    Class classHello = Class.forName("hello.proxy.jdkdynamic.ReflectionTest$Hello");

    Hello target = new Hello();

    // callA 메소드 정보
    dynamicCall(classHello.getMethod("callA"), target);

    // callB 메소드 정보
    dynamicCall(classHello.getMethod("callB"), target);
  }

  private void dynamicCall(Method method, Object target) throws Exception {
    log.info("start");

    Object result = method.invoke(target);

    log.info("result={}", result);
  }

  @Slf4j
  static class Hello {
    public String callA() {
      log.info("callA");
      return "A";
    }

    public String callB() {
      log.info("callB");
      return "B";
    }
  }
}
