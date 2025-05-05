import express from "express";
import fetch from "node-fetch";

const POLICY_KEY ="apollo::authorization::required_policies";

const app = express();

/**
 * This currently does not enforce authentication via a JWT or other token for the sake of simplicity.
 * In a real code base, you would likely have a consumer provided token in a header which would be passed down to the "auth service", not the hard coding that this example does
 */
const processSupergraphRequestStage = async (payload) => {
  if (!payload.context.entries[POLICY_KEY]) {
    console.log("🚀 No policies found");
    return payload
  }

  // Get the unevaluated policies
  const policies = Object.keys(payload.context.entries[POLICY_KEY]);

  // Send the list of policies downstream to the auth service
  const response = await fetch("http://localhost:8181/v1/data", {
    method: "POST",
    headers: {
      "content-type": "application/json",
    },
    body: JSON.stringify({
      input: {
        headers: {
          authorization: payload.headers.authorization ? payload.headers.authorization[0] : null
        },
        policies,
      },
    }),
  }).then((res) => res.json());

  if (!response.result || !response.result.auth_demo || !response.result.auth_demo.policies) {
    console.warn("⚠️ Missing expected policy data in OPA response:", response);
    return payload;
  }

  Object.entries(response.result.auth_demo.policies).forEach(([key, value]) => {
    console.log("🚀 Setting policy", key, value);
    payload.context.entries[POLICY_KEY][key] = value;
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
    default:
      console.warn("⚠️ Unknown stage", payload.stage);
      break;
  }

  res.send(response);
});

app.listen(3007, () => {
  console.log("🚀 Coprocessor running at http://localhost:3007");
});
