package top.kingworker.blog.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * 辅助注解 打上这个注解的方法
 * 会使用AOP 来清楚所有的缓存
 */
@Target(value = ElementType.METHOD)
public @interface ClearAllCache {
}
