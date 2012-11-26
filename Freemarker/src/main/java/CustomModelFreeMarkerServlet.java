import freemarker.core.Environment;
import freemarker.template.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomModelFreeMarkerServlet extends HttpServlet {
    private static final long serialVersionUID = 5037142879571230511L;

    @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    Configuration configuration = new Configuration();
    //configuration.setDirectoryForTemplateLoading(new File("web"));
         configuration.setServletContextForTemplateLoading(getServletConfig().getServletContext(),"/");
    
    Template template = configuration.getTemplate("custom-model.ftl");

    // Create the map
    Map<String, Object> root = new HashMap<String, Object>();
    root.put("string", new StringSequence("Hello World"));
    root.put("directive", new EchoDirective());
    root.put("sum", new SumMethod());
    root.put("sumEx", new SumMethodEx());

    try {
      template.process(root, response.getWriter());
    } catch (TemplateException e) {
      throw new ServletException(e);
    }
  }

  public static class StringSequence implements TemplateSequenceModel {
    private final String string;

    public StringSequence(String string) {
      this.string = string;
    }


    public TemplateModel get(int index) throws TemplateModelException {
      return new SimpleScalar("" + string.charAt(index));
    }

    public int size() throws TemplateModelException {
      return string.length();
    }
  }

  public static class EchoDirective implements TemplateDirectiveModel {
    public void execute(Environment env, Map params, TemplateModel[] loopVars, TemplateDirectiveBody body) throws TemplateException, IOException {
      Writer writer = env.getOut();
      for (Object key : params.keySet()) {
        writer.append(key.toString()).append("=").append(params.get(key).toString()).append("\n");
      }
    }
  }

  public static class SumMethod implements TemplateMethodModel {
    public Object exec(List arguments) throws TemplateModelException {
      int sum = 0;
      for (Object argument : arguments) {
        sum += Integer.parseInt(argument.toString());
      }

      return sum;
    }
  }

  public static class SumMethodEx implements TemplateMethodModelEx {
    public Object exec(List arguments) throws TemplateModelException {
      int sum = 0;
      for (Object argument : arguments) {
        TemplateNumberModel value = (TemplateNumberModel) argument;
        sum += value.getAsNumber().intValue();
      }

      return sum;
    }
  }
}
