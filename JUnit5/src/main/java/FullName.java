public class FullName {
    public static String inputFullName(String fullName) {
        // Kiem tra de trong the
        if (fullName.trim().isEmpty()) {
            return "Không được để trống" ;
        }

        // Kiem tra ki tu dau tien de trong
        if (fullName.startsWith(" ")) {
            return "Kí tự đầu tiên không được để trống" ;
        }
        // Kiem tra ki tu dac biet va ki tu so
        if (!fullName.matches("^[a-zA-Z]+$")) {
            return "Kiểm tra kí tự đặc biệt hoặc kí tự số" ;
        }

        else{
            return "Bạn đã nhập đúng";
        }
    }
}
