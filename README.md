# The example demonstrate the single entry design pattern using zuul service


Application is a demo for single point entry based authentication and authorization


User--request-->Zuul(gateway Service) ---routes to--> microservices(customer-rest) 

Since our gateway service is a single point entry for all requests for different services, It is a good place to do any kind of filtering, currently filter for authentication and authorization  

There are two kinds of tokens used in web    
1)Session token (state is saved at server side, server knows who got the token)   
2)Stateless token (server doesn’t know which client is this token given to so the token should contain complete information to identify user )   

In the current example we are creating simple text based stateless token, it can be encrypted for security if there is a need, TokenUtil class contains code for generating and decoding our token, If there is a need jwt generation and decoding can happen here by using any jwt library  


routes are setup in application.properties in gateway application
applications(services) are currently setup with hsql database

Gateway handles both authentication and authorization currently three kind of urls are supported(spring security or any other security framework is not used)

### Currently Three kinds of url are supported
1)urls starting with /public/ (no authentication required)  
2)urls starting with /admin/  (token required for authentication + authorization for admin role)  
3)urls not starting with /admin/ or /public/ (token required for authentication )  

### Two roles are there
1)admin : can access every urls   
2)user: can access non admin urls  

Run discovery server , it will occupy 8585 port, run gateway (8587 port), run customers app (8586 port)

1)try visiting with get request at http://localhost:8587/admin/customers 
You are not allowed message will be given because request doesn’t have desired token

create customer with POST request from postman at  http://localhost:8587/register (This url is exempted, token not required for it)
password should be minimum 6 character long

BODY Below
{
	"username" : "scooby" ,
	"password" : "123456",
        "role" : "admin"
} 

Add 2 users One by one, one with user role , another with admin role

Customer details with id, username, role is given 

2)Create token with POST request at  http://localhost:8587/createtoken
 
Body below
{
	"username": "scooby",
	"password": "123456",
	"role": "admin"
}

Response will be coma separated generated token is given in format username,password,role , note it

3)We will send this token as HEADER in our requests from now on, key is token , value is generated token in last step
 
Let’s visit http://localhost:8587/admin/customers again this time with token as key and  value is generated token in last step , if we try token of a user with admin role , we will be routed to customer-rest service where we can see details of all customers 
If we will try with token of a user with user role then we will not be authorized as the url is of admin 

To visit /public urls no token is required so we can easily visit http://localhost:8587/public/fruits this will be routed to customer-rest service with /public/fruits uri 




