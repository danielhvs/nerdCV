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
or
   ```bash
	clj -M:runner resources/sample.edn resources/profile.jpg
   ```

4. Create your `data.edn` file with your information but using the same template. Also include your profile picture, for example `profile.png`

5. Generate your CV:
   ```bash
   clj -M:runner data.edn profile.png
   ```

6. Check the result by opening the `cv.pdf` file.

## Customize

Feel free to fork this project and adjust at your own will.
