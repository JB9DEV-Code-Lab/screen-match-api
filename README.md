# Screen Match API

This project was created to follow a Spring Boot project from Alura course. The goal is to follow as it is, with some
personal touches like English oriented (since it's a Brazilian course it's originally in portuguese), and some other
preferences while programming, but the generaral application idea is going to be the same for the v1.0.0. Any new
feature or idea I implement after the v1.0.0, I'm going to follow the semantic version pattern, by increasing the
version.

The project itself is a simple REST API using Spring Boot and Maven, to build an API that simulates a stream application
back-end.

## How to set up

This project uses the OMDB API, which requires a free registration to generate the API Key, so to be able to run this
project, it's required:

1. Access [OMDB site](https://www.omdbapi.com/apikey.aspx);
2. Choose an account type, type down your email and click on the submit button;
3. You're going to receive an email, to confirm that you own the email by clicking in a link. In this email there is
    also another link which has the apikey in the query params. That's you apikey, which is going to be available some
    minutes after the confirmation;
4. Set this API Key in a system variable called `OMDB_API_KEY` and that's it, now you'll be able to run the application.

**🚧 Document under construction 🚧**
