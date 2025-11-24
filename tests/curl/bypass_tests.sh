#!/bin/bash

BASE_URL="http://localhost:8080/api"

echo "🔴 Test 1: Bypass - Past Time Booking"
curl -X POST "$BASE_URL/bookings/create" \
  -H "Content-Type: application/json" \
  -d '{"userId": 1, "pickupLocation": "A", "dropLocation": "B", "pickupTime": "2020-01-01 10:00:00", "passengers": 2, "vehicleType": "sedan", "distance": 10, "fare": 500, "paymentMode": "card"}'

echo -e "\n🔴 Test 2: Bypass - Excessive Passengers"
curl -X POST "$BASE_URL/bookings/create" \
  -H "Content-Type: application/json" \
  -d '{"userId": 1, "pickupLocation": "A", "dropLocation": "B", "pickupTime": "2025-12-25 10:00:00", "passengers": 99, "vehicleType": "sedan", "distance": 10, "fare": 500, "paymentMode": "card"}'

echo -e "\n🔴 Test 3: Bypass - Negative Fare"
curl -X POST "$BASE_URL/bookings/create" \
  -H "Content-Type: application/json" \
  -d '{"userId": 1, "pickupLocation": "A", "dropLocation": "B", "pickupTime": "2025-12-25 10:00:00", "passengers": 2, "vehicleType": "sedan", "distance": 10, "fare": -1, "paymentMode": "card"}'

echo -e "\n🔴 Test 4: Bypass - Invalid Vehicle Type"
curl -X POST "$BASE_URL/bookings/create" \
  -H "Content-Type: application/json" \
  -d '{"userId": 1, "pickupLocation": "A", "dropLocation": "B", "pickupTime": "2025-12-25 10:00:00", "passengers": 2, "vehicleType": "helicopter", "distance": 10, "fare": 500, "paymentMode": "card"}'

echo -e "\n🔴 Test 5: Bypass - Missing Fields"
curl -X POST "$BASE_URL/bookings/create" \
  -H "Content-Type: application/json" \
  -d '{"userId": 1, "pickupLocation": null, "dropLocation": "B", "pickupTime": "2025-12-25 10:00:00", "passengers": 2, "vehicleType": "sedan", "distance": 10, "fare": 500, "paymentMode": "card"}'

echo -e "\n🔴 Test 6: Bypass - Invalid Distance"
curl -X POST "$BASE_URL/bookings/create" \
  -H "Content-Type: application/json" \
  -d '{"userId": 1, "pickupLocation": "A", "dropLocation": "B", "pickupTime": "2025-12-25 10:00:00", "passengers": 2, "vehicleType": "sedan", "distance": -10, "fare": 500, "paymentMode": "card"}'

echo -e "\n🔴 Test 7: Bypass - Invalid Payment Mode"
curl -X POST "$BASE_URL/bookings/create" \
  -H "Content-Type: application/json" \
  -d '{"userId": 1, "pickupLocation": "A", "dropLocation": "B", "pickupTime": "2025-12-25 10:00:00", "passengers": 2, "vehicleType": "sedan", "distance": 10, "fare": 500, "paymentMode": "invalid_mode"}'

echo -e "\n🔴 Test 8: Bypass - Direct Book Endpoint"
curl -X POST http://localhost:8080/api/book -H "Content-Type: application/json" -d '{"passengers": 20}'

echo -e "\n✅ Bypass tests complete!"
