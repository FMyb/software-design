package ru.yandex.ilin.profiler;


import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

@Aspect
public class ProfilerAspect {
    private final Profiler profiler = Profiler.getInstance();

    @Around("execution(* *(..)) && !within(ru.yandex.ilin.profiler..*)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        Signature signature = joinPoint.getSignature();
        String currentPackage = signature.getDeclaringType().getPackage().toString().substring(8);
        if (profiler.getPackagePrefix() == null || !currentPackage.startsWith(profiler.getPackagePrefix())) {
            return joinPoint.proceed();
        }

        long begin = System.nanoTime();
        try {
            return joinPoint.proceed();
        } finally {
            long finish = System.nanoTime();
            profiler.register(signature, finish - begin);
        }
    }
}
