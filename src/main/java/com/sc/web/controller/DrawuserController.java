package com.sc.web.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.sc.api.constant.IConstants;
import com.sc.api.model.Page;
import com.sc.api.model.Pd;
import com.sc.api.response.ReturnModel;
import com.sc.web.util.RestTemplateUtil;
import com.sc.web.util.WXUtil;

@Controller
@RequestMapping("drawuser")
public class DrawuserController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(DrawuserController.class);

	@Autowired
	RestTemplateUtil rest;

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

		rest.post(IConstants.SC_SERVICE_KEY, "drawuser/save", pd, Pd.class);

		mv.addObject("msg", getMessageUrl("MSG_CODE_ADD_SUCCESS", new Object[] { "抽奖记录" }, ""));
		mv.setViewName("redirect:/drawuser/listPage");
		logger.info("新增抽奖记录成功");
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
	public ModelAndView delete(@RequestParam String DRAWUSER_ID) throws Exception {
		ModelAndView mv = this.getModelAndView();
		Pd pd = new Pd();
		pd.put("DRAWUSER_ID", DRAWUSER_ID);

		rest.post(IConstants.SC_SERVICE_KEY, "drawuser/delete", pd, Pd.class);

		mv.addObject("msg", getMessageUrl("MSG_CODE_DELETE_SUCCESS", new Object[] { "抽奖记录" }, ""));
		mv.setViewName("redirect:/drawuser/listPage");
		logger.info("删除抽奖记录成功");

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

		rest.post(IConstants.SC_SERVICE_KEY, "drawuser/edit", pd, Pd.class);

		mv.addObject("msg", getMessageUrl("MSG_CODE_EDIT_SUCCESS", new Object[] { "抽奖记录" }, ""));
		mv.setViewName("redirect:/drawuser/listPage");
		logger.info("修改抽奖记录成功");
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

		Page page = rest.post(IConstants.SC_SERVICE_KEY, "drawuser/listPage", pd, Page.class);

		mv.setViewName("/drawuser/list");
		mv.addObject("page", page);
		mv.addObject("pd", pd);
		logger.info("分页查询抽奖记录信息");
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

		mv.addObject("pd", pd);
		mv.setViewName("drawuser/add");
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

		pd = rest.post(IConstants.SC_SERVICE_KEY, "drawuser/find", pd, Pd.class);
		mv.addObject("pd", pd); // 放入视图容器

		mv.setViewName("drawuser/edit");
		return mv;
	}

	@RequestMapping(value = "/goInfo")
	public ModelAndView goInfo() throws Exception {
		ModelAndView mv = this.getModelAndView();
		Pd pd = new Pd();
		pd = this.getPd();

		pd = rest.post(IConstants.SC_SERVICE_KEY, "drawuser/find", pd, Pd.class);
		mv.addObject("pd", pd); // 放入视图容器

		mv.setViewName("drawuser/info");
		return mv;
	}

	@RequestMapping(value = "/sendNoAddress")
	@ResponseBody
	public ReturnModel deletePrizeitems() throws Exception {
		ReturnModel rm = new ReturnModel();
		Pd pd = new Pd();
		pd = this.getPd();

		Pd user = (Pd) getSession().getAttribute(IConstants.USER_SESSION);

		Pd pdm = new Pd();
		pdm.put("COMPANY_ID", user.getString("COMPANY_ID"));

		pdm = rest.post(IConstants.SC_SERVICE_KEY, "merchant/findBy", pdm, Pd.class);

		WXUtil.sendMessage("温馨提示:发现你没有快递信息不完整,完善信息才能处理.", pd.getString("OPENID"));

		return rm;
	}
}
