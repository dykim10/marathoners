package org.project.marathoners.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.project.marathoners.entity.User;

@Mapper
public interface UserMapper {
	@Select("SELECT * FROM users WHERE username = #{username}")
	User findByUsername(String username);
	
}
