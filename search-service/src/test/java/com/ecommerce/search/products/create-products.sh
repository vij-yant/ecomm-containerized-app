#!/bin/bash

# Set base URL for product-service
BASE_URL="http://localhost:8081/api/products"

# Array of products in JSON format
PRODUCTS=(
  '{"name": "Men'\''s T-Shirt", "brand": "Nike", "size": "M", "price": 999.99, "description": "Breathable sports t-shirt", "quantityInStock": 100}'
  '{"name": "Women'\''s Hoodie", "brand": "Adidas", "size": "L", "price": 1299.99, "description": "Warm and cozy hoodie", "quantityInStock": 50}'
  '{"name": "Running Shoes", "brand": "Puma", "size": "9", "price": 3499.99, "description": "Lightweight running shoes", "quantityInStock": 75}'
  '{"name": "Men'\''s Shorts", "brand": "Reebok", "size": "XL", "price": 599.99, "description": "Quick-dry training shorts", "quantityInStock": 60}'
  '{"name": "Sports Cap", "brand": "Under Armour", "size": "Free", "price": 399.99, "description": "Adjustable training cap", "quantityInStock": 120}'
)

# Loop through each product and send POST request
for PRODUCT in "${PRODUCTS[@]}"; do
  echo "Creating product: $PRODUCT"
  curl --silent --location "$BASE_URL" \
    --header 'Content-Type: application/json' \
    --data "$PRODUCT"
  echo -e "\n---"
done
