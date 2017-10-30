package com.tw.metadata.web;

import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

/**
 * Created by pzzheng on 10/30/17.
 */
@RestController
@RequestMapping("/api-specs")
public class HicApi {
    @PostMapping
    public ResponseEntity<Object> resolveApiSpec(@RequestBody JsonNode apiSpec) {
        return ResponseEntity.created(URI.create("")).body("");
    }
}
