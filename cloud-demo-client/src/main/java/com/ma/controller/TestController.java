package com.ma.controller;




import com.jd.datadevs.request.process.DataDevProcessCloneBean;
import com.jd.datadevs.request.process.DataDevProcessCreateBean;
import com.jd.datadevs.request.process.DataDevProcessTreeRequest;
import com.jd.datadevs.vo.TreeProgramBean;
import com.jd.framework.validate.bean.CommonResponse;
import com.ma.service.FeignService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;
import java.util.UUID;


@RestController
public class TestController {

    private static final Logger log = LoggerFactory.getLogger(TestController.class);

    private static final UUID INSTANCE_UUID = UUID.randomUUID();

    @Resource
    private FeignService feignService;

    @GetMapping("/test")
    public String test() {
        log.info("test :" + INSTANCE_UUID.toString());
        return feignService.test();
    }
    @GetMapping("/createProcess")
    public String createProcess() {
        log.info("test :" + INSTANCE_UUID.toString());
        DataDevProcessCreateBean dp = new DataDevProcessCreateBean();
        dp.setProcessName("初始化测试流程");
        dp.setProcessCode("init---test");
        dp.setCreatedDate(new Date());
        CommonResponse res = feignService.createProcess(dp);
       log.info(res.success()+" "+res.getRspCode());
        return "success";
    }
    @GetMapping("/queryTree")
    public CommonResponse queryTree() {
        log.info("test :" + INSTANCE_UUID.toString());
        DataDevProcessTreeRequest dataDevProcessTreeRequest = new DataDevProcessTreeRequest();
        dataDevProcessTreeRequest.setTenantId("");
        CommonResponse<List<TreeProgramBean>> response =feignService.queryTree(dataDevProcessTreeRequest);
        return response;
    }
    @GetMapping("/cloneProcess")
    public CommonResponse cloneProcess() {
        DataDevProcessCloneBean dataDevProcessCloneBean = new DataDevProcessCloneBean();
        dataDevProcessCloneBean.setProcessId(1);
        dataDevProcessCloneBean.setSourceProgramId("2");
        dataDevProcessCloneBean.setTargetProgramId("9");
//        dataDevProcessCloneBean.setProcessName("初始化测试流程");
        return feignService.cloneProcess(dataDevProcessCloneBean);
    }
}
