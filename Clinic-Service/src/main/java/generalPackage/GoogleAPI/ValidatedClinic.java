package generalPackage.GoogleAPI;

public class ValidatedClinic {
    private float ratings;
    private Integer total_user_ratings;
    private String photoURL;

    public ValidatedClinic() {
    }

    public float getRatings() {
        return this.ratings;
    }

    public Integer getTotalUserRatings() {
        return this.total_user_ratings;
    }

    public String getPhotoURL() {
        return this.photoURL;
    }
}
