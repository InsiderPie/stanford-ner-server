package de.insiderpie;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.ling.CoreLabel;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;
import io.micronaut.http.annotation.Produces;
import io.micronaut.http.annotation.QueryValue;

import java.io.IOException;

@Controller("/ner")
public class NER {

    static String serializedClassifier = "lib/stanford-ner-4.2.0/classifiers/english.all.3class.distsim.crf.ser.gz";
    static AbstractSequenceClassifier<CoreLabel> classifier = null;

    static AbstractSequenceClassifier<CoreLabel> getClassifier() throws IOException, ClassNotFoundException {
        if (classifier == null) {
            classifier = CRFClassifier.getClassifier(serializedClassifier);
        }
        return classifier;
    }

    @Get
    @Produces(MediaType.TEXT_XML)
    public String index(@QueryValue("input") String input) throws IOException, ClassNotFoundException {
        return "<CLASSIFICATION>" + getClassifier().classifyWithInlineXML(input) + "</CLASSIFICATION>";
    }
}
