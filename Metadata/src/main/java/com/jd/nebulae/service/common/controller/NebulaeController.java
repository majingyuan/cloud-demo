package com.jd.nebulae.service.common.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.google.common.base.Function;
import com.google.common.base.Throwables;
import com.google.common.collect.Lists;
import com.jd.nebulae.service.common.exceptions.NebulaeExistsException;
import com.jd.nebulae.service.common.exceptions.NebulaeNotExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("AlibabaClassMustHaveAuthor")
public class NebulaeController {

    private Logger log = LoggerFactory.getLogger(getClass());

    private static String toJson(final Object result) {
        return JSON.toJSONString(JSON.parseObject(JSON.toJSONString(result,
                SerializerFeature.WriteNullListAsEmpty, SerializerFeature.WriteDateUseDateFormat))
                , new ValueFilter() {
                    @Override
                    public Object process(Object o, String s, Object o1) {
                        if (o1 == null) {
                            return "";
                        }
                        return o1;
                    }
                });
    }


    private void logError(HttpServletRequest request, Exception ex) {
        JSONObject requestInfo = new JSONObject();
        requestInfo.put("url", request.getRequestURL().toString());
        requestInfo.put("remote", request.getRemoteAddr());
        requestInfo.put("method", request.getMethod());
        requestInfo.put("errorMsg", ex.getMessage());
        requestInfo.put("errorStackTrace", Lists.transform(Arrays.asList(ex.getStackTrace()), new Function<StackTraceElement, String>() {
            int line = 0;
            @Override
            public String apply(StackTraceElement input) {
                return line++ + ".   " + input.toString();
            }
        }));
        log.error("exceptionDetails : {}", JSON.toJSONString(requestInfo, true));
    }

    @ExceptionHandler(value = NebulaeExistsException.class)
    public String nebulaeExistsExceptionHandler(NebulaeExistsException ex, HttpServletRequest request) {
        logError(request, ex);
        return returnExists();
    }

    @ExceptionHandler(value = NebulaeNotExistsException.class)
    public String nebulaeNotExistsExceptionHandler(NebulaeNotExistsException ex, HttpServletRequest request) {
        logError(request, ex);
        return returnNone();
    }


    @ExceptionHandler(value = EmptyResultDataAccessException.class)
    public String emptyResultDataAccessExceptionHandler(EmptyResultDataAccessException ex) {
        return returnSystemError(ex);
    }

    @ExceptionHandler(value = NullPointerException.class)
    public String nullPointerExceptionHandler(Exception ex, HttpServletRequest request) {
        logError(request, ex);
        return returnSystemError(ex);
    }

    @ExceptionHandler(value = Exception.class)
    public String exceptionHandler(Exception ex, HttpServletRequest request) {
        logError(request, ex);
        return returnSystemError(ex);
    }

    public String returnArgsNullOrEmpty() {
        Map<String, Object> result = new HashMap<>();
        this.addError(result, MsgEnum.C1001);
        return toJson(result);
    }

    public String returnSuccess(Object data) {
        Map<String, Object> result = new HashMap<>();
        this.addSuccess(result, data);
        return toJson(result);
    }

    public String returnSuccessWithoutNull(Object data) {
        JSONObject resultInfo = new JSONObject();
        resultInfo.put("success", 1);
        resultInfo.put("result", data);
        resultInfo.put("code", MsgEnum.C1000.getCode());
        resultInfo.put("msg", MsgEnum.C1000.getMsg());
        return resultInfo.toJSONString();
    }

    public String returnSuccess(String data) {
        Map<String, Object> result = new HashMap<>();
        this.addSuccess(result, data);
        return JSONObject.toJSONString(result);
    }

    public String returnNone() {
        Map<String, Object> result = new HashMap<>();
        result.put("success", 1);
        result.put("result", null);
        result.put("code", MsgEnum.C1002.getCode());
        result.put("msg", MsgEnum.C1002.getMsg());
        return toJson(result);
    }

    public String returnNone(Throwable t) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", 1);
        result.put("result", null);
        result.put("error", Throwables.getStackTraceAsString(t));
        result.put("code", MsgEnum.C1002.getCode());
        result.put("msg", MsgEnum.C1002.getMsg());
        return toJson(result);
    }

    public String returnSystemError(Throwable t) {
        Map<String, Object> resultInfo = new HashMap<>();
        resultInfo.put("success", 0);
        resultInfo.put("error", Throwables.getStackTraceAsString(t));
        resultInfo.put("code", MsgEnum.C1003.getCode());
        resultInfo.put("msg", MsgEnum.C1003.getMsg());
        return toJson(resultInfo);
    }

    public String returnExists() {
        Map<String, Object> result = new HashMap<>();
        result.put("success", 1);
        result.put("result", null);
        result.put("code", "1004");
        result.put("msg", "已存在");
        return toJson(result);
    }

    public String returnExists(Throwable t) {
        Map<String, Object> result = new HashMap<>();
        result.put("success", 1);
        result.put("result", null);
        result.put("error", Throwables.getStackTraceAsString(t));
        result.put("code", "1004");
        result.put("msg", "已存在");
        return toJson(result);
    }

    public String returnAccessDenied() {
        Map<String, Object> result = new HashMap<>();
        result.put("success", 1);
        result.put("result", "accessDenied");
        result.put("code", "1005");
        result.put("msg", "没有权限");
        return toJson(result);
    }

    public String returnNotExists() {
        Map<String, Object> result = new HashMap<>();
        result.put("success", 0);
        result.put("result", null);
        result.put("code", "1005");
        result.put("msg", "源数据不存在");
        return toJson(result);
    }

    protected void addSuccess(Map<String, Object> resultInfo, Object data) {
        resultInfo.put("success", 1);
        resultInfo.put("result", data);
        resultInfo.put("code", MsgEnum.C1000.getCode());
        resultInfo.put("msg", MsgEnum.C1000.getMsg());
    }

    protected void addError(Map<String, Object> resultInfo, MsgEnum msgEnum) {
        resultInfo.put("success", 0);
        resultInfo.put("code", msgEnum.getCode());
        resultInfo.put("msg", msgEnum.getMsg());
    }

    public enum MsgEnum {
        C0000("0000", "未登录"),
        C1000("1000", "成功"),
        C1001("1001", "参数不能为空"),
        C1002("1002", "查无数据"),
        C1003("1003", "系统异常l");

        private String code;
        private String msg;

        private MsgEnum(String code, String msg) {
            this.code = code;
            this.msg = msg;
        }

        public String getCode() {
            return this.code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMsg() {
            return this.msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }

}