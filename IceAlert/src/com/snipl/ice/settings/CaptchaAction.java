package com.snipl.ice.settings;

/**
* @Author Kamalakar Challa
*   
*/

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import com.snipl.ice.blog.SaveBlogCommentAction;
import com.snipl.ice.registration.RegistrationAction;
import com.snipl.ice.utility.IceImage;

public class CaptchaAction extends Action{

	public ActionForward execute(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response)
			throws IOException, ServletException {
		if (request.getParameter("captchaId") != null||request.getParameter("captchaReg") != null ||request.getParameter("captchaRegister") != null) {
			try {
			    String userId = request.getParameter("captchaId");
				String registrationCaptcha = request.getParameter("captchaReg");
				String registerCaptcha = request.getParameter("captchaRegister");
				String captcha  = "";
				if(userId!=null){
				}else if(registrationCaptcha!=null)
				{
				    captcha = SaveBlogCommentAction.getCaptcha(registrationCaptcha);
				}else if(registerCaptcha!=null)
				{
				    captcha = RegistrationAction.getCaptcha(registerCaptcha);
				}
				
				BufferedImage image = IceImage.getImageFromString(captcha,false);
				response.setContentType("image/jpg");
				OutputStream o = response.getOutputStream();
				ImageIO.write(image,"jpg",o);
				o.flush();
				o.close();
				
			} catch (Exception e) {
				System.out.print(e.getMessage());
			}
		}
		return null;
	}
	
}
