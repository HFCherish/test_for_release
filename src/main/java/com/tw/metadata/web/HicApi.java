package com.tw.metadata.web;

import com.tw.metadata.domain.Api;
import com.tw.metadata.domain.ApiSpec;
import com.tw.metadata.domain.Service;
import com.tw.metadata.domain.factory.ApiFactory;
import com.tw.metadata.domain.factory.ApiSpecFactory;
import com.tw.metadata.domain.factory.ServiceServiceFactory;
import com.tw.metadata.web.payload.SinglePayload;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

/**
 * Created by pzzheng on 10/30/17.
 */
@RestController
@RequestMapping("/api-specs")
public class HicApi {
    @PostMapping
    public ResponseEntity<Object> resolveApiSpec(@RequestBody SinglePayload request) {
        ApiSpec apiSpecModel = ApiSpecFactory.from(request);
        Service service = ServiceServiceFactory.from(request);
        List<Api> apis = ApiFactory.from(request);


        ApiSpec savedApiSpec = service.addApiSpec(apiSpecModel);
        savedApiSpec.addApis(apis);

        return ResponseEntity.created(URI.create("")).body(savedApiSpec);
    }
}
