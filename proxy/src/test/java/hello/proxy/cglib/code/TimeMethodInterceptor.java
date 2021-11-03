package hello.proxy.cglib.code;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * CGLIB 는 잘 사용하지 않고, 스프링이 지원하는 ProxyFactory 를 사용한다.
 *
 * <p>* 제약사항
 *
 * <pre>
 *   - 부모 클래스의 생성자를 체크해야한다 -> CGLIB 는 자식 클래스를 동적으로 생성하기 때문에 기본 생성자가 필요하다
 *   - 클래스에 final 이 붙는 경우 상속이 불가능 -> CGLIB 에서 예외가 발생한다.
 *   - 메서드에 final 이 붙는 경우 해당 메서드를 오버라이딩 할 수 없다 -> CGLIB 에서는 프록시 로직이 동작하지 않는다.
 * </pre>
 */
@Slf4j
public class TimeMethodInterceptor implements MethodInterceptor {

  private final Object target;

  public TimeMethodInterceptor(Object target) {
    this.target = target;
  }

  @Override
  public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy)
      throws Throwable {

    log.info("TimeMethodInterceptor 실행");

    long startTime = System.currentTimeMillis();

    // 성능상 MethodProxy 를 사용해야한다고 한다.
    Object result = methodProxy.invoke(target, args);

    long endTime = System.currentTimeMillis();

    long resultTime = endTime - startTime;

    log.info("TimeMethodInterceptor 종료 resultTime={}", resultTime);

    return result;
  }
}
