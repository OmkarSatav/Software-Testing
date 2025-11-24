#!/bin/bash

BASE_URL="http://localhost:8080/api"

# Bypass: upload disguised executable (using raw curl form upload)
curl -X POST "$BASE_URL/uploads" \
  -H "Content-Type: multipart/form-data" \
  -F "file=@/path/to/malicious.exe;type=image/jpeg" || true

# Bypass: admin role spoofing in JSON body
curl -X POST "$BASE_URL/admin/block" \
  -H "Content-Type: application/json" \
  -d '{"userId":2, "role":"ADMIN"}' || true

# Bypass: force driver assignment with invalid driverId
curl -X POST "$BASE_URL/bookings/create" \
  -H "Content-Type: application/json" \
  -d '{"userId":1, "pickupLocation":"A", "dropLocation":"B", "pickupTime":"2025-12-25 10:00:00", "passengers":2, "vehicleType":"sedan", "driverId":9999, "distance":5, "fare":10, "paymentMode":"card"}' || true

# Bypass: remove authentication cookie and call protected endpoint
curl -X GET "$BASE_URL/bookings/user/1" -H "Accept: application/json" || true

# Bypass: try to cancel another user's booking by tampering userId
curl -X POST "$BASE_URL/bookings/1/cancel" \
  -H "Content-Type: application/json" \
  -d '{"userId":999, "reason":"test"}' || true

echo "Additional curl bypass tests created."

