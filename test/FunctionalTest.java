import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import play.mvc.Result;
import play.test.WithApplication;

import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static play.test.Helpers.*;

// Use FixMethodOrder to run the tests sequentially
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class FunctionalTest extends WithApplication {

    @Test
    public void redirectHomePage() {
        Result result = route(app, controllers.routes.HomeController.index());

        assertThat(result.status(), equalTo(SEE_OTHER));
        assertThat(result.redirectLocation().get(), equalTo("/computers"));
    }

    @Test
    public void listComputersOnTheFirstPage() {
        Result result = route(app, controllers.routes.HomeController.list(0, "name", "asc", ""));

        assertThat(result.status(), equalTo(OK));
        assertThat(contentAsString(result), containsString("574 computers found"));
    }

    @Test
    public void filterComputerByName() {
        Result result = route(app, controllers.routes.HomeController.list(0, "name", "asc", "Apple"));

        assertThat(result.status(), equalTo(OK));
        assertThat(contentAsString(result), containsString("13 computers found"));
    }

    @Test
    public void createANewComputer() {
        Result result = route(app, controllers.routes.HomeController.save());

        assertThat(result.status(), equalTo(BAD_REQUEST));

        Map<String, String> data = new HashMap<>();
        data.put("name", "FooBar");
        data.put("introduced", "badbadbad");
        data.put("company.id", "1");

        String saveUrl = controllers.routes.HomeController.save().url();
        result = route(app, fakeRequest().bodyForm(data).method("POST").uri(saveUrl));

        assertThat(result.status(), equalTo(BAD_REQUEST));
        assertThat(contentAsString(result), containsString("<option value=\"1\" selected=\"selected\">Apple Inc.</option>"));
        //  <input type="text" id="introduced" name="introduced" value="badbadbad" aria-describedby="introduced_info_0 introduced_error_0" aria-invalid="true" class="form-control">
        assertThat(contentAsString(result), containsString("<input type=\"text\" id=\"introduced\" name=\"introduced\" value=\"badbadbad\" "));
        // <input type="text" id="name" name="name" value="FooBar" aria-describedby="name_info_0" required="true" class="form-control">
        assertThat(contentAsString(result), containsString("<input type=\"text\" id=\"name\" name=\"name\" value=\"FooBar\" "));

        data.put("introduced", "2011-12-24");

        result = route(
                app,
                fakeRequest().bodyForm(data).method("POST").uri(saveUrl)
        );

        assertThat(result.status(), equalTo(SEE_OTHER));
        assertThat(result.redirectLocation().get(), equalTo("/computers"));
        assertThat(result.flash().get("success"), equalTo("Computer FooBar has been created"));

        result = route(app, controllers.routes.HomeController.list(0, "name", "asc", "FooBar"));
        assertThat(result.status(), equalTo(OK));
        assertThat(contentAsString(result), containsString("One computer found"));
    }

}
