package truffle_scheme6.runtime;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

class SPairTest {
    @Test
    public void construction() {
        assertEquals(
                SPair.from(0, SNil.SINGLETON),
                new SPair(0, SNil.SINGLETON)
        );

        assertEquals(
                SPair.from(IntStream.of(1, 2, 3).boxed().toArray()),
                new SPair(1, new SPair(2, 3))
        );

        assertEquals(
                SPair.from(SSymbol.get("some-quoted-thing"), 1, 2, 3, SNil.SINGLETON),
                new SPair(SSymbol.get("some-quoted-thing"),
                        new SPair(1,
                                new SPair(2,
                                        new SPair(3,
                                                SNil.SINGLETON))))
        );
    }

    @Test
    public void illegalThings() {
        assertThrows(IllegalArgumentException.class, () -> SPair.from(1));
        assertThrows(IllegalArgumentException.class, () -> new SPair(1, null));
        assertThrows(IllegalArgumentException.class, () -> new SPair(null, 1));
        assertDoesNotThrow(() -> new SPair(SNil.SINGLETON, SNil.SINGLETON));
        assertThrows(IllegalArgumentException.class, () -> new SPair(SSymbol.get("some-quoted-thing"), SNil.SINGLETON).setCar(null));
        assertThrows(IllegalArgumentException.class, () -> new SPair(SSymbol.get("some-quoted-thing"), SNil.SINGLETON).setCdr(null));
    }

    @Test
    public void counting() {
        assertEquals(
                SPair.from(1, 2, SNil.SINGLETON).getArraySize(),
                2
        );

        assertEquals(
                new SPair(1, SNil.SINGLETON).getArraySize(),
                1
        );

        assertEquals(
                SPair.from(1, 2, 3, 4).getArraySize(),
                4
        );
    }
}