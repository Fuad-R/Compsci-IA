public class PasswordValidation extends UserAuth{
    
    public static boolean validatePassword(String password) {
        
        // Check Password against parameters
        if (password.length() < 8) {
            System.out.println("Password must be at least 8 characters long");
            return false;
        }
        if (password.length() > 20) {
            System.out.println("Password must be less than 20 characters long");
            return false;
        }
        if (password.matches("[a-zA-Z0-9]+")) {
            System.out.println("Password must contain at least one special character");
            return false;
        }
        if (password.matches("[a-zA-Z]+")) {
            System.out.println("Password must contain at least one number");
            return false;
        }
        if (password.matches("[0-9]+")) {
            System.out.println("Password must contain at least one letter");
            return false;
        } else{
            return true;
        }
    }
}
