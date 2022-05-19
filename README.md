# SeatApp Backend

## About

### Purpose

This application was developed during an internship at Xplore. The intent of this application is that a cronos employee
could log in through Azure Ad and then get a list of available seats in the office. Here he would then be able to
reserve a seat so that he would definitely have a seat. Once arrived at the seat, the user would scan a QR code and thus
check in at his seat so that the system knows that he is actually present.

### Api actions

<ul>
<li>Create: You can create a seat with a certain name.</li>
<li>Delete: You can delete a seat.</li>
<li>Reserve: You can reserve a seat from an hour to another.</li>
    The necessary checks are present so that you cannot reserve a seat that is already
    reserved between the chosen hours. You also can't reserve a seat that is unavailable 
    or reserve a seat in a timeslot that is already in the past.
<li>Check in: You can check in on a seat that you reserved.</li>
    You can check in on a seat that you reserved between the timeslot you reserved,
    if you try to check in before the start time or after the end time of the reservation
    it will throw an error.
</ul>

### How to login

Another important implementation is about security, in order to access the application you have to login on Azure Ad but
at this moment only people with an account from Cronos can login onto the application.

## Environment variables

<ul>
<li>SONAR_TOKEN: this variables is for SonarCloud</li>
<li>AZURE_CLIENT_SECRET: this variabel is for the Azure AD application client secret</li>
<li>JWT_SECRET: the secret for generating the JWT token</li>
<li>AWS_ACCESS_KEY_ID: the access id for deployment</li>
<li>AWS_SECRET_ACCESS_KEY: the secret access key for deployment</li>
</ul>

## Development

### Checkstyle & PMD

To keep our code as clean as possible, we have used the Checkstyle and PMD tools throughout the project to check the
code for various rules. These rules are configured in the config directory at the root of the project.

#### Checkstyle <a>https://checkstyle.sourceforge.io/

Checkstyle is a development tool to help programmers write Java code that adheres to a coding standard. It automates the
process of checking Java code to spare humans of this boring (but important) task. This makes it ideal for projects that
want to enforce a coding standard.

As a config for checkstyle we used the Google Java Style with some small adjustments.
<a>https://checkstyle.sourceforge.io/styleguides/google-java-style-20180523/javaguide.html

#### PMD <a>https://pmd.github.io/latest/index.html

PMD is a static source code analyzer. It finds common programming flaws like unused variables, empty catch blocks,
unnecessary object creation, and so forth. It’s mainly concerned with Java and Apex, but supports six other languages.

### Portal Azure

This redirects your application and does a check whether you are logged in with your cronos account or not. If you are
not logged in you will not be redirected to the application. You have to put all your redirect urls into azure portal
otherwise it won't let you go back to the application after you logged in.

### Ngrok

ngrok is the programmable network edge that adds connectivity, security, and observability to your apps with no code
changes.

In case you want to run the application on android you have to use ngrok, android doesn't just work
with `http://localhost` only with `http://10.0.2.2` so we used ngrok to convert the redirect link to a https link.
Because `https://` is allowed by azure portal it will redirect you to the right location.

## Deploy

For creating an image, use this command: `docker build -t seatsapp .`

For creating a container, use this command:
`docker container run -d -p 8080:8080 --name seatsapp seatsapp:latest`

To deploy to the AWS run this command:
`aws lightsail push-container-image --region eu-west-1 --service-name seatapp --label seatapp --image seatsapp:latest`

**If you push a new commit to the main branch, your commit will automatically be deployed.**