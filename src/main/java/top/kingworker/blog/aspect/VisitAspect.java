package top.kingworker.blog.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import top.kingworker.blog.service.VisitRecordService;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@Aspect
public class VisitAspect {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    RedisTemplate redisTemplate;

    @Autowired
    VisitRecordService visitRecordService;

    @Pointcut("execution(* top.kingworker.blog.controller.*.* (..))")
    public void pointCut(){};

    @Before("pointCut()")
    public void beforeVisit(JoinPoint jointPoint){
        Signature signature = jointPoint.getSignature();
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String requestURL = request.getRequestURL().toString();
        String ip = getIpAdrress(request);
        String classMethod = signature.getDeclaringTypeName() + "." + signature.getName();
        RequestVisitor requestVisitor = new RequestVisitor(requestURL,ip,classMethod);
        logger.info("Request : {}",requestVisitor);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date currentDate = new Date();
        String todayString = simpleDateFormat.format(currentDate);
        if(redisTemplate.opsForValue().get(ip) == null){
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR,0);
            calendar.set(Calendar.MINUTE,0);
            calendar.set(Calendar.SECOND,0);
            calendar.add(Calendar.DAY_OF_MONTH,1);
            //当前ip没有访问过网站 设置过期时间为明天0点
            redisTemplate.opsForValue().set(ip,"exist",
                    calendar.getTimeInMillis()-currentDate.getTime(),TimeUnit.MILLISECONDS);
            visitRecordService.addToDayVisitCount(todayString);
            //只保存当天用户第一次访问的时候的访问纪录
            //有Bug 当缓存清空的时候 又会在添加一次
            visitRecordService.insertVisitHistory(ip, requestURL,
                    classMethod.substring(classMethod.lastIndexOf("blog.") + 5), currentDate);
        }
    }

    private class RequestVisitor{
        private String url;
        private String ip;
        private String classMethod;

        public RequestVisitor(String url, String ip, String classMethod) {
            this.url = url;
            this.ip = ip;
            this.classMethod = classMethod;
        }

        @Override
        public String toString() {
            return "RequestVisitor{" +
                    "url='" + url + '\'' +
                    ", ip='" + ip + '\'' +
                    ", classMethod='" + classMethod + '\'' +
                    '}';
        }
    }
    private String getIpAdrress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip.split(",").length > 1) {
            ip = ip.split(",")[0];
        }
        return ip;
    }

}
