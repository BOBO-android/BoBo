package android.example.bobo.data.model;

public class ResendCodeRequest {
    private String email;

    public ResendCodeRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

