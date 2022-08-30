package com.peggy.reggies.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.peggy.reggies.entity.AddressBook;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface AddressBookMapper extends BaseMapper<AddressBook> {
}
