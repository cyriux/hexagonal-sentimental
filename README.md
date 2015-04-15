# hexagonal-sentimental
Sample app of sentiment analysis as an example of hexagonal architecture

*Automatically extract the sentiment of English sentences submitted on the web, using a given lexicon. Keep track of the submitted sentences.*

## Architecture

![Generated Living Diagram](sentimental/hexagonal-architecture.png)

## Running the app

in the command line:

    cd git/hexagonal-sentimental/sentimental
    java -jar target/sentimental-0.0.1-SNAPSHOT.jar server sentimental.yml

## Remarks

- There is an **End-to-end test** marked as @ignored until you install postgres; this test is very convenient
- The **auto-generation of the living diagram** uses the dot-diagram library that I've created. This dependency is brutally provided as a 'system' dependency until it's available on a public repository. Dot-diagram requires **Graphviz** installed (www.graphviz.org)
- The sound files are not mine and are only provided as examples; unfortunately I don't remember where they come from.
