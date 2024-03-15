import express from "express";

const app = express();

app.post("/policy/evaluate", express.json(), (req, res) => {
  // This is just an example for the purposes of having something that the coprocessor can call.
  res.send({
    policies: req.body.policies.map((scope) => ({
      scope,
      result: true,
    })),
  });
});

app.listen(3005, () => {
  console.log("🚀 Server running at http://localhost:3005");
});
