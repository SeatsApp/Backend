# Backend

## Deploy 
For creating an image, use this command: `docker build -t seatsapp .
`

For creating a container, use this command:
`docker container run -d -p 8080:8080 --name seatsapp seatsapp:latest`

To deploy to the AWS run this command: 
`aws lightsail push-container-image --region eu-west-1 --service-name seatapp --label seatapp --image seatsapp:latest`