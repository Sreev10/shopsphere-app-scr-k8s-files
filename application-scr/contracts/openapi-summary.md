# ShopSphere API contracts

## Product
`GET /api/products` → product list
`GET /api/products/{id}` → product
`POST /api/products` → create product

## User
`POST /api/users/register`
```json
{"name":"Demo User","email":"demo@example.com","password":"password"}
```

## Order
`POST /api/orders`
```json
{"userId":1,"items":[{"productId":1,"quantity":2,"unitPrice":7999.00}]}
```

## Payment
`POST /api/payments`
```json
{"orderId":1,"amount":15998.00}
```

Payment is intentionally a mock service for DevOps practice. Real payment-provider integration is outside this training phase.
