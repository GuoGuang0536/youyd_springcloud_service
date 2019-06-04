package com.youyd.user.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.youyd.pojo.user.Role;
import com.youyd.pojo.user.User;

import java.util.List;

/**
 * 角色管理
 * @author : LGG
 * @create : 2019-06-04 16:21
 **/

public interface RoleDao extends BaseMapper<Role> {


	void updateRole(List roleId);

	List<User> findUsersOfRole(String roleId);
}
