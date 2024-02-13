package sample.cafekiosk.spring.domain.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.groups.Tuple.tuple;
import static sample.cafekiosk.spring.domain.product.ProductSellingStatus.*;
import static sample.cafekiosk.spring.domain.product.ProductType.HANDMADE;

@ActiveProfiles("test") // 테스트를 위한 프로파일 지정
//@SpringBootTest
@DataJpaTest // 스프링 부트 테스트 보다 가볍다 = JPA 관련 빈만 주입해서 빠르게 스프링을 띄울 수 있다.
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("원하는 판매상태를 가진 상품들을 조회한다.")
    @Test
    void findAllBySellingStatusIn() {
        //given
        final Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);

        final Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);

        final Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 7000);
        productRepository.saveAll(List.of(product1, product2, product3));

        //when
        final List<Product> products = productRepository.findAllBySellingStatusIn(List.of(SELLING, HOLD));

        //then
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingStatus") // 검증하고자 하는 필드만 추출
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", SELLING),
                        tuple("002", "카페라떼", HOLD)
                );
    }

    @DisplayName("상품번호 리스트로 상품들을 조회한다.")
    @Test
    void findAllByProductNumberIn() {
        //given
        final Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        final Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
        final Product product3 = createProduct("003", HANDMADE, STOP_SELLING, "팥빙수", 7000);
        productRepository.saveAll(List.of(product1, product2, product3));

        //when
        final List<Product> products = productRepository.findAllByProductNumberIn(List.of("001", "002"));

        //then
        assertThat(products).hasSize(2)
                .extracting("productNumber", "name", "sellingStatus") // 검증하고자 하는 필드만 추출
                .containsExactlyInAnyOrder(
                        tuple("001", "아메리카노", SELLING),
                        tuple("002", "카페라떼", HOLD)
                );
    }

    @DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어온다.")
    @Test
    void findLatestProduct() {
        //given
        String targetProductNumber = "003";
        final Product product1 = createProduct("001", HANDMADE, SELLING, "아메리카노", 4000);
        final Product product2 = createProduct("002", HANDMADE, HOLD, "카페라떼", 4500);
        final Product product3 = createProduct(targetProductNumber, HANDMADE, STOP_SELLING, "팥빙수", 7000);
        productRepository.saveAll(List.of(product1, product2, product3));

        //when
        final String latestProductNumber = productRepository.findLatestProduct();

        //then
        assertThat(latestProductNumber).isEqualTo(targetProductNumber);
    }

    @DisplayName("가장 마지막으로 저장한 상품의 상품번호를 읽어올 때, 상품이 하나도 없는 경우에는 null을 반환한다.")
    @Test
    void findLatestProductNumberWhenProductIsEmpty() {
        //given
        //when
        final String latestProductNumber = productRepository.findLatestProduct();

        //then
        assertThat(latestProductNumber).isNull();
    }

    private Product createProduct(String productNumber, ProductType type, ProductSellingStatus status, String name, int price) {
        return Product.builder()
                .productNumber(productNumber)
                .type(type)
                .sellingStatus(status)
                .name(name)
                .price(price)
                .build();
    }
}