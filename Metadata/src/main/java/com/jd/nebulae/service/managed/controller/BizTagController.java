package com.jd.nebulae.service.managed.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.google.common.base.Throwables;
import com.jd.nebulae.common.entity.BizLine;
import com.jd.nebulae.common.entity.Page;
import com.jd.nebulae.common.entity.TagType;
import com.jd.nebulae.service.common.controller.NebulaeController;
import com.jd.nebulae.service.managed.service.BizTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("nebulae/meta/ops")
public class BizTagController extends NebulaeController {

    @Autowired
    private BizTagService bizTagService;

    @RequestMapping(value = "addBizTag", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String addTag(@RequestBody BizLine bizLine) {
        boolean success = bizTagService.addTag(bizLine);
        return returnSuccess(success);
    }

    @RequestMapping(value = "deleteBizTag", method = RequestMethod.POST)
    public String deleteTag(@RequestParam(value = "id") String id) {
        boolean success = bizTagService.deleteTag(id);
        return returnSuccess(success);
    }

    @RequestMapping(value = "updateBizTag", method = RequestMethod.POST)
    public String updateBizTag(@RequestBody BizLine bizLine) {
        boolean success = bizTagService.updateBizTag(bizLine);
        return returnSuccess(success);
    }

    @RequestMapping(value = "getBizLineByCode", method = RequestMethod.GET)
    public String getBizLineByCode(@RequestParam(value = "code") String code) {
        JSONObject bizLine = bizTagService.getBizLineByCode(code);
        return returnSuccess(bizLine);
    }

    @RequestMapping(value = "getBizLineAll", method = RequestMethod.POST)
    public String getBizLineAll(@RequestParam(value = "pageNum", defaultValue = "1")Integer pageNum,
                                @RequestParam(value = "pageSize", defaultValue = "10")Integer pageSize,
                                @RequestBody(required = false) BizLine bizLine) {
        Page<JSONObject> bizLines = bizTagService.getBizLineAll(pageNum,pageSize,bizLine);
        return returnSuccess(bizLines);
    }

    @GetMapping(value = "getTagTypes")
    public String getTagTypes() {
        try {
            return returnSuccess(bizTagService.getTagTypesAll());
        } catch (RuntimeException ex) {
            return returnSystemError(ex);
        }
    }

    @PostMapping(value = "addTagType")
    public String addTagType(@RequestBody TagType tagType) {
        try {
            tagType.setCreateBy(tagType.getUsername());
            return returnSuccess(bizTagService.addTagType(tagType));
        } catch (RuntimeException ex) {
            return returnSystemErrorMess(ex);
        }
    }

    @PostMapping(value = "updateTagType")
    public String updateTagType(@RequestBody TagType tagType) {
        try {
            tagType.setUpdateBy(tagType.getUsername());
            return returnSuccess(bizTagService.updateTagType(tagType));
        } catch (RuntimeException ex) {
            return returnSystemError(ex);
        }
    }

    @PostMapping(value = "deleteTagType")
    public String deleteTagType(@RequestBody TagType tagType) {
        try {
            boolean result = bizTagService.deleteTagType(tagType.getId());
            if(result) {
                return returnSuccess(result);
            } else {
                return returnNotExists();
            }
        } catch (RuntimeException ex) {
            return returnSystemError(ex);
        }
    }


    @RequestMapping(value = "getBizLineByCodeV2", method = RequestMethod.GET)
    public String getBizLineByCodeV2(@RequestParam(value = "code") String code) {
        try {
            return returnSuccess(bizTagService.getBizLineByCodeV2(code));
        } catch (RuntimeException ex) {
            return returnSystemError(ex);
        }
    }

    @RequestMapping(value = "getBizLineByCondition", method = RequestMethod.POST)
    public String getBizLineByCondition(@RequestBody BizLine bizLine) {
        try {
            return returnSuccess(bizTagService.getBizLineLinkByCondition(bizLine));
        } catch (RuntimeException ex) {
            return returnSystemError(ex);
        }
    }

    @RequestMapping(value = "addBizTagV2", method = RequestMethod.POST,produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public String addTagV2(@Valid @RequestBody BizLine bizLine) {
        try {
            bizLine.setCreateBy(bizLine.getUsername());
            return returnSuccess(bizTagService.addTagV2(bizLine));
        } catch (RuntimeException ex) {
            return returnSystemError(ex);
        }
    }


    @RequestMapping(value = "updateBizTagV2", method = RequestMethod.POST)
    public String updateBizTagV2(@RequestBody BizLine bizLine) {
        try {
            bizLine.setUpdateBy(bizLine.getUsername());
            return returnSuccess(bizTagService.updateBizTagV2(bizLine));
        } catch (RuntimeException ex) {
            return returnSystemError(ex);
        }
    }

    @RequestMapping(value = "deleteBizTagV2", method = RequestMethod.POST)
    public String deleteBizTagV2(@RequestBody BizLine bizLine) {
        try {
            boolean result = bizTagService.deleteTagV2(bizLine.getId());
            if(result) {
                return returnSuccess(result);
            } else {
                return returnNotExists();
            }
        } catch (RuntimeException ex) {
            return returnSystemError(ex);
        }
    }


    public String hello() {
        return "hello world";
    }


    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public String exceptionHandler(MethodArgumentNotValidException ex, HttpServletRequest request) {

        Map<String, Object> resultInfo = new HashMap<>();
        resultInfo.put("success", 0);
        resultInfo.put("error", ex.getMessage());
        resultInfo.put("code", MsgEnum.C1003.getCode());
        resultInfo.put("msg", ex.getBindingResult().getFieldError().getDefaultMessage());
        return toJson(resultInfo);

    }
    /**
     * 用于处理返回值中携带异常信息
     * @param t
     * @return
     */
    private String returnSystemErrorMess(Throwable t) {
        Map<String, Object> resultInfo = new HashMap<>();
        resultInfo.put("success", 0);
        resultInfo.put("error", Throwables.getStackTraceAsString(t));
        resultInfo.put("code", MsgEnum.C1003.getCode());
        resultInfo.put("msg", t.getMessage());
        return toJson(resultInfo);
    }
    private  String toJson(final Object result) {
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

}
