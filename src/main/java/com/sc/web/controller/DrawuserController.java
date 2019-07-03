package com.sc.web.controller;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants.SignType;
import com.github.wxpay.sdk.WXPayUtil;
import com.sc.api.constant.IConstants;
import com.sc.api.model.Page;
import com.sc.api.model.Pd;
import com.sc.api.response.ReturnModel;
import com.sc.api.util.DateUtil;
import com.sc.web.config.FileConfig;
import com.sc.web.config.WXPayConfigImpl;
import com.sc.web.util.RestTemplateUtil;
import com.sc.web.util.WXUtil;

@Controller
@RequestMapping("drawuser")
public class DrawuserController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(DrawuserController.class);

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
	public ReturnModel sendNoAddress() {
		ReturnModel rm = new ReturnModel();
		Pd pd = new Pd();
		pd = this.getPd();

		Pd user = (Pd) getSession().getAttribute(IConstants.USER_SESSION);

		Pd pdm = new Pd();
		pdm.put("COMPANY_ID", user.getString("COMPANY_ID"));

		pdm = rest.post(IConstants.SC_SERVICE_KEY, "merchant/findBy", pdm, Pd.class);

		String APPID = pdm.getString("APPID");
		String APPSECRET = pdm.getString("APPSECRET");

		try {
			new WXUtil(APPID, APPSECRET).sendMessage("【云码系统】 发现快递信息不完整,请尽快处理,以免影响奖品发放.", pd.getString("OPENID"));
		} catch (Exception e) {
			rm.setFlag(false);
		}

		return rm;
	}

	@RequestMapping(value = "/express")
	@ResponseBody
	public ReturnModel express() throws Exception {
		ReturnModel rm = new ReturnModel();
		Pd pd = new Pd();
		pd = this.getPd();

		Pd pdd = rest.post(IConstants.SC_SERVICE_KEY, "drawuser/find", pd, Pd.class);
		pdd.put("STATE", IConstants.STRING_1);
		rest.post(IConstants.SC_SERVICE_KEY, "drawuser/edit", pdd, Pd.class);

		pd.put("CREATE_TIME", DateUtil.getTime());
		pd.put("STATE", IConstants.STRING_0);
		rest.post(IConstants.SC_SERVICE_KEY, "express/save", pd, Pd.class);

		return rm;
	}

	@RequestMapping(value = "/money")
	@ResponseBody
	public ReturnModel money() {
		ReturnModel rm = new ReturnModel();
		Pd pd = new Pd();
		pd = this.getPd();

		try {
			Pd user = (Pd) getSession().getAttribute(IConstants.USER_SESSION);

			Pd pdm = new Pd();
			pdm.put("COMPANY_ID", user.getString("COMPANY_ID"));

			pdm = rest.post(IConstants.SC_SERVICE_KEY, "merchant/findBy", pdm, Pd.class);

			String APPID = pdm.getString("APPID");
			String APPSECRET = pdm.getString("APPSECRET");
			String MCHID = pdm.getString("MCHID");
			String MCHKEY = pdm.getString("MCHKEY");
			String MCHCERT = pdm.getString("MCHCERT");

			WXPayConfigImpl config = new WXPayConfigImpl(APPID, APPSECRET, MCHID, MCHKEY,
					fileConfig.getDirCert() + File.separator + MCHCERT);

			logger.info("进入红包付款到个人");
			WXPay wxpay = new WXPay(config, SignType.MD5);
			Map<String, String> parameters = new HashMap<String, String>();
			parameters.put("mch_appid", config.getAppID());
			parameters.put("mchid", config.getMchID());
			parameters.put("nonce_str", WXPayUtil.generateNonceStr());
			parameters.put("partner_trade_no", pd.getString("DRAWUSER_ID"));
			parameters.put("openid", pd.getString("OPENID"));
			parameters.put("check_name", "NO_CHECK");
			String fee = pd.getString("MONEY");
			if (fee.indexOf(".") != -1) {
				fee = fee.substring(0, fee.indexOf("."));
			}
			parameters.put("amount", fee);
			parameters.put("spbill_create_ip", getIpAddr(getRequest()));
			parameters.put("desc", "云码系统红包现金");
			// 签名
			String sign = WXPayUtil.generateSignature(parameters, config.getKey());
			parameters.put("sign", sign);

			String notityXml = wxpay.requestWithCert(
					"https://api.mch.weixin.qq.com/mmpaymkttransfers/promotion/transfers", parameters,
					config.getHttpConnectTimeoutMs(), config.getHttpReadTimeoutMs());
			System.err.println(notityXml);

			Map<String, String> dataMap = WXPayUtil.xmlToMap(notityXml);

			String resultCode = dataMap.get("result_code");
			if ("SUCCESS".equals(resultCode)) {
				Pd pdd = new Pd();
				pdd.put("DRAWUSER_ID", pd.getString("DRAWUSER_ID"));
				pdd = rest.post(IConstants.SC_SERVICE_KEY, "drawuser/find", pdd, Pd.class);
				pdd.put("STATE", IConstants.STRING_1);
				rest.post(IConstants.SC_SERVICE_KEY, "drawuser/edit", pdd, Pd.class);
			} else {
				rm.setFlag(false);
				rm.setMessage(dataMap.get("err_code_des"));
			}

		} catch (Exception e) {
			rm.setFlag(false);
			e.printStackTrace();
		}

		return rm;
	}

	private String getIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("X-Forwarded-For");
		if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			int index = ip.indexOf(",");
			if (index != -1) {
				return ip.substring(0, index);
			} else {
				return ip;
			}
		}
		ip = request.getHeader("X-Real-IP");
		if (StringUtils.isNotEmpty(ip) && !"unKnown".equalsIgnoreCase(ip)) {
			return ip;
		}
		return request.getRemoteAddr();
	}
}
