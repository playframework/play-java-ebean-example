package models;

import io.ebean.Ebean;
import io.ebean.Finder;
import io.ebean.Model;
import play.data.validation.Constraints;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.LinkedHashMap;
import java.util.Map;



/**
 * Company entity managed by Ebean
 */
@Entity 
public class Company extends BaseModel {

    private static final long serialVersionUID = 1L;

    @Constraints.Required
    public String name;

    public static Map<String,String> options() {
        LinkedHashMap<String,String> options = new LinkedHashMap<String,String>();
        for(Company c: Ebean.find(Company.class).orderBy("name").findList()) {
            options.put(c.id.toString(), c.name);
        }
        return options;
    }

}

