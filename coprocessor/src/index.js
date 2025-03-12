import express from "express";
import fetch from "node-fetch";

const app = express();

/**
 * This currently does not enforce authentication via a JWT or other token for the sake of simplicity.
 * In a real code base, you would likely have a consumer provided token in a header which would be passed down to the "auth service", not the hard coding that this example does
 */
const processSupergraphRequestStage = async (payload) => {
  // If there are no policies to evaluate, return the payload as is
  if (!payload.context.entries["apollo::authorization::required_policies"]) {
    return payload;
  }
  // Get the unevaluated policies
  const policies = Object.keys(payload.context.entries["apollo::authorization::required_policies"]);

  // Send the list of policies downstream to the auth service
  const response = await fetch("http://localhost:3005/policy/evaluate", {
    method: "POST",
    headers: {
      "content-type": "application/json",
    },
    body: JSON.stringify({
      policies,
    }),
  }).then((res) => res.json());

  // Map the evaluated policies back into the payload for the Router
  response.policies.forEach((policy) => {
    payload.context.entries["apollo::authorization::required_policies"][policy.scope] = policy.result;
  });

  return payload;
};

app.post("/", express.json(), async (req, res) => {
  const payload = req.body;

  let response = payload;
  switch (payload.stage) {
    case "SupergraphRequest":
      response = await processSupergraphRequestStage(payload);
      break;
  }

  res.send(response);
});

app.listen(3007, () => {
  console.log("🚀 Server running at http://localhost:3007");
});
