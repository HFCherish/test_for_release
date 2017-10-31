import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.tw.metadata.util.MyExampleGenerator;
import io.swagger.models.Model;
import io.swagger.models.Response;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.properties.Property;
import io.swagger.parser.SwaggerParser;
import org.junit.Test;

import java.io.*;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * Created by pzzheng on 10/30/17.
 */
public class SwaggerTest {

    @Test
    public void name() throws IOException, InterruptedException {
        String petstore = "swagger/petstore.json";
        String petstore1 = "swagger/petstore1.json";
        String petstore_d = "swagger/petstore_d.json";
        File petstore_file = new File(petstore);
        File petstore1_file = new File(petstore1);
        File petstore_d_file = new File(petstore_d);
        petstore1_file.delete();
        petstore_d_file.delete();

//        get object from petstore
        InputStream resourceAsStream = new FileInputStream(petstore);
        Map map = new ObjectMapper().readValue(resourceAsStream, Map.class);

//        write to petstore1
        FileOutputStream fileOutputStream = new FileOutputStream(petstore1_file);
        new ObjectMapper().writeValue(fileOutputStream, map);

//        get petstore1 swagger
        SwaggerParser swaggerParser = new SwaggerParser();
        Swagger petstore1_swagger = getSwagger(petstore1_file, swaggerParser);

//        write to petstore_d
        Runtime runtime = Runtime.getRuntime();
        runtime.exec(String.format("swagger bundle -r %s -o %s", petstore1, petstore_d)).waitFor();

        Swagger petstore_d_swagger = getSwagger(petstore_d_file, swaggerParser);

        String mediatype = "application/json";
        MyExampleGenerator exampleGenerator = new MyExampleGenerator(petstore_d_swagger.getDefinitions());
//      request schema and example
        BodyParameter bodyParameter = (BodyParameter) petstore_d_swagger.getPaths().get("/pet").getPut().getParameters().get(0);
        Model requestSchema = bodyParameter.getSchema();
        Object o = exampleGenerator.resolveModelToExample(mediatype, requestSchema, Collections.emptySet());
        TestHelper.TEST_LOG.debug("********************* request ********************");
        TestHelper.TEST_LOG.debug(new ObjectMapper().writeValueAsString(o));

//      response schema and example
        Response response = petstore_d_swagger.getPath("/pet/findByStatus").getGet().getResponses().get("200");
        Property responseSchema = response.getSchema();
        Map<String, Object> responseExamples = response.getExamples();
        Optional<Object> example = exampleGenerator.resolvePropertyToExample(responseExamples, ImmutableList.of(mediatype), responseSchema);

        TestHelper.TEST_LOG.debug("********************* response ********************");
        TestHelper.TEST_LOG.debug(example.get());
    }

    private Swagger getSwagger(File petstore1_file, SwaggerParser swaggerParser) throws IOException {
        JsonNode jsonNode = new ObjectMapper().readTree(petstore1_file);
        Swagger petstore1_swagger = swaggerParser.read(jsonNode);
        return petstore1_swagger;
    }
}
