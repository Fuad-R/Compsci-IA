public class PasswordValidation extends BankingApp {
    
    public static boolean validatePassword(String password) {
        boolean valid = false;
        boolean hasUppercase = !password.equals(password.toLowerCase());
        boolean hasLowercase = !password.equals(password.toUpperCase());
        boolean hasSpecialChar = !password.matches("[A-Za-z0-9 ]*");
        boolean hasNumber = password.matches(".*\\d.*");
        boolean hasCorrectLength = password.length() >= 8 && password.length() <= 16;

        if (hasUppercase && hasLowercase && hasSpecialChar && hasNumber && hasCorrectLength) {
            valid = true;
        }

        return valid;
    }

}
