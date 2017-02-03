# Assignment Return

This application is for returning graded student assignments efficiently and with student privacy in mind. The idea for the work flow comes from Dan McNabney, a lecturer in the Biology Department at the University of Rochester.

The benefits this project may offer are (1) privacy for student grades during assignment return, (2) ease of return for a lecturer, and (3) student dishonesty prevention.

---

### Preparing to use the Assignment Returner

1. **Gather student assignments**
  * Ensure the stack of student assignments is alphabetically ordered and that students who were unable to provide an assignment have an empty placeholder assignment with their name on it
  * If it is planned to use a conventionally sophisticated office scanner, no staples should be binding the assignments
  * Ideally, it may also be desirable for the assignment to not be double sided depending on the equipment that is planned to be used in the next step

2. **Create combined PDF**

  Taking the alphabetized stack of assignments, most likely using a scanner, create a combined PDF document that has the ordered assignments of each student contiguously.
  
3. **Create a tab delimited file of the class roster**

  The tab delimited header row in this file must contain the following fields:
  
  | firstname | lastname | email |
  | --------- | -------- | ----- |
  
  (The delimiter may be changed to some other character using the `--delimiter, -d` flag.)<br/>
  For a class of 3 students, the **`roster.txt`** file might look like this:
  
  ```tsv
  firstname	lastname	email
  Alan	Bailey	alan_bailey@rochester.edu
  Tom	Hatherford	tom_hatherford@rochester.edu
  Chester	Nixon	chester_nixon@rochester.edu
  ```
  
  Bear in mind, this list should be in the order in which the assignment are arranged. However, a later version of the application may provide a sorting flag where a field is provided to sort on.<br/>
  &nbsp;&nbsp;&nbsp;&nbsp;`--sort,-s FIELD_NAME`, `--ascending,--descending`
  
4. **Feed this file to the application**

  At this point, the application can do the rest. Using the command line in the early version of this application, do the following:
  ```bash
  java -jar assignment-return.jar "Assignment_Title" /path/to/combined.pdf  /path/to/roster.txt
  ```
  The functionality of the application from this point is outlined below. Through the use of different options and flags on the command, a user can further manipulate this pipeline process.
  
  Enjoy!
  
--- 

### Application Work Flow and Intended Usage

1. **Split/Separate PDF into separate student assignment files**

  It will be necessary to have the `combined.pdf` file parsed into the individual assignments and eventually renamed accordingly. The first thing to consider is where these files should be generated to.
  
  * Using the `--pdf-output,-o` option, the user can define the path to which the individual files are saved.
  
  *By default, the files are saved in the same directory as the `combined.pdf` file.*

  --

  The next thing to consider is the naming convention of the files. Naturally the names of students will more than likely be the standard desired incorporation; but users may also prefer a prefix or suffix of: a date/time stamp, pdf-split number (`01` &mdash; `# of students`), student ID, assignment number or title, etc.

  * To accomodate for better file naming customizations, the `--pdf-naming` option will be useful.<br/><br/>
  The exact name of the individual files should be provided after this option, where any references to fields in the `roster.txt` file are be indicated by surrounding the **case-sensitive** name in curly braces: `{{firstname}}`.<br/><br/>
  For other fields like those listed above, consult this table of **reserved field names**:

    | Field Name | Outputs | 
    | :--------: | ------- |
    | #          | The ordered number split-off starting with 1.<br/><br/>*Leading zeros are automatically added depending on the number of total students. With a `roster.txt` containing 200 students*:<br/>&nbsp;&nbsp;&nbsp;&nbsp;`{{#}}` &rarr; `001.pdf` |
    | TIME | Date and time of file creation.<br/><br/>*Better support for timestamps may come in later versions, and will most likely utilize Java's [SimpleDateFormat](https://docs.oracle.com/javase/8/docs/api/java/text/SimpleDateFormat.html).*<br/>&nbsp;&nbsp;&nbsp;&nbsp;`{{TIME yyyyMMdd_HHmmss}}` &rarr; `20170203_160545.pdf` |
    | ASSIGNMENT | The assignment name given at the command line. |
  * In the future, it may be desirable to require the use of a student ID, to make it easier to deal with students with the same name, as rare as it might be.*

  *By default, the naming of individual PDFs will be*<br/>&nbsp;&nbsp;&nbsp;&nbsp;`{{#}}_{{lastname}}-{{firstname}}_{{ASSIGNMENT}}` &rarr; `023_Hatherford-Tom_Quiz3.pdf`

  --

  The final concern is how the `combined.pdf` ought to be split. In other words, the number of pages that make up the assignment.
  
  * Use the `--assignment-length,-n` option and provide a number indicating the number of pages that make up the assignment.
  
  *By default, this number is* (the number of pages in the `combined.pdf` file) / (the number of students in the `roster.txt` file).

2. **User preview for error checking**

  Given the tedious and exact nature of the preparation needed to use this application, it is in the user's best interest to ensure the individual PDFs are associated with the proper student.
  
  A rolling preview window provides the best possible solution for this. With it, a user will have to manually approve the correctness of the document and its name using a certain page number of the individual PDFs (*By default page 1, but can be altered with the `--preview-page` option*).

3. **Email individual PDFs to students**

  The `email` field is required in the `roster.txt` file for this step. By default, this tool will not email students and only split a combined PDF document. For this step to occur, the `--email, -e` option must be specified and the path to a `template.txt` file must provided.
  
  The `template.txt` file undergoes the same replacement-syntax as the individual PDF file naming conventions. An example of this file might be the following:
  
    ```text
    Hello {{firstname}} {{lastname}},
    
    Here is your graded assignment: {{ASSIGNMENT}}.
    If you have any questions, feel free to reply.
    
    - Professor Snape
    ```

    *Later versions of this application, may implement a [rich text format](https://en.wikipedia.org/wiki/Rich_Text_Format) for email templates making different fonts, font decoration, and font sizes possible.*

---
