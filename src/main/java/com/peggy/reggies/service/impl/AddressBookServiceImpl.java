package com.peggy.reggies.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.peggy.reggies.entity.AddressBook;
import com.peggy.reggies.mapper.AddressBookMapper;
import com.peggy.reggies.service.AddressBookService;
import org.springframework.stereotype.Service;

@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper,AddressBook> implements AddressBookService{
    
}