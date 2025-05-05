package auth_demo
import rego.v1

# This rule extracts the claims from the JWT token in the Authorization header, if available.
claims := claims if {
	"authorization" in object.keys(input.headers)
	header := input.headers.authorization
	startswith(lower(header), lower("bearer"))
	bearer_token := substring(header, count("bearer "), -1)
	[_, claims, _] := io.jwt.decode(bearer_token)
}

policies[policy] := result if {
	policy := "roles:user"

	# check that the policy exists in our list of policies before evaluating
	policy in input.policies

	# check if the claims have the role of "user" in the claims for the `roles:user` policy defined
	result := claims.role == "user"
}

# This rule checks if the token has claims, which is used to determine if the token is valid. 
# Some tokens may not have claims for one reason for another, but it is exceptionally rare, 
# thus not covered by this example.
policies[policy] := result if {
	policy := "token:valid"

    # check that the policy exists in our list of policies before evaluating
	policy in input.policies
	result := claims != {}
}

# This rule checks if the user has the role of "user" in the claims for the `role:user` policy defined
# in the schema.
policies[policy] := result if {
	policy := "role:user"

    # check that the policy exists in our list of policies before evaluating
	policy in input.policies
	result := claims.role == "user"
}

# This rule is dynamic, and will check a given key to a given value in the claims.
# As Rego does not support switch/case or if statements in a rule definition, there's no way to allow for behavior 
# based on the types of policies easily. 
# This rule further requires the format of `claim:key:value` to be used in the policy definition. The value must match
# the value in the claims. 
# This behavior could be customized for a different type of prefix (e.g. `scope:key:value`) 
# or a different separator (e.g. `claim.key.value`).
policies[policy] := result if {
	# iterate over all policies and finding those that start with claims: to then use as a claims lookup
	some policy in input.policies
	startswith(policy, "claim:")
	
    # split the policy into its components, and check if it has 3 components (which is what is needed)
	claim_policies := split(policy, ":")
	count(claim_policies) == 3
	
    # lastly, check if the claim exists in the claims and if it matches the value in the policy
    # we also convert the claim value into a string via sprintf to ensure we can compare values 
    # in case it is a float/number/boolean
	result = sprintf("%v", [claims[claim_policies[1]]]) == claim_policies[2]
}
