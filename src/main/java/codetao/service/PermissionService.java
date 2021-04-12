package codetao.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class PermissionService {

    @Value(value="classpath:/meta/permissions.json")
    private Resource trees;

    @Value(value="classpath:/meta/api_permissions.json")
    private Resource apis;

    public List getPermissions(){
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(trees.getInputStream()))){
            StringBuffer message = new StringBuffer();
            String line;
            while((line = reader.readLine()) != null){
                message.append(line);
            }
            String jsonStr = message.toString();
            ObjectMapper mapper = new ObjectMapper();
            List permissions = mapper.readValue(jsonStr, List.class);
            return permissions;
        }catch (Exception e){

        }
        return null;
    }

    public List getApiPermissions(){
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(apis.getInputStream()))){
            StringBuffer message = new StringBuffer();
            String line;
            while((line = reader.readLine()) != null){
                message.append(line);
            }
            String jsonStr = message.toString();
            List apis = new ObjectMapper().readValue(jsonStr, List.class);
            return apis;
        }catch (Exception e){

        }
        return null;
    }
}
