public class Contact {
    private String email;
    private String slack;
    private Github github;
    private String phone;

    public Contact(String email, String slack, Github github, String phone) {
        this.email = email;
        this.slack = slack;
        this.github = github;
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public String getSlack() {
        return slack;
    }

    public Github getGithub() {
        return github;
    }

    public String getPhone() {
        return phone;
    }

}
