package com.sc.web.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;

import com.sc.api.constant.IConstants;
import com.sc.api.model.Menu;
import com.sc.api.model.Pd;
import com.sc.web.util.RestTemplateUtil;

public class CommonFilter implements Filter {

	private Logger logger = LoggerFactory.getLogger(CommonFilter.class);

	@Autowired
	RestTemplateUtil rest;

	@Override
	public void destroy() {

	}

	@Override
	public void doFilter(ServletRequest arg0, ServletResponse arg1, FilterChain arg2)
			throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) arg0;
		String userID = request.getParameter("userid");
		if (StringUtils.isNotEmpty(userID)) {

			Pd pdu = new Pd();
			pdu.put("USER_ID", userID);
			pdu = rest.post(IConstants.SC_SERVICE_KEY, "common/user/find", pdu, Pd.class);
			pdu.put("ROLE_ID", "1");

			request.getSession().setAttribute(IConstants.USER_SESSION, pdu);

			List<Menu> menus = rest.postForList(IConstants.SC_SERVICE_KEY, "menu/listRoleMenu", pdu,
					new ParameterizedTypeReference<List<Menu>>() {
					});
			List<Menu> menuTemp = new ArrayList<Menu>();
			Map<Integer, List<Menu>> subMenu = new HashMap<Integer, List<Menu>>();

			for (Menu menu : menus) {
				if (menu.getPid() == 0) {
					menuTemp.add(menu);
				} else {
					List<Menu> menust = null;
					if (subMenu.containsKey(menu.getPid())) {
						menust = subMenu.get(menu.getPid());
						menust.add(menu);
						subMenu.put(menu.getPid(), menust);
					} else {
						menust = new ArrayList<Menu>();
						menust.add(menu);
						subMenu.put(menu.getPid(), menust);
					}
				}
			}

			List<Menu> menuLast = new ArrayList<Menu>();
			for (Menu menu : menuTemp) {
				if (!menu.getUrl().equals("#")) {
					menuLast.add(menu);
				} else {
					if (CollectionUtils.isNotEmpty(subMenu.get(menu.getId()))) {
						menu.setSubMenu(subMenu.get(menu.getId()));
						menuLast.add(menu);
					}
				}
			}

			request.getSession().setAttribute(IConstants.MENU_LIST, menuLast);

		}

		arg2.doFilter(arg0, arg1);
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {

	}

}
