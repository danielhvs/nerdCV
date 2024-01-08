import java.util.List;

public class Person {
    private String name;
    private List<Education> educations;
    private List<String> skills;
    private List<String> summary;
    private Contact contact;
    private List<Project> projects;

    public Person(String name, List<Education> educations, List<String> skills, List<String> summary, Contact contact,
            List<Project> projects) {
        this.name = name;
        this.educations = educations;
        this.skills = skills;
        this.summary = summary;
        this.contact = contact;
        this.projects = projects;
    }

    public String getName() {
        return name;
    }

    public List<Education> getEducations() {
        return educations;
    }

    public List<String> getSkills() {
        return skills;
    }

    public List<String> getSummary() {
        return summary;
    }

    public Contact getContact() {
        return contact;
    }

    public List<Project> getProjects() {
        return projects;
    }

}
