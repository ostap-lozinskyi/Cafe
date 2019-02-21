package ua.entity;

import ua.validation.annotation.UniqueCuisine;
import ua.validation.annotation.UniqueIngredient;
import ua.validation.annotation.UniqueMs;
import ua.validation.flag.CuisineFlag;
import ua.validation.flag.IngredientFlag;
import ua.validation.flag.MsFlag;

import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@MappedSuperclass
public abstract class AbstractEntityName extends AbstractEntity {

    @UniqueIngredient(message = "Such an ingredient already exists", groups = IngredientFlag.class)
    @UniqueCuisine(message = "Such a cuisine already exists", groups = CuisineFlag.class)
    @UniqueMs(message = "Such a ms already exists", groups = MsFlag.class)
    @NotBlank(message = "This field cannot be blank", groups = {IngredientFlag.class, CuisineFlag.class, MsFlag.class})
    @Pattern(regexp = "^[A-Z][A-Za-z0-9]+| *$",
            message = "The 'Name' should begin with a capital letter and have at least 2 letters",
            groups = {IngredientFlag.class, CuisineFlag.class})
    @Pattern(regexp = "^[a-z][a-z]*| *$",
            message = "The 'Name' should consists of lowercase letters", groups = {MsFlag.class})
    private String name;

    AbstractEntityName(String name) {
        this.name = name;
    }

    AbstractEntityName() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}