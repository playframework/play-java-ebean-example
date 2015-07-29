import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import org.junit.*;

import play.mvc.*;
import play.test.*;
import play.data.DynamicForm;
import play.data.validation.ValidationError;
import play.data.validation.Constraints.RequiredValidator;
import play.i18n.Lang;
import play.libs.F;
import play.libs.F.*;
import play.twirl.api.Content;
import static play.mvc.Http.Status.BAD_REQUEST;
import static play.mvc.Http.Status.OK;
import static play.mvc.Http.Status.SEE_OTHER;
import static play.test.Helpers.*;
import static org.junit.Assert.*;
import controllers.Application;

public class ApplicationFunctionalTest {

    @Test
    public void listComputersOnTheFirstPage() {
        running(fakeApplication(inMemoryDatabase("test")), () -> {
        	Result result = callAction(controllers.routes.ref.Application.list(0, "name", "asc", ""));
            assertEquals(OK, result.status());
            assertTrue(contentAsString(result).contains("574 computers found"));
        });
    }
    
    @Test
    public void filterComputerByName() {
    	running(fakeApplication(inMemoryDatabase("test")), () -> {
           
           Result result = callAction(controllers.routes.ref.Application.list(0, "name", "asc", "Apple"));

           assertThat(status(result)).isEqualTo(OK);
           assertThat(contentAsString(result)).contains("13 computers found");
           
        });
    }
    
    @Test
    public void createANewComputer() {
        running(fakeApplication(), new Runnable() {
            public void run() {
                Result result = callAction(controllers.routes.ref.Application.save());

                assertThat(status(result)).isEqualTo(BAD_REQUEST);
                
                Map<String,String> data = new HashMap<String,String>();
                data.put("name", "FooBar");
                data.put("introduced", "badbadbad");
                data.put("company.id", "1");
                
                result = callAction(
                    controllers.routes.ref.Application.save(), 
                    fakeRequest().withFormUrlEncodedBody(data)
                );
                
                assertThat(status(result)).isEqualTo(BAD_REQUEST);
                assertThat(contentAsString(result)).contains("<option value=\"1\" selected=\"selected\">Apple Inc.</option>");
                assertThat(contentAsString(result)).contains("<input type=\"date\" id=\"introduced\" name=\"introduced\" value=\"badbadbad\" />");
                assertThat(contentAsString(result)).contains("<input type=\"text\" id=\"name\" name=\"name\" value=\"FooBar\" />");
                
                data.put("introduced", "2011-12-24");
                
                result = callAction(
                    controllers.routes.ref.Application.save(), 
                    fakeRequest().withFormUrlEncodedBody(data)
                );
                
                assertThat(status(result)).isEqualTo(SEE_OTHER);
                assertThat(redirectLocation(result)).isEqualTo("/computers");
                assertThat(flash(result).get("success")).isEqualTo("Computer FooBar has been created");
                
                result = callAction(controllers.routes.ref.Application.list(0, "name", "asc", "FooBar"));
                assertThat(status(result)).isEqualTo(OK);
                assertThat(contentAsString(result)).contains("One computer found");
                
            }
        });
    }

}
