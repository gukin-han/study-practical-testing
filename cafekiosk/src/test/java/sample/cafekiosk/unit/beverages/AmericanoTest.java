package sample.cafekiosk.unit.beverages;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class AmericanoTest {

    @Test
    void getName() {
        final Americano americano = new Americano();

        assertEquals(americano.getName(), "아메리카노"); // JUnit API
        assertThat(americano.getName()).isEqualTo("아메리카노"); // AssertJ
    }

    @Test
    void getPrice() {
        final Americano americano = new Americano();

        assertThat(americano.getPrice()).isEqualTo(4000);
    }
}