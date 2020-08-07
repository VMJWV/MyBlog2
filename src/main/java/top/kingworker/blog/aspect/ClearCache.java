package top.kingworker.blog.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Set;

@Aspect
@Component
public class ClearCache {
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RedisTemplate redisTemplate;

    @Pointcut(value = "execution(* top.kingworker.blog.service.*.* (..)) && @annotation(top.kingworker.blog.aspect.ClearAllCache))")
    private void pointCut() {
    }

    /**
     * 这个方法用于在改增方法过后删除分页的缓存
     *
     * @param joinPoint
     */
    @AfterReturning(value = "pointCut()")
    private void process(JoinPoint joinPoint) {
        //获取被代理的类
        Object target = joinPoint.getTarget();
        //获取方法签名
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        //获取切入方法
        Method method = signature.getMethod();
        //获取切入方法的参数
        Object[] args = joinPoint.getArgs();
        //发生了改和删 应该是对应的分页缓存全部删除 因为我们的分页数据也缓存了
        // 如果是label发生变化 那么文章也应该重新进行查询
        // 有点不合理 但是没想到更加合理的方法
        Set<String> keys = redisTemplate.keys("*");
        redisTemplate.delete(keys);
        logger.info("remove caches {}", keys);
    }

}
