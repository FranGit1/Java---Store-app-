package hr.java.production.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Class Category that extends calss NamedEntity
 */
public class Category extends NamedEntity implements Serializable{
    private String description;



    /**
     * onstructor for class Category with all of the avaliable fields
     * @param name
     * @param description
     */
    public Category(String name, String description,Long id) {
        super(name,id);
        this.description = description;
    }


    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName(){
        return this.name;
    }


    @Override
    public boolean equals(Object obj) {
        if(obj==this) {
            return true;
        }
        if (obj == null){
            return false;
        }
        if(!(obj instanceof Category)){
            return false;
        }

        Category i = (Category) obj;
        return (i.description.equals(this.description)) && super.equals(obj);

    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(),description);
    }


    @Override
    public String toString() {
        return super.name;
    }
}
