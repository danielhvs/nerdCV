import java.util.List;

public class Project {
    private String name;
    private String company;
    private List<String> description;
    private List<String> tech;

    public Project(String name, String company, List<String> description, List<String> tech) {
        this.name = name;
        this.company = company;
        this.description = description;
        this.tech = tech;
    }

    public String getName() {
        return name;
    }

    public String getCompany() {
        return company;
    }

    public List<String> getDescription() {
        return description;
    }

    public List<String> getTech() {
        return tech;
    }

}
