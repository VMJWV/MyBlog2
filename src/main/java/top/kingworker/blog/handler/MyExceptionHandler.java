package top.kingworker.blog.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;
import top.kingworker.blog.entity.Message;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@ControllerAdvice
public class MyExceptionHandler {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler(Exception.class)
    public String exceptionHandler(HttpServletRequest request,Exception e) throws Exception {
        logger.error("Request URL: {},Exception : {}",request.getRequestURL(),e);
//        这里巨几把坑 这个返回的String 前面一定不要带/ 否则打包过后会找不到page
        return "error/error";
    }

    @ExceptionHandler(value = BindException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ModelAndView violationException(BindException exception) {
        // 不带任何参数访问接口,会抛出 BindException
        // 因此，我们只需捕获这个异常，并返回我们设置的 message 即可
        System.out.println("接收到表单错误异常");
        ModelAndView modelAndView = new ModelAndView("error/illegal");
        modelAndView.addObject("msg",exception.getAllErrors().get(0).getDefaultMessage());
        return modelAndView;
    }
}
