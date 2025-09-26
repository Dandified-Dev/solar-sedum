# Project Setup

## Installation
Before getting started, you need to install the necessary dependencies. 
Run the following command in the terminal:
```
npm install
```
This will install all the required libraries and packages.

## Running the Frontend
To start the frontend of the project, use the following command:

```
cd frontend
```
```
npm run serve
```
This will initiate the development server and make your application 
available at http://localhost:8080. 
You can open the application in your web browser to preview the changes live during development.

## Preparing the database
Follow these steps to connect your database to your backend, the steps are all inside the application.properties
1. Fill in your database host here: MYSQL_HOST=
2. Fill in your database reference here: spring.datasource.url=
3. Fill in the username for the database here: spring.datasource.username=
4. Fill in the password for the database here: spring.datasource.password=

## Preparing email service
To make the email service work, follow the next steps inside the application.properties
1. Fill in your email adress name here: spring.mail.username=
2. Fill in your email password here: spring.mail.password=

## Running the Backend
The backend of this project is developed using Spring Boot. Follow the steps below to run the backend:

1. Install Java if not already installed.
2. Download and install Maven if not already installed.
3. Navigate to the backend project folder.
4. src > main > java > BackendApplication.
4. Run the file and make sure there are no errors.

#



## Application Access
To have access to the application, follow the next steps

1. Execute the steps and commands above. If you face any difficulties while running the commands, 
retry the installation command: `npm install`
2. Once both the Frontend and Backend are successfully running, you can try logging with the following credentials:

**Admin:** 
- Username: admin
- Password: admin

## Links
**Technical documentation Wiki link:**
- https://gitlab.fdmci.hva.nl/se-ewa/2023-2024-1/solar-2/-/wikis/Technical-Documentation

**Frontend link:**
- https://ewa-front-end-ugsl.onrender.com

**Backend Link:**
- https://ewa-back-end-6158.onrender.com
##
