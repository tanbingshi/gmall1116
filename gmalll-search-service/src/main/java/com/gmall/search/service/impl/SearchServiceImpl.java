package com.gmall.search.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.gmall.api.bean.PmsSearchParam;
import com.gmall.api.bean.PmsSearchSkuInfo;
import com.gmall.api.bean.PmsSkuAttrValue;
import com.gmall.api.service.SearchService;
import io.searchbox.client.JestClient;
import io.searchbox.core.Search;
import io.searchbox.core.SearchResult;
import org.apache.commons.lang3.StringUtils;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.highlight.HighlightBuilder;
import org.elasticsearch.search.highlight.Highlighter;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    JestClient jestClient;

    @Override
    public List<PmsSearchSkuInfo> list(PmsSearchParam pmsSearchParam) {

        List<PmsSearchSkuInfo> pmsSearchSkuInfoList = new ArrayList<>();

        String catalog3Id = pmsSearchParam.getCatalog3Id();
        String keyword = pmsSearchParam.getKeyword();
        String[] valueId = pmsSearchParam.getValueId();

        //search
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        //bool
        BoolQueryBuilder boolQueryBuilder = new BoolQueryBuilder();

        //filter
        //catalog3Id
        if(catalog3Id != null){
            TermQueryBuilder catalog3Id1 = new TermQueryBuilder("catalog3Id", catalog3Id);
            boolQueryBuilder.filter(catalog3Id1);
        }

        //skuAttrValueList
        if(valueId != null){
            for (String s : valueId) {
                TermQueryBuilder termQueryBuilder = new TermQueryBuilder("skuAttrValueList.valueId", s);
                boolQueryBuilder.filter(termQueryBuilder);
            }
        }

        //query
        if(StringUtils.isNotBlank(keyword)){
            MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("skuName", keyword);
            boolQueryBuilder.must(matchQueryBuilder);
        }

        //highlight
        HighlightBuilder highlightBuilder = new HighlightBuilder();
        highlightBuilder.preTags("<span style='color:red';>");
        highlightBuilder.field("skuName");
        highlightBuilder.postTags("</span>");
        searchSourceBuilder.highlight(highlightBuilder);


        //size
        searchSourceBuilder.from(0);
        searchSourceBuilder.size(20);

        //sort

        searchSourceBuilder.query(boolQueryBuilder);
        String dsl = searchSourceBuilder.toString();

        System.out.println(dsl);

        Search search = new Search.Builder(dsl).addIndex("gmall1116").addType("PmsSearchSkuInfo").build();
        try {
            SearchResult execute = jestClient.execute(search);
            List<SearchResult.Hit<PmsSearchSkuInfo, Void>> hits = execute.getHits(PmsSearchSkuInfo.class);
            for (SearchResult.Hit<PmsSearchSkuInfo, Void> hit : hits) {
                PmsSearchSkuInfo pmsSearchSkuInfo = hit.source;
                Map<String, List<String>> highlight = hit.highlight;
                if(highlight != null){
                    String skuName = highlight.get("skuName").get(0);
                    pmsSearchSkuInfo.setSkuName(skuName);
                }
                pmsSearchSkuInfoList.add(pmsSearchSkuInfo);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return pmsSearchSkuInfoList;
    }
}
