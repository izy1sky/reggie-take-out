package org.sunshiyi.ExceptionHandler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.sunshiyi.common.R;
import org.sunshiyi.exceptions.CategoryDeleteException;

import java.sql.SQLIntegrityConstraintViolationException;

@RestControllerAdvice
@ResponseBody
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        String message = ex.getMessage();
        String error = "未知错误";
        if (message.contains("Duplicate")) {
            String name = message.split(" ")[2];
            error = name + "已存在！";
        }
        return R.error(error);
    }

    @ExceptionHandler(CategoryDeleteException.class)
    public R<String> exceptionHandler(CategoryDeleteException ex) {
        return R.error(ex.getMessage());
    }

//    @ExceptionHandler(Exception.class)
//    public R<String> exceptionHandler(Exception ex) {
//        return R.error("系统故障！");
//    }
}
