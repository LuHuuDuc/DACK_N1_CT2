import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class TestNumberPhone {
    @Test
    public void inputNumberPhone() {
        NumberPhone input = new NumberPhone();
        assertEquals("Kí tự đầu tiên không được để trống",input.inputNumberPhone(" abc"));
        assertEquals("Không được để trống",input.inputNumberPhone(""));
        assertEquals("Không chứa kí tự đặc biệt",input.inputNumberPhone("!@#"));
        String result = "0912345";
        assertEquals("Không đúng định dạng",input.inputNumberPhone(result));
    }
}
