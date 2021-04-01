package codetao.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;

@Service
public class PermissionService {
    @Value(value="classpath:/meta/permissions.json")
    private Resource resource;

    public List<Map<String, Object>> getPermissions(){
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))){
            StringBuffer message = new StringBuffer();
            String line = null;
            while((line = reader.readLine()) != null){
                message.append(line);
            }
            String jsonStr = message.toString();
            ObjectMapper mapper = new ObjectMapper();
            List<Map<String, Object>> permissions = mapper.readValue(jsonStr, List.class);
            return permissions;
        }catch (Exception e){

        }
        return null;
    }
}
