package android.example.bobo.data.model;

public class VerifyRequest {
    private  String username;
    private String codeId;

    public VerifyRequest(String username, String codeId) {
        this.username = username;
        this.codeId = codeId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCodeId() {
        return codeId;
    }

    public void setCodeId(String codeId) {
        this.codeId = codeId;
    }
}
