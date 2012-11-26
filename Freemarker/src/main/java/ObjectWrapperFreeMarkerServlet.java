import freemarker.template.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ObjectWrapperFreeMarkerServlet extends HttpServlet {
    private static final long serialVersionUID = 1811358584395635641L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Configuration configuration = new Configuration();
        //configuration.setDirectoryForTemplateLoading(new File("web"));
        configuration.setServletContextForTemplateLoading(getServletConfig().getServletContext(),"/");
        configuration.setObjectWrapper(new FortyTwoObjectWrapper());

        Template template = configuration.getTemplate("object-wrapper.ftl");

        // Create the map
        Map<String, Object> root = new HashMap<String, Object>();
        root.put("string", "Hello World");

        try {
            template.process(new SimpleHash(root, configuration.getObjectWrapper()), response.getWriter());
        } catch (TemplateException e) {
            throw new ServletException(e);
        }
    }

    public static class FortyTwoObjectWrapper implements ObjectWrapper {
        public TemplateModel wrap(Object obj) throws TemplateModelException {
            return new SimpleNumber(42);
        }
    }
}
