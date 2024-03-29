package com.sc.web.controller;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ModelAndView;

import com.sc.api.constant.IConstants;
import com.sc.api.model.Page;
import com.sc.api.model.Pd;
import com.sc.api.util.DateUtil;
import com.sc.web.config.FileConfig;
import com.sc.web.util.RestTemplateUtil;

@Controller
@RequestMapping("prizeitems")
public class PrizeitemsController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(PrizeitemsController.class);

	@Autowired
	RestTemplateUtil rest;

	@Autowired
	FileConfig fileConfig;

	/**
	 * 保存
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/save")
	public ModelAndView save() throws Exception {
		ModelAndView mv = this.getModelAndView();
		Pd pd = new Pd();
		pd = this.getPd();

		Pd user = (Pd) getSession().getAttribute(IConstants.USER_SESSION);

		pd.put("CREATE_USER", user.getString("USER_ID"));
		pd.put("CREATE_TIME", DateUtil.getTime());
		pd.put("COMPANY_ID", user.getString("COMPANY_ID"));

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

				File fileNew = new File(fileConfig.getDirImage() + File.separator + tempName);
				file.transferTo(fileNew);

				pd.put("IMAGE_PATH", tempName);
			}
		}

		rest.post(IConstants.SC_SERVICE_KEY, "prizeitems/save", pd, Pd.class);

		mv.addObject("msg", getMessageUrl("MSG_CODE_ADD_SUCCESS", new Object[] { "奖品池" }, ""));
		mv.setViewName("redirect:/prizeitems/listPage");
		logger.info("新增奖品池成功");
		return mv;
	}

	/**
	 * 删除
	 * 
	 * @param
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/delete")
	@ResponseBody
	public ModelAndView delete(@RequestParam String PRIZEITEMS_ID) throws Exception {
		ModelAndView mv = this.getModelAndView();
		Pd pd = new Pd();
		pd.put("PRIZEITEMS_ID", PRIZEITEMS_ID);

		rest.post(IConstants.SC_SERVICE_KEY, "prizeitems/delete", pd, Pd.class);

		mv.addObject("msg", getMessageUrl("MSG_CODE_DELETE_SUCCESS", new Object[] { "奖品池" }, ""));
		mv.setViewName("redirect:/prizeitems/listPage");
		logger.info("删除奖品池成功");

		return mv;
	}

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

				File fileNew = new File(fileConfig.getDirImage() + File.separator + tempName);
				file.transferTo(fileNew);

				pd.put("IMAGE_PATH", tempName);
			}
		}

		rest.post(IConstants.SC_SERVICE_KEY, "prizeitems/edit", pd, Pd.class);

		mv.addObject("msg", getMessageUrl("MSG_CODE_EDIT_SUCCESS", new Object[] { "奖品池" }, ""));
		mv.setViewName("redirect:/prizeitems/listPage");
		logger.info("修改奖品池成功");
		return mv;
	}

	@RequestMapping("listPage")
	public ModelAndView listPage() throws Exception {
		ModelAndView mv = this.getModelAndView();
		Pd pd = new Pd();
		pd = this.getPd();
		String keywords = pd.getString("keywords"); // 检索条件
		if (null != keywords && !"".equals(keywords)) {
			pd.put("keywords", keywords.trim());
		}

		Pd user = (Pd) getSession().getAttribute(IConstants.USER_SESSION);

		pd.put("COMPANY_ID", user.getString("COMPANY_ID"));

		Page page = rest.post(IConstants.SC_SERVICE_KEY, "prizeitems/listPage", pd, Page.class);

		Pd pdt = new Pd();
		List<Pd> handertypeData = rest.postForList(IConstants.SC_SERVICE_KEY, "prizeitems/listAllHandertype", pdt,
				new ParameterizedTypeReference<List<Pd>>() {
				});
		mv.addObject("handertypeData", handertypeData);

		mv.setViewName("/prizeitems/list");
		mv.addObject("page", page);
		mv.addObject("pd", pd);
		logger.info("分页查询奖品池信息");
		return mv;
	}

	/**
	 * 去新增页面
	 * 
	 * @param
	 * @throws Exception
	 */
	@RequestMapping(value = "/goAdd")
	public ModelAndView goAdd() throws Exception {
		ModelAndView mv = this.getModelAndView();
		Pd pd = new Pd();
		pd = this.getPd();

		Pd pdt = new Pd();
		List<Pd> handertypeData = rest.postForList(IConstants.SC_SERVICE_KEY, "prizeitems/listAllHandertype", pdt,
				new ParameterizedTypeReference<List<Pd>>() {
				});
		mv.addObject("handertypeData", handertypeData);

		mv.addObject("pd", pd);
		mv.setViewName("prizeitems/add");
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

		Pd pdt = new Pd();
		List<Pd> handertypeData = rest.postForList(IConstants.SC_SERVICE_KEY, "prizeitems/listAllHandertype", pdt,
				new ParameterizedTypeReference<List<Pd>>() {
				});
		mv.addObject("handertypeData", handertypeData);

		pd = rest.post(IConstants.SC_SERVICE_KEY, "prizeitems/find", pd, Pd.class);
		mv.addObject("pd", pd); // 放入视图容器

		mv.setViewName("prizeitems/edit");
		return mv;
	}

	@RequestMapping(value = "/goInfo")
	public ModelAndView goInfo() throws Exception {
		ModelAndView mv = this.getModelAndView();
		Pd pd = new Pd();
		pd = this.getPd();

		Pd pdt = new Pd();
		List<Pd> handertypeData = rest.postForList(IConstants.SC_SERVICE_KEY, "prizeitems/listAllHandertype", pdt,
				new ParameterizedTypeReference<List<Pd>>() {
				});
		mv.addObject("handertypeData", handertypeData);

		pd = rest.post(IConstants.SC_SERVICE_KEY, "prizeitems/find", pd, Pd.class);
		mv.addObject("pd", pd); // 放入视图容器

		mv.setViewName("prizeitems/info");
		return mv;
	}
}
