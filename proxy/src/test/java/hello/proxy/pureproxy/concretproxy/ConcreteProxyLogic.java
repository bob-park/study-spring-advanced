package hello.proxy.pureproxy.concretproxy;

import hello.proxy.pureproxy.concretproxy.code.ConcreteClient;
import hello.proxy.pureproxy.concretproxy.code.ConcreteLogic;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
class ConcreteProxyLogic {

  @Test
  void noProxy() {
    ConcreteLogic concreteLogic = new ConcreteLogic();
    ConcreteClient client = new ConcreteClient(concreteLogic);

    client.execute();
  }
}
