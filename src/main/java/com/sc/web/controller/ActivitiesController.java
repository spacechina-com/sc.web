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
import com.sc.api.response.ReturnModel;
import com.sc.api.util.DateUtil;
import com.sc.web.config.FileConfig;
import com.sc.web.util.RestTemplateUtil;

@Controller
@RequestMapping("activities")
public class ActivitiesController extends BaseController {

	private Logger logger = LoggerFactory.getLogger(ActivitiesController.class);

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

				pd.put("BACK_PATH", tempName);
			}
		}

		String[] ACTIVITIES_PRIZEITEMS = pd.getString("PRIZEITEMS_ID_PERCENT").split(",");
		String BATCH_ID = pd.getString("BATCH_ID");

		pd = rest.post(IConstants.SC_SERVICE_KEY, "activities/save", pd, Pd.class);

		for (int i = 0; i < ACTIVITIES_PRIZEITEMS.length; i++) {
			String[] one = ACTIVITIES_PRIZEITEMS[i].split("_");
			Pd pdpi = new Pd();
			pdpi.put("ACTIVITIES_ID", pd.getString("ACTIVITIES_ID"));
			pdpi.put("PRIZEITEMS_ID", one[0]);
			pdpi.put("PERCENT", one[1]);
			rest.post(IConstants.SC_SERVICE_KEY, "activities/savePrizeitems", pdpi, Pd.class);
		}

		if (StringUtils.isNotEmpty(BATCH_ID)) {
			String[] BATCH_IDS = BATCH_ID.split(",");
			for (String batcId : BATCH_IDS) {
				Pd pdgb = new Pd();
				pdgb.put("ACTIVITIES_ID", pd.getString("ACTIVITIES_ID"));
				pdgb.put("BATCH_ID", batcId);
				rest.post(IConstants.SC_SERVICE_KEY, "activities/saveBatchs", pdgb, Pd.class);
			}
		}

		mv.addObject("msg", getMessageUrl("MSG_CODE_ADD_SUCCESS", new Object[] { "抽奖活动" }, ""));
		mv.setViewName("redirect:/activities/listPage");
		logger.info("新增抽奖活动成功");
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
	public ModelAndView delete(@RequestParam String ACTIVITIES_ID) throws Exception {
		ModelAndView mv = this.getModelAndView();
		Pd pd = new Pd();
		pd.put("ACTIVITIES_ID", ACTIVITIES_ID);

		rest.post(IConstants.SC_SERVICE_KEY, "activities/delete", pd, Pd.class);

		mv.addObject("msg", getMessageUrl("MSG_CODE_DELETE_SUCCESS", new Object[] { "抽奖活动" }, ""));
		mv.setViewName("redirect:/activities/listPage");
		logger.info("删除抽奖活动成功");

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

				pd.put("BACK_PATH", tempName);
			}
		}

		String ACTIVITIES_PRIZEITEM = pd.getString("PRIZEITEMS_ID_PERCENT");
		String BATCH_ID = pd.getString("BATCH_ID");

		rest.post(IConstants.SC_SERVICE_KEY, "activities/edit", pd, Pd.class);

		Pd pdap = new Pd();
		pdap.put("ACTIVITIES_ID", pd.getString("ACTIVITIES_ID"));
		List<Pd> apData = rest.postForList(IConstants.SC_SERVICE_KEY, "activities/listAllPrizeitems", pdap,
				new ParameterizedTypeReference<List<Pd>>() {
				});
		for (Pd pageData : apData) {
			rest.post(IConstants.SC_SERVICE_KEY, "activities/deletePrizeitems", pageData, Pd.class);
		}

		if (StringUtils.isNotEmpty(ACTIVITIES_PRIZEITEM)) {
			String[] ACTIVITIES_PRIZEITEMS = ACTIVITIES_PRIZEITEM.split(",");
			for (int i = 0; i < ACTIVITIES_PRIZEITEMS.length; i++) {
				String[] one = ACTIVITIES_PRIZEITEMS[i].split("_");
				Pd pdpi = new Pd();
				pdpi.put("ACTIVITIES_ID", pd.getString("ACTIVITIES_ID"));
				pdpi.put("PRIZEITEMS_ID", one[0]);
				pdpi.put("PERCENT", one[1]);
				rest.post(IConstants.SC_SERVICE_KEY, "activities/savePrizeitems", pdpi, Pd.class);
			}
		}

		Pd pdmm = new Pd();
		pdmm.put("ACTIVITIES_ID", pd.getString("ACTIVITIES_ID"));
		List<Pd> bData = rest.postForList(IConstants.SC_SERVICE_KEY, "activities/listAllBatchs", pdmm,
				new ParameterizedTypeReference<List<Pd>>() {
				});
		for (Pd pageData : bData) {
			rest.post(IConstants.SC_SERVICE_KEY, "activities/deleteBatchs", pageData, Pd.class);
		}

		if (StringUtils.isNotEmpty(BATCH_ID)) {
			String[] BATCH_IDS = BATCH_ID.split(",");
			for (String batcId : BATCH_IDS) {
				Pd pdgb = new Pd();
				pdgb.put("ACTIVITIES_ID", pd.getString("ACTIVITIES_ID"));
				pdgb.put("BATCH_ID", batcId);
				rest.post(IConstants.SC_SERVICE_KEY, "activities/saveBatchs", pdgb, Pd.class);
			}
		}

		mv.addObject("msg", getMessageUrl("MSG_CODE_EDIT_SUCCESS", new Object[] { "抽奖活动" }, ""));
		mv.setViewName("redirect:/activities/listPage");
		logger.info("修改抽奖活动成功");
		return mv;
	}

	@RequestMapping("listPage")
	public ModelAndView listPage() throws Exception {
		ModelAndView mv = this.getModelAndView();

		Pd pdt = new Pd();
		List<Pd> modalitiesData = rest.postForList(IConstants.SC_SERVICE_KEY, "modalities/listAll", pdt,
				new ParameterizedTypeReference<List<Pd>>() {
				});
		mv.addObject("modalitiesData", modalitiesData);

		Pd pd = new Pd();
		pd = this.getPd();
		String keywords = pd.getString("keywords"); // 检索条件
		if (null != keywords && !"".equals(keywords)) {
			pd.put("keywords", keywords.trim());
		}

		Pd user = (Pd) getSession().getAttribute(IConstants.USER_SESSION);

		pd.put("COMPANY_ID", user.getString("COMPANY_ID"));

		Page page = rest.post(IConstants.SC_SERVICE_KEY, "activities/listPage", pd, Page.class);

		mv.setViewName("/activities/list");
		mv.addObject("page", page);
		mv.addObject("pd", pd);
		logger.info("分页查询抽奖活动信息");
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

		Pd user = (Pd) getSession().getAttribute(IConstants.USER_SESSION);

		Pd pdt = new Pd();
		List<Pd> modalitiesData = rest.postForList(IConstants.SC_SERVICE_KEY, "modalities/listAll", pdt,
				new ParameterizedTypeReference<List<Pd>>() {
				});
		mv.addObject("modalitiesData", modalitiesData);

		Pd pdml = new Pd();
		pdml.put("COMPANY_ID", user.getString("COMPANY_ID"));
		List<Pd> goodsData = rest.postForList(IConstants.SC_SERVICE_KEY, "common/listAllGoods", pdml,
				new ParameterizedTypeReference<List<Pd>>() {
				});
		mv.addObject("goodsData", goodsData);

		Pd pdpi = new Pd();
		pdpi.put("COMPANY_ID", user.getString("COMPANY_ID"));
		List<Pd> prizeitemsData = rest.postForList(IConstants.SC_SERVICE_KEY, "prizeitems/listAll", pdpi,
				new ParameterizedTypeReference<List<Pd>>() {
				});
		mv.addObject("prizeitemsData", prizeitemsData);

		mv.addObject("pd", pd);
		mv.setViewName("activities/add");
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

		Pd pdt = new Pd();
		List<Pd> modalitiesData = rest.postForList(IConstants.SC_SERVICE_KEY, "modalities/listAll", pdt,
				new ParameterizedTypeReference<List<Pd>>() {
				});
		mv.addObject("modalitiesData", modalitiesData);

		Pd pdml = new Pd();
		pdml.put("COMPANY_ID", user.getString("COMPANY_ID"));
		List<Pd> goodsData = rest.postForList(IConstants.SC_SERVICE_KEY, "common/listAllGoods", pdml,
				new ParameterizedTypeReference<List<Pd>>() {
				});
		mv.addObject("goodsData", goodsData);

		Pd pdpi = new Pd();
		pdpi.put("COMPANY_ID", user.getString("COMPANY_ID"));
		List<Pd> prizeitemsData = rest.postForList(IConstants.SC_SERVICE_KEY, "prizeitems/listAll", pdpi,
				new ParameterizedTypeReference<List<Pd>>() {
				});
		mv.addObject("prizeitemsData", prizeitemsData);

		Pd pdap = new Pd();
		pdap.put("ACTIVITIES_ID", pd.getString("ACTIVITIES_ID"));
		List<Pd> activitiesprizeitemsData = rest.postForList(IConstants.SC_SERVICE_KEY, "activities/listAllPrizeitems",
				pdap, new ParameterizedTypeReference<List<Pd>>() {
				});
		mv.addObject("activitiesprizeitemsData", activitiesprizeitemsData);

		Pd pdmm = new Pd();
		pdmm.put("ACTIVITIES_ID", pd.getString("ACTIVITIES_ID"));
		List<Pd> bData = rest.postForList(IConstants.SC_SERVICE_KEY, "activities/listAllBatchs", pdmm,
				new ParameterizedTypeReference<List<Pd>>() {
				});
		String IDS = "";
		String NAMES = "";
		for (Pd pageData : bData) {
			if (IDS.length() > 0) {
				IDS += ",";
				NAMES += "  ,  ";
			}
			IDS += pageData.getString("BATCH_ID");
			NAMES += pageData.getString("BATCHNAME");
		}
		mv.addObject("IDS", IDS);
		mv.addObject("NAMES", NAMES);

		pd = rest.post(IConstants.SC_SERVICE_KEY, "activities/find", pd, Pd.class);
		mv.addObject("pd", pd); // 放入视图容器

		mv.setViewName("activities/edit");
		return mv;
	}

	@RequestMapping(value = "/goInfo")
	public ModelAndView goInfo() throws Exception {
		ModelAndView mv = this.getModelAndView();
		Pd pd = new Pd();
		pd = this.getPd();

		Pd user = (Pd) getSession().getAttribute(IConstants.USER_SESSION);

		Pd pdt = new Pd();
		List<Pd> modalitiesData = rest.postForList(IConstants.SC_SERVICE_KEY, "modalities/listAll", pdt,
				new ParameterizedTypeReference<List<Pd>>() {
				});
		mv.addObject("modalitiesData", modalitiesData);

		Pd pdml = new Pd();
		pdml.put("COMPANY_ID", user.getString("COMPANY_ID"));
		List<Pd> goodsData = rest.postForList(IConstants.SC_SERVICE_KEY, "common/listAllGoods", pdml,
				new ParameterizedTypeReference<List<Pd>>() {
				});
		mv.addObject("goodsData", goodsData);

		Pd pdpi = new Pd();
		pdpi.put("COMPANY_ID", user.getString("COMPANY_ID"));
		List<Pd> prizeitemsData = rest.postForList(IConstants.SC_SERVICE_KEY, "prizeitems/listAll", pdpi,
				new ParameterizedTypeReference<List<Pd>>() {
				});
		mv.addObject("prizeitemsData", prizeitemsData);

		Pd pdap = new Pd();
		pdap.put("ACTIVITIES_ID", pd.getString("ACTIVITIES_ID"));
		List<Pd> activitiesprizeitemsData = rest.postForList(IConstants.SC_SERVICE_KEY, "activities/listAllPrizeitems",
				pdap, new ParameterizedTypeReference<List<Pd>>() {
				});
		mv.addObject("activitiesprizeitemsData", activitiesprizeitemsData);

		Pd pdmm = new Pd();
		pdmm.put("ACTIVITIES_ID", pd.getString("ACTIVITIES_ID"));
		List<Pd> bData = rest.postForList(IConstants.SC_SERVICE_KEY, "activities/listAllBatchs", pdmm,
				new ParameterizedTypeReference<List<Pd>>() {
				});
		String IDS = "";
		String NAMES = "";
		for (Pd pageData : bData) {
			if (IDS.length() > 0) {
				IDS += ",";
				NAMES += "  ,  ";
			}
			IDS += pageData.getString("BATCH_ID");
			NAMES += pageData.getString("BATCHNAME");
		}
		mv.addObject("IDS", IDS);
		mv.addObject("NAMES", NAMES);

		pd = rest.post(IConstants.SC_SERVICE_KEY, "activities/find", pd, Pd.class);
		mv.addObject("pd", pd); // 放入视图容器

		mv.setViewName("activities/info");
		return mv;
	}

	@RequestMapping(value = "/findGoodsBatchs")
	@ResponseBody
	public ReturnModel findGoodsBatchs() throws Exception {
		ReturnModel rm = new ReturnModel();
		Pd pd = new Pd();
		pd = this.getPd();
		List<Pd> batchsData = rest.postForList(IConstants.SC_SERVICE_KEY, "common/listAllGoodsBatch", pd,
				new ParameterizedTypeReference<List<Pd>>() {
				});
		rm.setData(batchsData);
		return rm;
	}

	@RequestMapping(value = "/deletePrizeitems")
	@ResponseBody
	public ReturnModel deletePrizeitems() throws Exception {
		ReturnModel rm = new ReturnModel();
		Pd pd = new Pd();
		pd = this.getPd();
		rest.post(IConstants.SC_SERVICE_KEY, "activities/deletePrizeitems", pd, Pd.class);
		return rm;
	}
}
