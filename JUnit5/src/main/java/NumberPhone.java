public class NumberPhone {
    public static String inputNumberPhone(String NumbPhone) {
        // Kiem tra ki tu dau tien de trong
        if (NumbPhone.startsWith(" ")) {
            return "Kí tự đầu tiên không được để trống" ;
        }
        if (NumbPhone.trim().isEmpty()) {
            return "Không được để trống" ;
        }
        // Kiểm tra không chứa kí tự đặc biệt
        if (!NumbPhone.matches("^[a-zA-Z0-9\\s]*$")) {
            return "Không chứa kí tự đặc biệt";
        }
        // Kiem tra dinh dang của Phone Number
        if (!NumbPhone.matches("^(0|\\+84)[1-9][0-9]{8,9}$")) {
            return "Không đúng định dạng";
        }
        return "Bạn đã nhập đúng";
    }
}
