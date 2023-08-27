package org.sunshiyi.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.sunshiyi.common.R;
import org.sunshiyi.entity.AddressBook;
import org.sunshiyi.service.AddressBookService;
import org.sunshiyi.utils.BaseContext;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/addressBook")
@Slf4j
public class AddressBookController {
    @Resource
    private AddressBookService addressBookService;

    @GetMapping("/list")
    public R<List<AddressBook>> getAddressBooks() {
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, userId);
        List<AddressBook> list = addressBookService.list(queryWrapper);
        return R.success(list);
    }

    @PostMapping
    public R<String> addAddressBook(@RequestBody AddressBook addressBook) {
        addressBook.setUserId(BaseContext.getCurrentId());
        addressBookService.save(addressBook);
        return R.success("添加成功！");
    }

    @PutMapping("/default")
    @Transactional
    public R<String> setDefault(@RequestBody AddressBook addressBook) {
        Long id = BaseContext.getCurrentId();
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getIsDefault, 1)
                .eq(AddressBook::getUserId, id);
        AddressBook defaultAddressBook = addressBookService.getOne(queryWrapper);
//        如果为空，代表还没有默认地址，否则将之前的设置为非默认地址
        if (defaultAddressBook != null) {
            defaultAddressBook.setIsDefault(0);
            addressBookService.updateById(defaultAddressBook);
        }

        addressBook.setIsDefault(1);
        addressBookService.updateById(addressBook);
        return R.success("修改成功！");
    }

    @GetMapping("/{id}")
    public R<AddressBook> getAddressBook(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        return R.success(addressBook);
    }

    @PutMapping
    public R<String> updateAddressBook(@RequestBody AddressBook addressBook) {
        addressBookService.updateById(addressBook);
        return R.success("更新成功！");
    }

    @GetMapping("/default")
    public R<AddressBook> getDefaultAddressBook() {
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getUserId, userId);
        queryWrapper.eq(AddressBook::getIsDefault, 1);
        AddressBook addressBook = addressBookService.getOne(queryWrapper);
        return R.success(addressBook);

    }
}
