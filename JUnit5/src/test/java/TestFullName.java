import org.junit.Test;

import static org.junit.Assert.*;

public class TestFullName {
    @Test
    public void inputFullName() {
        FullName input = new FullName();
        assertEquals("Kiểm tra kí tự đặc biệt hoặc kí tự số",input.inputFullName("!@#"));
        assertEquals("Không được để trống",input.inputFullName(""));
        assertEquals("Kiểm tra kí tự đặc biệt hoặc kí tự số",input.inputFullName("123"));
        assertEquals("Kí tự đầu tiên không được để trống",input.inputFullName(" abc"));
    }
}
