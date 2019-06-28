package com.sc.web.controller;

import java.io.File;
import java.util.Iterator;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.sc.api.constant.IConstants;
import com.sc.api.model.Pd;
import com.sc.api.util.DateUtil;
import com.sc.web.config.FileConfig;
import com.sc.web.util.RestTemplateUtil;

@Controller
@RequestMapping("merchant")
public class MerchantController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(MerchantController.class);

	@Autowired
	RestTemplateUtil rest;

	@Autowired
	FileConfig fileConfig;

	/**
	 * 修改
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/edit")
	public ModelAndView edit() throws Exception {
		ModelAndView mv = this.getModelAndView();
		Pd pd = new Pd();
		pd = this.getPd();

		CommonsMultipartResolver resolver = new CommonsMultipartResolver(getSession().getServletContext());
		if (resolver.isMultipart(getRequest())) {
			MultipartHttpServletRequest multipartHttpServletRequest = (MultipartHttpServletRequest) getRequest();
			Iterator<String> it = multipartHttpServletRequest.getFileNames();
			while (it.hasNext()) {
				String fileID = it.next();
				MultipartFile file = multipartHttpServletRequest.getFile(fileID);
				if (StringUtils.isEmpty(file.getOriginalFilename())) {
					continue;
				}
				String fileOriginaName = file.getOriginalFilename();
				String tempName = get32UUID() + fileOriginaName.substring(fileOriginaName.lastIndexOf("."));

				File fileNew = new File(fileConfig.getDirCert() + File.separator + tempName);
				file.transferTo(fileNew);

				pd.put("MCHCERT", tempName);
			}
		}

		pd.put("UPDATE_TIME", DateUtil.getTime());

		rest.post(IConstants.SC_SERVICE_KEY, "merchant/edit", pd, Pd.class);

		mv.addObject("msg", getMessageUrl("MSG_CODE_EDIT_SUCCESS", new Object[] { "公众号" }, ""));
		mv.setViewName("redirect:/merchant/goEdit");
		logger.info("修改公众号成功");
		return mv;
	}

	/**
	 * 去修改页面
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/goEdit")
	public ModelAndView goEdit() throws Exception {
		ModelAndView mv = this.getModelAndView();
		Pd pd = new Pd();
		pd = this.getPd();

		Pd user = (Pd) getSession().getAttribute(IConstants.USER_SESSION);

		pd.put("COMPANY_ID", user.getString("COMPANY_ID"));

		pd = rest.post(IConstants.SC_SERVICE_KEY, "merchant/findBy", pd, Pd.class);

		if (null == pd) {
			pd = new Pd();
			pd.put("COMPANY_ID", user.getString("COMPANY_ID"));
			pd = rest.post(IConstants.SC_SERVICE_KEY, "merchant/save", pd, Pd.class);
		}

		mv.addObject("pd", pd); // 放入视图容器

		mv.setViewName("merchant/edit");
		return mv;
	}
}
