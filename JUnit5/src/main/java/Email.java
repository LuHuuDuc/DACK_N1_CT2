public class Email {
    public static String inputEmail(String email) {
        // Kiem tra ki tu dau tien de trong
        if (email.startsWith(" ")) {
            return "Kí tự đầu tiên không được để trống" ;
        }
        // Kiem tra de trong the
        if (email.trim().isEmpty()) {
            return "Không được để trống" ;
        }
        // Kiem tra dinh dang email
        if (!email.matches("^[a-zA-Z0-9_+&*-]+(?:\\." +
                "[a-zA-Z0-9_+&*-]+)*@" +
                "(?:[a-zA-Z0-9-]+\\.)+[a-z" +
                "A-Z]{2,7}$")) {
            return "Không đúng định dạng";
        }
        return "Bạn đã nhập đúng";
    }
}
