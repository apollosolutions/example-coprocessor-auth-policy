import express from "express";
import fetch from "node-fetch";

const app = express();

/**
 * This currently does not enforce authentication via a JWT or other token for the sake of simplicity.
 * In a real code base, you would likely have a consumer provided token in a header which would be passed down to the "auth service", not the hard coding that this example does
 */
const processSupergraphRequestStage = async (payload) => {
  // Get the unevaluated policies
  if (payload.context) {
    const authScopes = payload.context.entries["apollo_authorization::policies::required"];
    if (authScopes) {
      const policies = Object.keys(authScopes);

      // Map the evaluated policies back into the payload for the Router
      policies.forEach((policy) => {
        payload.context.entries["apollo_authorization::policies::required"][policy] = true;
      });
    }
  }

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
