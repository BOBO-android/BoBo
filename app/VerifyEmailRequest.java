public class VerifyEmailRequest {
    private String storeId;
    private String codeId;

    public VerifyEmailRequest(String storeId, String codeId) {
        this.storeId = storeId;
        this.codeId = codeId;
    }

    // Getters và setters nếu cần
}
