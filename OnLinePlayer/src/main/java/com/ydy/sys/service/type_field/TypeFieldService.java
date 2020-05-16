package com.ydy.sys.service.type_field;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ydy.sys.dao.type_field.ITypeFieldDAO;
import com.ydy.sys.entity.TypeField;

@Service("TypeFieldService")
public class TypeFieldService {
	@Autowired
	private ITypeFieldDAO iTypeFieldDAO;
	
	/**
	 * 根据分类信息主键，查询该分类下的字段配置
	 * @param typeId 分类信息主键
	 * @return
	 */
	public List<TypeField> listByTypeId(String typeId) {
		return iTypeFieldDAO.listByTypeId(typeId);
	}
	/**
	 * 检索指定字段是否必填
	 * @param typeId 分类信息主键
	 * @param varName 字段变量名
	 * @return
	 */
	public String selectIsRequired(String typeId, String varName) {
		return iTypeFieldDAO.selectIsRequired(typeId, varName);
	}
	
}
