package main.java;

import static com.google.common.collect.ImmutableList.toImmutableList;
import static com.google.common.collect.ImmutableSet.toImmutableSet;
import static java.util.Collections.min;

import com.google.common.base.Ascii;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class ShopProductProblem {

  /**
   * Represents a Product.
   */
  public static class Product {

    private String productName;
    private int productPrice;
    private int quantity;

    public Product(ProductBuilder productBuilder) {
      this.productName = productBuilder.productName;
      this.productPrice = productBuilder.productPrice;
      this.quantity = productBuilder.quantity;
    }

    String getProductName() {
      return productName;
    }

    int getProductPrice() {
      return productPrice;
    }

    int getQuantity() {
      return quantity;
    }

    /**
     * Product Builder class.
     */
    public static class ProductBuilder {

      private String productName;
      private int productPrice;
      private int quantity;

      public ProductBuilder newBuilder() {
        return new ProductBuilder();
      }

      public ProductBuilder setProductName(String productName) {
        this.productName = productName;
        return this;
      }

      public ProductBuilder setProductPrice(int productPrice) {
        this.productPrice = productPrice;
        return this;
      }

      public ProductBuilder setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
      }

      public Product build() {
        return new Product(this);
      }
    }

    @Override
    public String toString() {
      return "Product { productName = "
          + this.productName
          + ", productPrice = "
          + this.productPrice
          + ", quantity = "
          + this.quantity
          + " }";
    }
  }

  /**
   * Represents a Shop.
   */
  public static class Shop {

    private String shopName;
    private int distanceFromSource;
    private List<Product> availableProducts;

    public Shop(ShopBuilder shopBuilder) {
      this.shopName = shopBuilder.shopName;
      this.distanceFromSource = shopBuilder.distanceFromSource;
      this.availableProducts = shopBuilder.availableProducts;
    }

    String getShopName() {
      return shopName;
    }

    int getDistanceFromSource() {
      return distanceFromSource;
    }

    List<Product> getAvailableProducts() {
      return availableProducts;
    }

    /**
     * Shop Builder class.
     */
    public static class ShopBuilder {

      private String shopName;
      private int distanceFromSource;
      private List<Product> availableProducts;

      public ShopBuilder newBuilder() {
        return new ShopBuilder();
      }

      public ShopBuilder setShopName(String shopName) {
        this.shopName = shopName;
        return this;
      }

      public ShopBuilder setDistanceFromSource(int distanceFromSource) {
        this.distanceFromSource = distanceFromSource;
        return this;
      }

      public ShopBuilder setAvailableProducts(List<Product> availableProducts) {
        this.availableProducts = availableProducts;
        return this;
      }

      public Shop build() {
        return new Shop(this);
      }
    }

    @Override
    public String toString() {
      return "Shop { shopName = "
          + this.shopName
          + ", distanceFromSource = "
          + this.distanceFromSource
          + ", availableProducts = "
          + this.availableProducts
          + " }";
    }
  }

  /**
   * Problem Description: A customer wants to buy a few products at the minimum cost from the given
   * few shops where those products may or may not be available.
   *
   * <p>Write a function that accepts three parameters: 1. A list of shops with the following
   * information: shopName, distanceFromSource, availableProducts(productName, productPrice,
   * quantity). 2. A map of required products(product name as key and quantity as value). 3. The
   * price per unit distance.
   */
  /**
   * Returns a shop name from where the customer can buy all the products in their wishlist at
   * minimum cost after factoring in 2-way travel cost.
   *
   * @param shops List of shops.
   * @param wishList Map of required products with quantity.
   * @param pricePerUnitDistance Price per unit distance.
   * @return String recommended shop name which can provide required products in low price.
   * @throws IllegalArgumentException if any shop is at same distance from source.
   */
  public static String getRecommendedShop(
      ImmutableList<Shop> shops, ImmutableMap<String, Integer> wishList, int pricePerUnitDistance) {
    ImmutableList<Integer> distancesFromSource =
        shops.stream().map(Shop::getDistanceFromSource).collect(toImmutableList());
    if (distancesFromSource.stream()
        .anyMatch(distance -> Collections.frequency(distancesFromSource, distance) > 1)) {
      throw new IllegalArgumentException("Invalid distances from source.");
    }

    // Get all the shops from the input shop list which have all the required products in the
    // required quantity.
    ImmutableList<Shop> recommendedShops =
        shops.stream()
            .filter(
                shop ->
                    shop.getAvailableProducts().stream()
                        .map(Product::getProductName)
                        .collect(toImmutableSet())
                        .containsAll(wishList.keySet()))
            .filter(
                shop -> {
                  for (Product product : shop.getAvailableProducts()) {
                    if (wishList.containsKey(product.getProductName())
                        && product.getQuantity() < wishList.get(product.getProductName())) {
                      return false;
                    }
                  }
                  return true;
                })
            .collect(toImmutableList());

    Map<String, Integer> costPerShopMap = new HashMap<>();
    for (Shop shop : recommendedShops) {
      int totalPrice = shop.getDistanceFromSource() * pricePerUnitDistance;
      for (Map.Entry<String, Integer> product : wishList.entrySet()) {
        totalPrice +=
            shop.getAvailableProducts().stream()
                .filter(
                    availableProduct ->
                        Ascii.equalsIgnoreCase(product.getKey(), availableProduct.getProductName()))
                .map(Product::getProductPrice)
                .map(productPrice -> productPrice * product.getValue())
                .findFirst()
                .orElse(0);
      }
      costPerShopMap.put(shop.getShopName(), totalPrice);
    }

    return costPerShopMap.entrySet().stream()
        .filter(entry -> min(costPerShopMap.values()).equals(entry.getValue()))
        .map(Entry::getKey)
        .findFirst()
        .orElse("");
  }

  private ShopProductProblem() {
  }
}
