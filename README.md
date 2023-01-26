# Stanford NER Server

A very small REST API for the [Stanford Named Entity Recognition software](https://nlp.stanford.edu/software/CRF-NER.html).

## How to use

The Server only has a single endpoint:

```http request
GET /ner?input=Stanford University is a private university in Stanford, California
```

This will classify the string given as `input` and return an XML representation of the result:

```xml
<CLASSIFICATION>
    <ORGANIZATION>Stanford University</ORGANIZATION> is a private university in <LOCATION>Stanford</LOCATION>, <LOCATION>California</LOCATION>
</CLASSIFICATION>
```

## How to run

Run the included Dockerfile:

```shell
docker build -t stanford-ner-server .
docker run -d stanford-ner-server
```

The Dockerfile downloads the Stanford NER version 4.2.0, then builds the server application and runs it

---

Made with ❤️ by InsiderPie
