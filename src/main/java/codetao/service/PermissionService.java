package codetao.service;

import codetao.cache.MetaCache;
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
    private Resource permissionsFile;

    @Value(value="classpath:/meta/api_permissions.json")
    private Resource apiPermissionsFile;

    public List getPermissions(){
        List permissions = (List)MetaCache.getMetaCache("permissions");
        if(permissions == null){
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(permissionsFile.getInputStream()))){
                StringBuffer message = new StringBuffer();
                String line;
                while((line = reader.readLine()) != null){
                    message.append(line);
                }
                String jsonStr = message.toString();
                ObjectMapper mapper = new ObjectMapper();
                permissions = mapper.readValue(jsonStr, List.class);
                MetaCache.setMetaCache("permissions", permissions);
            }catch (Exception e){

            }
        }
        return permissions;
    }

    public List getApiPermissions(){
        List apiPermissions = (List)MetaCache.getMetaCache("api_permissions");
        if(apiPermissions == null){
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(apiPermissionsFile.getInputStream()))){
                StringBuffer message = new StringBuffer();
                String line;
                while((line = reader.readLine()) != null){
                    message.append(line);
                }
                String jsonStr = message.toString();
                apiPermissions = new ObjectMapper().readValue(jsonStr, List.class);
                MetaCache.setMetaCache("api_permissions", apiPermissions);
            }catch (Exception e){

            }
        }
        return apiPermissions;
    }

    
}
