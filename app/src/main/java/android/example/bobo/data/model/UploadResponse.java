package android.example.bobo.data.model;

public class UploadResponse {
    private int statusCode;
    private String message;
    private UploadData data;

    public int getStatusCode() {
        return statusCode;
    }

    public String getMessage() {
        return message;
    }

    public UploadData getData() {
        return data;
    }

    public static class UploadData {
        private String ownerId;
        private String publicId;
        private String secureUrl;
        private String _id;
        private String createdAt;
        private String updatedAt;
        private int __v;

        public String getOwnerId() {
            return ownerId;
        }

        public String getPublicId() {
            return publicId;
        }

        public String getSecureUrl() {
            return secureUrl;
        }

        public String get_id() {
            return _id;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public int get__v() {
            return __v;
        }
    }
}



