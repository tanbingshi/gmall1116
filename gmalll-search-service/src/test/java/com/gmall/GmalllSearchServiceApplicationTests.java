package com.gmall;

import com.alibaba.dubbo.config.annotation.Reference;
import com.gmall.api.bean.PmsSearchSkuInfo;
import com.gmall.api.bean.PmsSkuInfo;
import com.gmall.api.service.SkuInfoService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Index;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GmalllSearchServiceApplicationTests {

    @Reference
    SkuInfoService skuInfoService;

    @Autowired
    JestClient jestClient;

    @Test
    public void contextLoads() throws IOException {

        //查询所有PmsSKuInfo
        List<PmsSkuInfo> pmsSkuInfoList = skuInfoService.getAll();

        List<PmsSearchSkuInfo> pmsSearchSkuInfoList = new ArrayList<>();
        for (PmsSkuInfo pmsSkuInfo : pmsSkuInfoList) {
            PmsSearchSkuInfo pmsSearchSkuInfo = new PmsSearchSkuInfo();
            BeanUtils.copyProperties(pmsSkuInfo,pmsSearchSkuInfo);
            Index index = new Index.Builder(pmsSearchSkuInfo).index("gmall1116").type("PmsSearchSkuInfo").id(pmsSearchSkuInfo.getId()).build();
            pmsSearchSkuInfoList.add(pmsSearchSkuInfo);
            jestClient.execute(index);
        }
        System.out.println(pmsSearchSkuInfoList);
    }

}
