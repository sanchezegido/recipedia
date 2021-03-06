package models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.ebean.Finder;
import play.data.validation.Constraints.MaxLength;
import play.data.validation.Constraints.Required;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;

@Entity
@Table(name = "reviews")
public class Review extends BaseModel {

    @Required
    @MaxLength(255)
    private String comment;

    @Required
    @DecimalMin(value = "0.0", message = "error.greater")
    @DecimalMax(value = "5.0", message = "error.lower")
    private Float rating;

    @ManyToOne
    @JsonManagedReference
    private User user;

    @ManyToOne
    @JsonBackReference
    private Recipe recipe;

    private static final Finder<Long, Review> find =
            new Finder<>(Review.class);

    public Review() {
        super();
    }

    private static Review findByUserAndRecipe(User user, Recipe recipe) {
        return find
                .query()
                .where()
                    .eq("user.id", user.getId())
                    .eq("recipe.id", recipe.getId())
                .findOne();
    }

    public boolean validateAndSave() {
        if (isReviewDuplicated()) {
            return false;
        }

        this.save();

        return true;
    }

    // A user can only comment once for each recipe
    private boolean isReviewDuplicated() {
        return Review.findByUserAndRecipe(this.user, this.recipe) != null;
    }

    @JsonIgnore
    @Override
    public Long getId() {
        return super.getId();
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Float getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Recipe getRecipe() {
        return recipe;
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }
}
