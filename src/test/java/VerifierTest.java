import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class VerifierTest {
    @Test
    void testFileName() {
        assertThrows(NullPointerException.class, () -> Verifier.verify(null));
    }

    @Test
    void testPolicyName() {
        assertThrows(RuntimeException.class, () -> Verifier.verify("PolicyNameNotString.json"));
    }

    @Test
    void testIfElementsAreNull() {
        assertThrows(RuntimeException.class, () ->Verifier.verify("PolicyDocumentNull.json"));
        assertThrows(RuntimeException.class, () ->Verifier.verify("StatementNull.json"));
        assertThrows(RuntimeException.class, () ->Verifier.verify("StatementArrayNull.json"));
        assertThrows(RuntimeException.class, () ->Verifier.verify("ResourceNull.json"));
    }

    @Test
    void testIfResourceIsAsterisk() {
        assertFalse(Verifier.verify("demo.json"));
    }

    @Test
    void testIfResourceIsNotAsterisk() {
        assertTrue(Verifier.verify("ResourceNotAsterisk1.json"));
        assertTrue(Verifier.verify("ResourceNotAsterisk2.json"));
    }

    @Test
    void testIfFileIsEmpty() {
        assertThrows(RuntimeException.class, () -> Verifier.verify("empty.json"));
    }

    @Test
    void testIfFileIsJson() {
        assertThrows(IllegalArgumentException.class, () -> Verifier.verify("notJson.txt"));
        assertThrows(IllegalArgumentException.class, () -> Verifier.verify("csvFile.csv"));
    }

}
