import freemarker.cache.CacheStorage;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CachingFreeMarkerServlet extends HttpServlet {
    private static final long serialVersionUID = -6607860679724967149L;
    private Configuration configuration = new Configuration();

  @Override
  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    
    // Setup TemplateLoader
    configuration.setTemplateLoader(new WebTemplateLoader(config.getServletContext()));

    // Setup caching
    configuration.setCacheStorage(new MapCache());
    configuration.setTemplateUpdateDelay(30);
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Template template = configuration.getTemplate("hello-world.ftl");
    try {
      template.process(new HashMap(), response.getWriter());
    } catch (TemplateException e) {
      throw new ServletException(e);
    }
  }

  public static class MapCache implements CacheStorage {
    private final Map<Object, Object> cache = new HashMap<Object, Object>();

    public Object get(Object key) {
      return cache.get(key);
    }

    public void put(Object key, Object value) {
      cache.put(key, value);
    }

    public void remove(Object key) {
      cache.remove(key);
    }

    public void clear() {
      cache.clear();
    }
  }

  public static class WebTemplateLoader implements TemplateLoader {
    private final ServletContext context;

    public WebTemplateLoader(ServletContext context) {
      this.context = context;
    }

    public Object findTemplateSource(String name) throws IOException {
      String path = context.getRealPath(name);
      File f = new File(path);
      if (f.isFile()) {
        return f;
      }

      return null;
    }

    public long getLastModified(Object templateSource) {
      return ((File) templateSource).lastModified();
    }

    public Reader getReader(Object templateSource, String encoding) throws IOException {
      return new InputStreamReader(new FileInputStream((File) templateSource), encoding);
    }

    public void closeTemplateSource(Object templateSource) throws IOException {
      // Not needed
    }
  }
}
