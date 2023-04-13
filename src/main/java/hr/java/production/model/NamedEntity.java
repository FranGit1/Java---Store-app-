package hr.java.production.model;

/**
 * Abstract class NamedEntity
 */
public abstract class NamedEntity {
    protected String name;
    protected Long id;
    /**
     * Constructor for class NamedEntity with all the available fields
     * @param name
     */
    public NamedEntity(String name,Long id) {
        this.name = name;
        this.id = id;
    }

    public NamedEntity(String name) {
        this.name = name;


    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object obj) {
        NamedEntity n = (NamedEntity) obj;
        return n.name.equals(this.name);
    }
}
