# shop-product-problem

**Problem Description:** \
A customer wants to buy a few products at the minimum cost from the given few shops where those products may or may not be available.

Write a function that accepts three parameters:
1. A list of shops with the following information: shopName, distanceFromSource, availableProducts(productName, productPrice, quantity).
2. A map of required products(product name as key and quantity as value).
3. The price per unit distance.

**Returns**
- Shop name from where the customer can buy all the products in their wishlist at minimum cost after factoring in 2-way travel cost.

**Function Signature**
String getRecommendedShop(ImmutableList<Shop> shops, ImmutableMap<String, Integer> wishList, int pricePerUnitDistance);

**Sample Input**
```
shopsWithProducts =
[
    {
        shopName: "Shop-1"
        distanceFromSource: 10
        products: [
                        {
                            productName: P1
                            productPrice: 25
                            quantity: 2
                        },
                        {
                            productName: P2
                            productPrice: 30
                            quantity: 3
                        }
                    ]
    },
    {
        shopName: "Shop-2"
        distanceFromSource: 12
        products: [
                        {
                            productName: P1
                            productPrice: 30
                            quantity: 2
                        },
                        {
                            productName: P2
                            productPrice: 25
                            quantity: 3
                        },
                        {
                            productName: P3
                            productPrice: 20
                            quantity: 2
                        },
                        {
                            productName: P4
                            productPrice: 30
                            quantity: 3
                        }
                    ]
    },
    {
        shopName: "Shop-3"
        distanceFromSource: 20
        products: [
                        {
                            productName: P1
                            productPrice: 20
                            quantity: 3
                        },
                        {
                            productName: P2
                            productPrice: 20
                            quantity: 2
                        },
                        {
                            productName: P3
                            productPrice: 15
                            quantity: 4
                        }
                    ]
    }
]
requiredProducts = [{"P1": 2}, {"P2", 2}, {"P3": 2}]
pricePerUnitDistance = 2
```

**Sample Output** \
"Shop-3"

**Constraints**
- All the given shops are at different distances from the source.
