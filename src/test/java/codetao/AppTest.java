package codetao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import java.util.HashMap;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@EnableAutoConfiguration
public class AppTest{

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void testLogin(){
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
        Map<String, String> data=new HashMap<>();
        data.put("username", "demo");
        data.put("password", "demo");
        HttpEntity<?> request = new HttpEntity(data, headers);
        ResponseEntity<?> response = testRestTemplate.postForEntity("/login", request, Map.class);

        Assert.assertEquals(response.getStatusCode().value(), HttpStatus.OK.value());

        System.out.println("token="+response.getHeaders().get(HttpHeaders.AUTHORIZATION));
    }
}
