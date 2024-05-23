# Authorization using `@policy` and a Coprocessor

This repository demonstrates how to setup a coprocessor with the Router to evaluate policy-based authorization with the `@policy` directive. Note that this repo currently does not enforce authentication via a JWT or other token for the sake of simplicity. In a real code base, you would likely have a consumer provided token in a header which would be passed down to the "auth service", not the hard coding that this example does.

## Disclaimer
**The code in this repository is experimental and has been provided for reference purposes only. Community feedback is welcome but this project may not be supported in the same way that repositories in the official [Apollo GraphQL GitHub organization](https://github.com/apollographql) are. If you need help you can file an issue on this repository, [contact Apollo](https://www.apollographql.com/contact-sales) to talk to an expert, or create a ticket directly in Apollo Studio.**

## Running the Example

> Note: To run this example, you will need a GraphOS Enterprise plan and must create `/router/.env` based on `/router/.env.example` which exports `APOLLO_KEY` and `APOLLO_GRAPH_REF`.

1. Run the subgraph from the `/subgraph` directory with `npm run dev`
1. Run the auth-service from the `/auth-service` directory with `npm run dev`
1. Run the coprocessor from the `/coprocessor` directory with `npm run dev`
1. In the `/router` directory, download the router by running `./download_router.sh`
1. In the `/router` directory, compose the schema by running `./create_local_schema.sh`
1. In the `/router` directory, run the router by running `./start_router.sh`

Now if you run this code in the browser (http://127.0.0.1:4000/), you will be able to query the router.

## Code Highlights

### Router Configuration

In `router/router-config.yaml`, the coprocessor is configured with the Router to be called on the `supergraph` `request` stage.

Additionally, `authorization` directives are enabled.

### Coprocessor

In `coprocessor/src/index.js`, the coprocessor is setup with `express` to listen to the `/` POST endpoint and respond to the `SupergraphRequest` stage.

In the `processSupergraphRequestStage` function, the unevaluated policies are pulled from the context, sent to the auth service to be evaluated, and the resulting evaluated policies are mapped back into the payload for the Router.
