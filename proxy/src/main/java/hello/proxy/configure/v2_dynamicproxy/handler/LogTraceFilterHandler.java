package hello.proxy.configure.v2_dynamicproxy.handler;

import hello.proxy.trace.TraceStatus;
import hello.proxy.trace.logtrace.LogTrace;
import org.springframework.util.PatternMatchUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class LogTraceFilterHandler implements InvocationHandler {

  private final Object target;
  private final LogTrace trace;
  private final String[] patterns;

  public LogTraceFilterHandler(Object target, LogTrace trace, String[] patterns) {
    this.target = target;
    this.trace = trace;
    this.patterns = patterns;
  }

  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

    // 메서드 이름 필터
    String methodName = method.getName();

    // save, request, reque*, *est 일때만, LogTrace 실행
    if (!PatternMatchUtils.simpleMatch(patterns, methodName)) {
      return method.invoke(target, args);
    }

    TraceStatus status = null;

    try {

      String message = method.getDeclaringClass().getSimpleName() + method.getName();

      status = trace.begin(message);

      // target 호출
      Object result = method.invoke(target, args);

      trace.end(status);

      return result;

    } catch (Exception e) {
      // 해당 method 실행시 exception 이 발생하면, reflect.InvocationTargetException 이 발생함
      trace.exception(status, e);

      throw e;
    }
  }
}
