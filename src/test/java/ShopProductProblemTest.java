package test.java;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.testing.junit.testparameterinjector.TestParameter;
import com.google.testing.junit.testparameterinjector.TestParameterInjector;
import main.java.ShopProductProblem;
import main.java.ShopProductProblem.Product;
import main.java.ShopProductProblem.Shop;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Tests for {@link ShopProductProblem}.
 */
@RunWith(TestParameterInjector.class)
public class ShopProductProblemTest {

  private static final Shop SHOP_1 =
      new Shop.ShopBuilder()
          .newBuilder()
          .setShopName("Shop-1")
          .setDistanceFromSource(10)
          .setAvailableProducts(
              ImmutableList.of(
                  new Product.ProductBuilder()
                      .newBuilder()
                      .setProductName("P1")
                      .setProductPrice(25)
                      .setQuantity(2)
                      .build(),
                  new Product.ProductBuilder()
                      .newBuilder()
                      .setProductName("P2")
                      .setProductPrice(30)
                      .setQuantity(3)
                      .build()))
          .build();
  private static final Shop SHOP_2 =
      new Shop.ShopBuilder()
          .newBuilder()
          .setShopName("Shop-2")
          .setDistanceFromSource(12)
          .setAvailableProducts(
              ImmutableList.of(
                  new Product.ProductBuilder()
                      .newBuilder()
                      .setProductName("P1")
                      .setProductPrice(30)
                      .setQuantity(2)
                      .build(),
                  new Product.ProductBuilder()
                      .newBuilder()
                      .setProductName("P2")
                      .setProductPrice(25)
                      .setQuantity(3)
                      .build(),
                  new Product.ProductBuilder()
                      .newBuilder()
                      .setProductName("P3")
                      .setProductPrice(20)
                      .setQuantity(2)
                      .build(),
                  new Product.ProductBuilder()
                      .newBuilder()
                      .setProductName("P4")
                      .setProductPrice(30)
                      .setQuantity(3)
                      .build()))
          .build();
  private static final Shop SHOP_3 =
      new Shop.ShopBuilder()
          .newBuilder()
          .setShopName("Shop-3")
          .setDistanceFromSource(20)
          .setAvailableProducts(
              ImmutableList.of(
                  new Product.ProductBuilder()
                      .newBuilder()
                      .setProductName("P1")
                      .setProductPrice(20)
                      .setQuantity(3)
                      .build(),
                  new Product.ProductBuilder()
                      .newBuilder()
                      .setProductName("P2")
                      .setProductPrice(20)
                      .setQuantity(2)
                      .build(),
                  new Product.ProductBuilder()
                      .newBuilder()
                      .setProductName("P3")
                      .setProductPrice(15)
                      .setQuantity(4)
                      .build()))
          .build();
  private static final Shop SHOP_4 =
      new Shop.ShopBuilder()
          .newBuilder()
          .setShopName("Shop-4")
          .setDistanceFromSource(10)
          .setAvailableProducts(
              ImmutableList.of(
                  new Product.ProductBuilder()
                      .newBuilder()
                      .setProductName("P1")
                      .setProductPrice(20)
                      .setQuantity(2)
                      .build(),
                  new Product.ProductBuilder()
                      .newBuilder()
                      .setProductName("P2")
                      .setProductPrice(30)
                      .setQuantity(1)
                      .build()))
          .build();

  enum ShopsWithProductsValidCase {
    WITH_PRODUCTS_NOT_AVAILABLE_IN_ANY_SHOP(
        ImmutableList.of(SHOP_1, SHOP_2), ImmutableMap.of("P1", 2, "P5", 2), ""),
    WITH_PRODUCT_AVAILABLE_IN_ONE_SHOP_IN_REQUIRED_QUANTITY(
        ImmutableList.of(SHOP_1, SHOP_2), ImmutableMap.of("P1", 2, "P3", 2), "Shop-2"),
    WITH_PRODUCT_AVAILABLE_IN_MORE_THAN_ONE_SHOP_IN_REQUIRED_QUANTITY_RECOMMENDS_ACC_TO_TOTAL_COST(
        ImmutableList.of(SHOP_1, SHOP_2, SHOP_3),
        ImmutableMap.of("P1", 2, "P2", 2, "P3", 2),
        "Shop-3");
    final ImmutableList<Shop> shops;
    final ImmutableMap<String, Integer> requiredProducts;
    final String recommendedShop;

    ShopsWithProductsValidCase(
        ImmutableList<Shop> shops,
        ImmutableMap<String, Integer> requiredProducts,
        String recommendedShop) {
      this.shops = shops;
      this.requiredProducts = requiredProducts;
      this.recommendedShop = recommendedShop;
    }
  }

  @Test
  public void getRecommendedShop_recommendShop(@TestParameter ShopsWithProductsValidCase testCase) {
    // Act.
    String recommendedShop =
        ShopProductProblem.getRecommendedShop(
            testCase.shops, testCase.requiredProducts, /* pricePerUnitDistance= */ 2);

    // Assert.
    assertEquals(testCase.recommendedShop, recommendedShop);
  }

  @Test
  public void getRecommendedShop_withShopsAtSameDistance_throwsException() {
    // Act & Assert.
    assertThrows(
        IllegalArgumentException.class,
        () ->
            ShopProductProblem.getRecommendedShop(
                ImmutableList.of(SHOP_1, SHOP_4),
                ImmutableMap.of("P1", 2, "P2", 2),
                /* pricePerUnitDistance= */ 2));
  }
}
