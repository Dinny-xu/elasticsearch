package com.study.elasticsearch.config;

import cn.hutool.core.util.StrUtil;
import com.study.elasticsearch.entity.ElasticsearchProperties;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Objects;

@Configuration
public class ElasticsearchConfig {

    @Bean
    public RestClientBuilder restClientBuilder(ElasticsearchProperties properties) {
        HttpHost[] hosts = Arrays.stream(properties.getHost())
                .map(this::makeHttpHost)
                .filter(Objects::nonNull)
                .toArray(HttpHost[]::new);
        return RestClient.builder(hosts);
    }


    @Bean
    public RestHighLevelClient highLevelClient(@Autowired RestClientBuilder restClientBuilder) {
        return new RestHighLevelClient(restClientBuilder);
    }


    private HttpHost makeHttpHost(String s) {
        assert StrUtil.isNotEmpty(s);
        String[] address = s.split(":");
        if (address.length == 2) {
            String ip = address[0];
            int port = Integer.parseInt(address[1]);
            return new HttpHost(ip, port, "http");
        } else {
            return null;
        }
    }
}
