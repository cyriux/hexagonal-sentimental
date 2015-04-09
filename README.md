# hexagonal-sentimental
Sample app of sentiment analysis as an example of hexagonal architecture

*Automatically extract the sentiment of English sentences submitted on the web, using a given lexicon. Keep track of the submitted sentences.*

## Architecture

![Generated Living Diagram](sentimental/hexagonal-architecture.png)

## Running the app

in the command line:

    cd git/hexagonal-sentimental/sentimental
    java -jar target/sentimental-0.0.1-SNAPSHOT.jar server sentimental.yml

