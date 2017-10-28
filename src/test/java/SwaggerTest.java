import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.collect.ImmutableList;
import com.tw.metadata.MyExampleGenerator;
import com.tw.metadata.util.ResourceUtil;
import io.swagger.models.Model;
import io.swagger.models.RefModel;
import io.swagger.models.Response;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.properties.Property;
import io.swagger.parser.Swagger20Parser;
import io.swagger.parser.SwaggerParser;
import io.swagger.parser.SwaggerResolver;
import io.swagger.parser.util.SwaggerDeserializationResult;
import io.swagger.parser.util.SwaggerDeserializer;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

import static com.tw.metadata.util.ResourceUtil.TEST_RESOURCE_PATH;

/**
 * Created by pzzheng on 10/24/17.
 */
public class SwaggerTest {

    @Test
    public void name() throws IOException {
        SwaggerParser swaggerParser = new SwaggerParser();
        JsonNode jsonNode = new ObjectMapper().readTree(this.getClass().getResourceAsStream("/petstore.json"));
        Swagger read = swaggerParser.read("src/test/resources/petstore.json");
        Swagger20Parser swagger20Parser = new Swagger20Parser();
        Swagger read1 = swagger20Parser.read(jsonNode);
        SwaggerDeserializationResult swaggerDeserializationResult1 = swagger20Parser.readWithInfo(jsonNode);
        new SwaggerDeserializer();
        Swagger parse = swaggerParser.parse(jsonNode.toString());

        BodyParameter bodyParameter = (BodyParameter) parse.getPath("/pet").getPut().getParameters().get(0);
        RefModel schema = (RefModel) bodyParameter.getSchema();
        String $ref = schema.get$ref();
        SwaggerDeserializationResult swaggerDeserializationResult = swaggerParser.readWithInfo(jsonNode.toString());
//        new SwaggerSchemaValidator()
        SwaggerResolver swaggerResolver = new SwaggerResolver(read);
        Swagger resolve = swaggerResolver.resolve();

        SwaggerResolver swaggerResolver1 = new SwaggerResolver(parse, null, null, new SwaggerResolver.Settings().addParametersToEachOperation(false));
        swaggerResolver1.resolve();

    }

    @Test
    public void test_using_cli() throws IOException, URISyntaxException, InterruptedException {
//        resolve ref
        Runtime runtime = Runtime.getRuntime();
        String oriPath = TEST_RESOURCE_PATH + "/petstore1.json";
        String targetPath = TEST_RESOURCE_PATH + "/petstore_d.json";
        InputStream resourceAsStream = this.getClass().getResourceAsStream("petstore.json");
        Map map = new ObjectMapper().readValue(resourceAsStream, Map.class);
//        Thread.sleep(2000);
        File file = ResourceUtil.createFileFromObject(map, "petstore1.json", ResourceUtil.ResourceType.TEST_RESOURCE);
        Process exec = null;
//        File oriFile = new File(oriPath);
//        if (!oriFile.isFile()) {
//            boolean write = false;
//            for (int i = 0; i < 3; i++) {
//                if (oriFile.isFile()) {
//                    exec = runtime.exec(String.format("swagger bundle -r %s -o %s", oriPath, targetPath));
//                    write = true;
//                    break;
//                }
//                Thread.sleep(5000);
//            }
//            if(!write) {
//                throw new TooBigFileException("swagger spec is too big!");
//            }
//        } else {
            exec = runtime.exec(String.format("swagger bundle -r %s -o %s", oriPath, targetPath));
//        }
//        InputStream inputStream = exec.getInputStream();
//        System.out.println(IOUtils.toString(inputStream));
//        InputStream errorStream = exec.getErrorStream();
//        int available = errorStream.available();
//        System.out.println(IOUtils.toString(errorStream));
//        System.out.println(available > 0);

        SwaggerParser swaggerParser = new SwaggerParser();
        Swagger read_d = swaggerParser.read(getJsonNodeFromPath("petstore_d.json"), true);
        Swagger read = swaggerParser.read(getJsonNodeFromPath("petstore1.json"), true);


        String mediatype = "application/json";
        MyExampleGenerator exampleGenerator = new MyExampleGenerator(read.getDefinitions());
//      request schema and example
        BodyParameter bodyParameter = (BodyParameter) read_d.getPaths().get("/pet").getPut().getParameters().get(0);
        Model requestSchema = bodyParameter.getSchema();
        Object o = exampleGenerator.resolveModelToExample(mediatype, requestSchema, Collections.emptySet());
        System.out.println("********************* request ********************");
        System.out.println(new ObjectMapper().writeValueAsString(o));

//      response schema and example
        Response response = read_d.getPath("/pet/findByStatus").getGet().getResponses().get("200");
        Property responseSchema = response.getSchema();
        Map<String, Object> responseExamples = response.getExamples();
        Optional<Object> example = exampleGenerator.resolvePropertyToExample(responseExamples, ImmutableList.of(mediatype), responseSchema);

        System.out.println("********************* response ********************");
        System.out.println(example.get());

//        file.delete();
    }

    private JsonNode getJsonNodeFromPath(String jsonFilePath) throws IOException, URISyntaxException {
        InputStream resourceAsStream = this.getClass().getResourceAsStream(jsonFilePath);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(resourceAsStream);
        resourceAsStream.close();
        return jsonNode;
    }
}
