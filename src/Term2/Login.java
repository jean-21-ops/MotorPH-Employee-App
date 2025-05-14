package Term2;

public class Login {
    private String email;
    private String password;

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean login(String email, String password) {
        return this.email != null && this.password != null &&
               this.email.equals(email) && this.password.equals(password);
    }
}
