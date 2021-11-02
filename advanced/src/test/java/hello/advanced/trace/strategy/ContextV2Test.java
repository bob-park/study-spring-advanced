package hello.advanced.trace.strategy;

import hello.advanced.trace.strategy.code.ContextV2;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class ContextV2Test {

  @Test
  void strategyV1() {

    ContextV2 context = new ContextV2();

    context.execute(() -> log.info("비지니스 로직1 실행"));
    context.execute(() -> log.info("비지니스 로직2 실행"));
  }
}
