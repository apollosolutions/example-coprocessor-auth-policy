package com.example.demo;

import com.example.demo.handlers.SupergraphRequestHandler;
import com.example.demo.models.CoprocessorStage;
import com.example.demo.models.RouterPayload;
import com.example.demo.handlers.RouterRequestHandler;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CoprocessorController {
    private final RouterRequestHandler routerRequestHandler;
    private final SupergraphRequestHandler supergraphRequestHandler;

    public CoprocessorController(
            RouterRequestHandler routerRequestHandler,
            SupergraphRequestHandler supergraphRequestHandler
    ) {
        this.routerRequestHandler = routerRequestHandler;
        this.supergraphRequestHandler = supergraphRequestHandler;
    }

    @PostMapping("/")
    public RouterPayload StageSelect(@RequestBody RouterPayload request) {
        CoprocessorStage stage = request.getStage();

        switch (stage) {
            case ROUTER_REQUEST -> request = routerRequestHandler.handle(request);
            case SUPERGRAPH_REQUEST -> request = supergraphRequestHandler.handle(request);
        }

        return request;
    }
}
