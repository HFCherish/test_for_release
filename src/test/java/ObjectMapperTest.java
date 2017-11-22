import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.Map;

public class ObjectMapperTest {

    @Test
    public void name() {
        ObjectMapper objectMapper = new ObjectMapper();
        TestObjectMapper testObjectMapper = new TestObjectMapper();
        testObjectMapper.notContained = "not contained";
        testObjectMapper.contained = "contained";
        Map map = objectMapper.convertValue(testObjectMapper, Map.class);
        System.out.println(map);
    }
    @JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
    static class TestObjectMapper {
        static Logger logger = LogManager.getLogger(TestObjectMapper.class);
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        private String notContained;
        private String contained;

    }

}
