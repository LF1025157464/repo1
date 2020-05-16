package com.ydy.sys.controller.type_field;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.ydy.sys.entity.TypeField;
import com.ydy.sys.service.type_field.TypeFieldService;

@Controller
@RequestMapping("type_field")
public class TypeFieldAction {

	@Autowired
	private TypeFieldService typeFieldService;
	/**
	 * 根据分类信息主键，查询该分类下的字段配置
	 * @param map
	 * @param typeId 分类信息主键
	 * @param typeName 分类信息名称
	 * @return
	 */
	@RequestMapping("list.action")
	public String list(ModelMap map,
			@RequestParam(value="typeId") String typeId,
			@RequestParam(value="typeName") String typeName) {
		
		// 查询已配置的字段
		List<TypeField> list = typeFieldService.listByTypeId(typeId);
		map.put("list", list);
		
		map.put("typeId", typeId);
		map.put("typeName", typeName);
		return "admin/type_field/list";
	}
}
