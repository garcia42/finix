# Finix Key Value Store Documentation
http://ec2-54-215-162-35.us-west-1.compute.amazonaws.com
# Connect to Db!
- host: http://ec2-54-215-162-35.us-west-1.compute.amazonaws.com
- db: finix
- User: other_user
- Password: guest
- Table: values
# Features!
  - URL of the form http://ec2-54-215-162-35.us-west-1.compute.amazonaws.com/values/ <id>
        List of available calls, if put or post include body
            -**POST**
            -**GET**
            -**PUT**
            -**DELETE**
  - Nginx listening on port 8080 and 80
  - Tomcat running on port 9000
  - PostGres running on port 5432
  
# Lacking
- No Whitelisting, available to incoming traffic from any ssh or http on ports 8080 and 9000
- No scripts to configure ec2 cluster
u
