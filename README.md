# clientApp

The stacks used for this project  are Spring Boot.java 17,Gradle,Junit ,mockito and H2 for Database

This is to  build a REST API that allows for creating, updating and searching for a client.
A client should have the following fields, fields marked with * a mandatory
Client
First Name*
Last Name*
Mobile Number
ID Number*
Physical Address
When a client is created or updated the following fields should be validate
ID Number
1. Must be a valid south African ID number
2. No Duplicates ID numbers
Mobile Number
1. No duplicate mobile numbers
When validation fails an appropriate response should be provided.
You should be able to search for a client using any one of the following fields FirstName or ID
Number or Phone Number
