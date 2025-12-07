#!/bin/bash
# verify_system.sh

# Base URLs
GATEWAY_URL="http://localhost:8080"
AUTH_URL="$GATEWAY_URL/api/v1/auth"
SALES_URL="$GATEWAY_URL/api/v1/customers"

echo "Step 1: Register Tenant A"
TOKEN_A=$(curl -s -X POST "$AUTH_URL/register" \
  -H "Content-Type: application/json" \
  -d '{"tenantName": "TenantA", "username": "adminA", "password": "password"}' | jq -r '.token')

echo "Tenant A Token: $TOKEN_A"

echo "Step 2: Register Tenant B"
TOKEN_B=$(curl -s -X POST "$AUTH_URL/register" \
  -H "Content-Type: application/json" \
  -d '{"tenantName": "TenantB", "username": "adminB", "password": "password"}' | jq -r '.token')

echo "Tenant B Token: $TOKEN_B"

echo "Step 3: Create Customer for Tenant A"
curl -s -X POST "$SALES_URL" \
  -H "Authorization: Bearer $TOKEN_A" \
  -H "Content-Type: application/json" \
  -d '{"name": "Customer A", "email": "a@tenant-a.com"}'

echo "Step 4: Create Customer for Tenant B"
curl -s -X POST "$SALES_URL" \
  -H "Authorization: Bearer $TOKEN_B" \
  -H "Content-Type: application/json" \
  -d '{"name": "Customer B", "email": "b@tenant-b.com"}'

echo "Step 5: Verify Tenant A Isolation"
RESPONSE_A=$(curl -s -X GET "$SALES_URL" -H "Authorization: Bearer $TOKEN_A")
echo "Tenant A Customers: $RESPONSE_A"

# Check if Tenant B customer is present in Tenant A response
if echo "$RESPONSE_A" | grep -q "Customer B"; then
  echo "FAILURE: Tenant A can see Tenant B's data!"
else
  echo "SUCCESS: Tenant A sees only their own data."
fi
