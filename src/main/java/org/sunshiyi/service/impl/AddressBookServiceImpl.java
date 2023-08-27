package org.sunshiyi.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.sunshiyi.entity.AddressBook;
import org.sunshiyi.mapper.AddressBookMapper;
import org.sunshiyi.service.AddressBookService;
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook> implements AddressBookService {
}
