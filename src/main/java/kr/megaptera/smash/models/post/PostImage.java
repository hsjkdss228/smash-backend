package kr.megaptera.smash.models.post;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.Objects;

@Embeddable
public class PostImage {
    @Column(name = "image_url", length = 2048)
    private String url;

    @Column(name = "is_thumbnail_image")
    private Boolean isThumbnailImage;

    private PostImage() {

    }

    public PostImage(String url, Boolean isThumbnailImage) {
        this.url = url;
        this.isThumbnailImage = isThumbnailImage;
    }

    public String url() {
        return url;
    }

    public Boolean isThumbnailImage() {
        return isThumbnailImage;
    }

    public static PostImage fakeThumbnailImage() {
        return new PostImage("Thumbnail Image Url", true);
    }

    public static PostImage fakeNotThumbnailImage() {
        return new PostImage("Image Url", false);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PostImage postImage = (PostImage) o;
        return Objects.equals(url, postImage.url);
    }

    @Override
    public int hashCode() {
        return Objects.hash(url);
    }

    @Override
    public String toString() {
        return "PostImage{" +
            "url='" + url + '\'' +
            ", isThumbnailImage=" + isThumbnailImage +
            '}';
    }
}
