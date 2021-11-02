package hello.proxy.pureproxy.concretproxy.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeProxy extends ConcreteLogic {

  private final ConcreteLogic target;

  public TimeProxy(ConcreteLogic target) {
    this.target = target;
  }

  @Override
  public String operation() {

    log.info("TimeProxy 실행");

    long startTime = System.currentTimeMillis();

    String result = target.operation();

    long endTime = System.currentTimeMillis();

    long resultTime = endTime - startTime;
    log.info("TimeProxy 종료 resultTime={}", resultTime);

    return result;
  }
}
