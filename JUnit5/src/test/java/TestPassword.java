import org.junit.Test;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

public class TestPassword {
    @Test
    public void inputNumberPhone() {
        Password input = new Password();
        assertEquals("Kí tự đầu tiên không được để trống",input.inputPassword(" Cuong123!@#"));
        assertEquals("Không được để trống",input.inputPassword(""));
        assertEquals("Nhập từ 8 đến 12 kí tự",input.inputPassword("Cuong1@"));
        assertEquals("Có ít nhất 1 kí tự số",input.inputPassword("Cuong!@#$%"));
        assertEquals("Có ít nhất 1 kí tự đặc biệt",input.inputPassword("Cuong12345"));
        assertEquals("Không lặp lại 1 kí tự quá nhiều lần",input.inputPassword("C1@ccccccc"));


        // Kiểm tra đúng:
//        assertTrue(input.inputPassword("Cuong1@#$"));
//        // Kiểm tra sai:
//        assertFalse(input.inputPassword(" ")); // Nhap ki tu de trong
//        assertFalse(input.inputPassword(" Cuong!")); // Nhap ki tu dau tien de trong
//        assertFalse(input.inputPassword("Cuong1!")); // Nhap duoi 8 ki tu
//        assertFalse(input.inputPassword("Cuong123!@#abcdefghklmn")); // Nhap tren 12 ki tu
//        assertFalse(input.inputPassword("Cuong12345")); // Chi nhap ki tu so
//        assertFalse(input.inputPassword("Cuong@#$%^")); // Chi nhap  ki tu dac biet
//        assertFalse(input.inputPassword("aaaaaaaaaaaa")); // Nhap nhieu ki tu trung lap
    }
}
