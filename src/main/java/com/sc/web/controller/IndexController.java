package com.sc.web.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.sc.api.constant.IConstants;
import com.sc.api.model.Menu;
import com.sc.api.model.Pd;
import com.sc.web.util.RestTemplateUtil;

@Controller
public class IndexController extends BaseController {

	@Autowired
	RestTemplateUtil rest;

	@RequestMapping("/index")
	public ModelAndView index() throws Exception {
		HttpSession session = getSession();
		ModelAndView mv = this.getModelAndView();
		if (null == session.getAttribute(IConstants.USER_SESSION)) {
			mv.setViewName("unauthorized");
			return mv;
		}
		Pd pd = new Pd();
		pd = this.getPd();

		List<Menu> menuLast = (List<Menu>) getSession().getAttribute(IConstants.MENU_LIST);

		if (CollectionUtils.isEmpty(menuLast)) {
			getSession().removeAttribute(IConstants.USER_SESSION);
			mv.addObject("errInfo", getMessage("DOES_NOT_HAVE_MENUS", new Object[] {}, ""));
			mv.setViewName("unauthorized");
		} else {
			mv.setViewName("/index/main");

			String menuUrl = null;
			Menu first = menuLast.get(0);
			if (!first.getUrl().equals("#")) {
				menuUrl = first.getUrl();
			} else {
				menuUrl = first.getSubMenu().get(0).getUrl();
			}

			session.setAttribute(IConstants.MENU_FIRST_DEFAULT, menuUrl);

		}

		mv.addObject("pd", pd);

		return mv;

	}

	@RequestMapping("/defaultPage")
	public ModelAndView defaultPage() throws Exception {
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("/index/defaultPage");
		return mv;
	}

	@RequestMapping("/unauthorized")
	public ModelAndView unauthorizedPage() throws Exception {
		ModelAndView mv = this.getModelAndView();
		mv.setViewName("unauthorized");
		return mv;
	}
}
