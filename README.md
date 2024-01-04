# CV Generator

This simple project provides a template for creating professional CVs with ease. Users can input their data and include a profile picture to generate a CV.

## Usage

1. Clone the repository:
   ```bash
   git clone https://github.com/danielhvs/nerdCV/
   ```

2. Navigate to the project directory:
   ```bash
   cd nerdCV
   ```

3. Make a sample to check it out:
   ```bash
   make sample
   ```

4. Input your data in some `data.edn` file. Ensure to include a path to your profile picture.

5. Generate your CV:
   ```bash
   clj -M:runner data.edn profile.png
   ```

6. The generated CV will be output

## Data Structure

Update the `data.json` file with your personal information, ensuring the following structure:

```json
{
  "name": "Your Name",
  "title": "Your Title",
  "contact": {
    "email": "your.email@example.com",
    "phone": "+123 456 7890",
    "address": "123 Main Street, City, Country"
  },
  "education": [
    {
      "degree": "Your Degree",
      "school": "University Name",
      "year": "Graduation Year"
    }
  ],
  "experience": [
    {
      "position": "Your Position",
      "company": "Company Name",
      "year": "Start - End Year",
      "description": "Your responsibilities and achievements"
    }
  ],
  "skills": [
    "Skill 1",
    "Skill 2",
    "Skill 3"
  ]
}
```

## Customize

Feel free to modify the template and styling to suit your preferences. Adjust the `template.ejs` file for changes in the CV layout.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

Happy CV building! 🚀
