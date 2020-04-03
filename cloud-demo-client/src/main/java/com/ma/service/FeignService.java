package com.ma.service;



import com.jd.datadevs.request.process.DataDevProcessCloneBean;
import com.jd.datadevs.request.process.DataDevProcessCreateBean;
import com.jd.datadevs.request.process.DataDevProcessTreeRequest;
import com.jd.datadevs.vo.TreeProgramBean;
import com.jd.framework.validate.bean.CommonResponse;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

@FeignClient(value = "datadevs-workspace")
public interface FeignService {

    @RequestMapping(value = "/helloTest",method = RequestMethod.GET)
    public String test();
    @RequestMapping(value = "/workspace/createProcess",method = RequestMethod.GET)
    public CommonResponse createProcess(DataDevProcessCreateBean dataDevProcessReqBean);

    @RequestMapping(value = "/processQuery/processTreeQuery",method = RequestMethod.GET)
    public CommonResponse<List<TreeProgramBean>> queryTree(DataDevProcessTreeRequest dataDevProcessTreeRequest);

    @RequestMapping(value = "/process/cloneProcess",method = RequestMethod.GET)
    public CommonResponse cloneProcess(DataDevProcessCloneBean dataDevProcessCloneBean);

}
