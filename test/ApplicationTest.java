import static org.junit.Assert.assertEquals;
import static play.mvc.Http.Status.SEE_OTHER;

import org.junit.Test;

import play.mvc.Result;
import controllers.Application;

public class ApplicationTest {

    @Test
    public void redirectHomepage() {
    	Result result = new Application().index();
    	assertEquals(SEE_OTHER, result.status());
    	assertEquals("/computers", result.redirectLocation());
    }
    
}
