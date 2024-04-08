package com.example.demo.handlers;

import com.example.demo.models.CoprocessorContext;
import com.example.demo.models.RouterPayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import graphql.language.Document;
import graphql.language.OperationDefinition;
import graphql.parser.Parser;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class SupergraphRequestHandler {

    private static final String APOLLO_AUTHORIZATION_POLICIES_REQUIRED = "apollo_authorization::policies::required";
    private ObjectMapper objectMapper = new ObjectMapper();

    public RouterPayload handle(RouterPayload request) {
        try {
            System.out.println(objectMapper.writeValueAsString(request));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // Read values
        CoprocessorContext context = request.getContext();
        JsonNode entriesNode = context.getEntries();
        Map<String, Map<String, Boolean>> entriesMap = objectMapper.convertValue(entriesNode, Map.class);
        Map<String, Boolean> policyMap = entriesMap.get(APOLLO_AUTHORIZATION_POLICIES_REQUIRED);
        //System.out.println(String.format("This request requires: %s", policyMap));

        // Invoke auth service to check permissions and update relevant policy
        policyMap.put("roles:user", true);

        // Mutate context
        entriesMap.put(APOLLO_AUTHORIZATION_POLICIES_REQUIRED, policyMap);
        entriesNode = objectMapper.convertValue(entriesMap, JsonNode.class);
        context.setEntries(entriesNode);
        request.setContext(context);

        // Always playback the request with mutated state
        try {
            System.out.println(objectMapper.writeValueAsString(request));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return request;
    }
}
