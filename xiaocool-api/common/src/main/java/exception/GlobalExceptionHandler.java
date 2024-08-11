package exception;

import domain.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {


    /**
     * 全局运行时异常拦截器
     *
     * @param e
     * @return
     */
    @ExceptionHandler(BaseException.class)
    public Result ExceptionHandler(BaseException e) {
        log.info("异常信息为:{}", e.getMessage());
        return Result.error(e.getMessage());
    }

    /**
     * 全局异常拦截器
     *
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result ExceptionHandler(Exception e) {
        log.info("异常信息为:{}", e.getMessage());
        return Result.error(e.getMessage());
    }
}