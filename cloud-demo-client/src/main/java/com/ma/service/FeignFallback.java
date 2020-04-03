package com.ma.service;


import com.jd.datadevs.request.process.DataDevProcessCloneBean;
import com.jd.datadevs.request.process.DataDevProcessCreateBean;
import com.jd.datadevs.request.process.DataDevProcessTreeRequest;
import com.jd.framework.validate.bean.CommonResponse;
import org.springframework.stereotype.Component;

@Component
public class FeignFallback implements FeignService {

    @Override
    public String test() {
        return "servie error!";
    }

    @Override
    public CommonResponse createProcess(DataDevProcessCreateBean dataDevProcessCreateBean) {
        return null;
    }

    @Override
    public CommonResponse queryTree(DataDevProcessTreeRequest dataDevProcessTreeRequest) {
        return null;
    }

    @Override
    public CommonResponse cloneProcess(DataDevProcessCloneBean dataDevProcessCloneBean) {
        return null;
    }

}
