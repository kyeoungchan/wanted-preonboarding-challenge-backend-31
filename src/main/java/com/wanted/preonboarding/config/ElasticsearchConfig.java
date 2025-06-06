package com.wanted.preonboarding.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;
import org.springframework.data.elasticsearch.repository.config.EnableReactiveElasticsearchRepositories;

@Configuration
@EnableReactiveElasticsearchRepositories(basePackages = "com.wanted.preonboarding.service.query.repository")
public class ElasticsearchConfig extends ElasticsearchConfiguration {
    @Value("${spring.elasticsearch.host:elasticsearch}")
    private String host;

    @Value("${spring.elasticsearch.port:9200}")
    private int port;


    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(host + ":" + port)
                .build();
    }

    /*Spring Data Elasticsearch에서 도큐먼트를 인덱싱할 때 _class 필드를 생성할지 여부를 결정하는 설정입니다.
    기본적으로 Spring Data Elasticsearch는 _class 필드를 추가하여 엔터티의 클래스 정보를 포함하지만,
    writeTypeHint를 false로 설정하면 이 필드의 생성을 비활성화할 수 있습니다. */
    @Override
    protected boolean writeTypeHints() {
        return false;
    }
}
