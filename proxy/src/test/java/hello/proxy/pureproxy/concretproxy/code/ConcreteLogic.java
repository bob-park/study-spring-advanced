package hello.proxy.pureproxy.concretproxy.code;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ConcreteLogic {

  public String operaction() {
    log.info("ConcreteLogic 실행");
    return "data";
  }
}
