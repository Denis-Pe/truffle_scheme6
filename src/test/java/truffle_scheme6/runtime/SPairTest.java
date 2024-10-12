package truffle_scheme6.runtime;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.StreamSupport;

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

    @Test
    public void nth() {
        var proper = SPair.from(IntStream.range(0, 10).boxed().toArray());

        for (int i = 0; i < 10; i++) {
            assertEquals(proper.nth(i), i);
        }

        var improper = new SPair(
                0,
                new SPair(1,
                        new SPair(2,
                                new SPair(3,
                                        new SPair(4,
                                                new SPair(5,
                                                        new SPair(6,
                                                                new SPair(7,
                                                                        new SPair(8, 9))))))))
        );

        for (int i = 0; i < 10; i++) {
            assertEquals(improper.nth(i), i);
        }

        assertThrows(IndexOutOfBoundsException.class, () -> proper.nth(10));
        assertThrows(IndexOutOfBoundsException.class, () -> improper.nth(10));

        proper.setNth(0, 11);
        assertEquals(proper.getCar(), 11);
        proper.setNth(9, 66);
        assertEquals(proper.nth(9), 66);

        improper.setNth(0, 11);
        assertEquals(improper.getCar(), 11);
        improper.setNth(9, 66);
        assertEquals(improper.nth(9), 66);
    }

    @Test
    public void iterator() {
        var proper1to4 = SPair.from(1, 2, 3, 4, SNil.SINGLETON);

        assertEquals(
                StreamSupport.stream(proper1to4.spliterator(), false).toList(),
                List.of(1, 2, 3, 4)
        );

        var improper1to5 = SPair.from(1, 2, 3, 4, 5);

        assertEquals(
                StreamSupport.stream(improper1to5.spliterator(), false).toList(),
                List.of(1, 2, 3, 4, 5)
        );
    }
}