# CV Generator

This simple project provides a template for creating professional CVs with ease. Users can input their data and include a profile picture to generate a CV.

## TLDR

Call the below but pass your own edn and image file.
```
clj -M:runner resources/sample.edn resources/profile.jpg
```

The result is a [cv.pdf](cv.pdf) file

## Usage

1. Clone the repository:
   ```bash
   git clone https://github.com/danielhvs/nerdCV/
   ```

2. Navigate to the project directory:
   ```bash
   cd nerdCV
   ```

3. Make a sample to check it out by using either of the below:

   ```bash
   make sample
   ```

   ```bash
	clj -M:runner resources/sample.edn resources/profile.jpg
   ```

3.1 Also test without a profile picture:

   ```bash
   make sample-2
   ```

   ```bash
	clj -M:runner resources/sample.edn
   ```

4. Create your `data.edn` file with your information but using the same template (`resources/sample.edn`). Also include your profile picture, for example `profile.png`.

5. Generate your CV:
   ```bash
   clj -M:runner data.edn profile.png
   ```

6. Check the result by opening the `cv.pdf` file.

## Customize

Feel free to fork this project and adjust at your own will.
