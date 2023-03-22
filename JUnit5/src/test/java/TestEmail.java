import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class TestEmail {
    @Test
    public void inputEmail() {
        Email input = new Email();
        assertEquals("Kí tự đầu tiên không được để trống",input.inputEmail(" dcc1411@gmail.com"));
        assertEquals("Không được để trống",input.inputEmail(""));
        assertEquals("Không đúng định dạng",input.inputEmail("dcc1411@"));
    }
}
