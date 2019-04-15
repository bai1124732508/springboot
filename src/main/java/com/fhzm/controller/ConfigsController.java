package com.fhzm.controller;

import com.fhzm.common.JsonUtils;
import com.fhzm.common.ReturnResult;
import com.fhzm.common.file.Term;
import com.fhzm.common.file.TermManager;
import com.fhzm.config.WebSocketServer;
import com.fhzm.entity.generator.Attach;
import com.fhzm.entity.generator.AuthMember;
import com.fhzm.entity.generator.BaseQueryVo;
import com.fhzm.entity.generator.Configs;
import com.fhzm.service.BaseService;
import com.fhzm.service.ConfigsService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@Slf4j
@Api(tags = "系统配置控制器",description="系统配置控制器",hidden=true)
@RequestMapping("configs")
public class ConfigsController {

	@Autowired
	private ConfigsService configsService;
	@Autowired
	private BaseService baseService;
	@Value("${file.url}")
	private String fileUrl;//虚拟路径



	/**
	 * 系统配置
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@GetMapping("index")
	public String index(Model model) throws Exception{
		Map<String, String> search = new HashMap<>();
		List<Configs> configsList = configsService.getConfigsListBySearch(search);
		for(Configs conf : configsList){
			if(conf.getType() == 2){
				List<Attach> list = new ArrayList<>();
				if(StringUtils.isNotEmpty(conf.getValue())){
					Attach attach = (Attach) baseService.getObjById(conf.getValue(),Attach.class);
					list.add(attach);
				}
				conf.setAttachList(list);
			}
		}
		String value = configsService.getValueById(100);
		List<Map<String,Object>> mapList = new ArrayList<>();
		List<String> list = Arrays.asList(value.split(","));
		for(String val : list){
			Map<String,Object> map = new HashMap<>();
			String[] split2 =  val.split(":");
			map.put("groupId",Integer.parseInt(split2[0]));
			map.put("value",split2[1]);
			mapList.add(map);

		}
		model.addAttribute("configsList", configsList);
		model.addAttribute("mapList", mapList);
		return "configs/index";
	}


	/**
	 * 更新系统配置
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@PostMapping("index")
	@ResponseBody
	public ReturnResult index(BaseQueryVo vo,String logo) throws Exception{
		try {
			List<Configs> list = new ArrayList<>();
			Map<String, String> search = vo.getSearch();
			Iterator iter = search.entrySet().iterator();

			Map <String,String > map = new HashMap<String,String >();


			while (iter.hasNext()) {
				Map.Entry entry = (Map.Entry) iter.next();
				Object key = entry.getKey();
				Object val = entry.getValue();
				log.debug(key+":"+val);
				Configs configs = new Configs();
				configs.setId(Integer.parseInt(String.valueOf(key)));
				configs.setValue(val.toString());
				configs.setUtime(new Date());
				list.add(configs);
				if("1".equals(String.valueOf(key))){ //更改了平台名称
					map.put("platformName",val.toString());
				}

			}
			if(StringUtils.isNotEmpty(logo)){
				Configs configs = new Configs();
				configs.setId(2);
				configs.setValue(logo);
				configs.setUtime(new Date());
				list.add(configs);
				/**
				 * 发送 WebSocket 更改了 logo  更新index 的logo
				 */
				Attach attach = (Attach) baseService.getObjById(logo,Attach.class);
				String logoPath= fileUrl+attach.getSavePath()+attach.getSaveName();
				map.put("logoPath",logoPath);
			}
			map.put("type","1");//修改基本信息 更新logo 和平台名称
			WebSocketServer.sendAll(JsonUtils.toJSONString(map));
			configsService.updateConfigs(list);
			return new ReturnResult("1", "操作成功", 3, "/configs/index");
		} catch (Exception e) {
			e.printStackTrace();
			return ReturnResult.error("操作失败");
		}
	}


}
