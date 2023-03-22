public class Password {
    public static String inputPassword(String password) {
        // Kiem tra ki tu dau tien de trong
        if (password.startsWith(" ")) {
            return "Kí tự đầu tiên không được để trống" ;
        }
        // Kiem tra de trong the
        if (password.trim().isEmpty()) {
            return "Không được để trống" ;
        }
        // Kiem tra co chua tu 8 den 12 ki tu hay khong
        if (password.length() < 8 || password.length() > 20) {
            return "Nhập từ 8 đến 12 kí tự";
        }
        // Kiểm tra ít nhất một giá trị số
        if (!password.matches(".*\\d.*")) {
            return "Có ít nhất 1 kí tự số";
        }
        // Kiem tra it nhat 1 gia tri dac biet
        if (!password.matches(".*[!@#$%^&*(),.?\":{}|<>].*")) {
            return "Có ít nhất 1 kí tự đặc biệt";
        }
        // Kiem tra password co chua ki tu lap lai nhieu lan hay khong
        for (int i = 0; i < password.length() - 2; i++) {
            if (password.charAt(i) == password.charAt(i+1) && password.charAt(i+1) == password.charAt(i+2)) {
                return "Không lặp lại 1 kí tự quá nhiều lần";
            }
        }
        return "Bạn đã nhập đúng";
    }
}
