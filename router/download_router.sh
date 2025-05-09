set -e

VERSION="latest"

echo "Downloading Router version $VERSION..."
curl -sSL https://router.apollo.dev/download/nix/"$VERSION" | sh

echo "Updating config schema file..."
./router config schema > configuration_schema.json

echo "Success!"
