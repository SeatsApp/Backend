# Backend

## Enivonment variables
<ul>
<li>SONAR_TOKEN: this variables is for SonarCloud</li>
<li>AZURE_CLIENT_SECRET: this variabel is for the Azure AD application client secret</li>
<li>JWT_SECRET: the secret for generating the JWT token</li>
</ul>

## Deploy 
For creating an image, use this command: `docker build -t seatsapp .`

For creating a container, use this command:
`docker container run -d -p 8080:8080 --name seatsapp seatsapp:latest`

To deploy to the AWS run this command: 
`aws lightsail push-container-image --region eu-west-1 --service-name seatapp --label seatapp --image seatsapp:latest`
