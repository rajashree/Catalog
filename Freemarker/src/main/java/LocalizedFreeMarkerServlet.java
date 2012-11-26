import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;

public class LocalizedFreeMarkerServlet extends HttpServlet {
    private static final long serialVersionUID = 9203417206521769742L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Create configuration
        Configuration configuration = new Configuration();

        // Setup directory for templates
        //configuration.setDirectoryForTemplateLoading(new File("web"));
        configuration.setServletContextForTemplateLoading(getServletConfig().getServletContext(),"/");

        // Set the locale
        configuration.setLocale(Locale.FRENCH);

        // Get the hello-world.ftl template
        Template template = configuration.getTemplate("hello-world.ftl");

        // Process template
        try {
            template.process(new HashMap(), response.getWriter());
        } catch (TemplateException e) {
            throw new ServletException(e);
        }
    }
}
