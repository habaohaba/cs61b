package flik;
import org.junit.Test;
import static org.junit.Assert.*;

public class Filk_test {

    @Test
    public void test_filk_library(){
        int a = 128;
        int b = 128;
        assertTrue(Flik.isSameNumber(a,b));
    }
}
