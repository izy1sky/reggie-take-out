package org.sunshiyi.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.sunshiyi.common.R;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

@RestController
@Slf4j
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.images.base-path}")
    private String basePath;

    @PostMapping("/upload")
    public R<String> upload(MultipartFile file) {
        String filename = file.getOriginalFilename();
        assert filename != null;
        String suffix = filename.substring(filename.lastIndexOf("."));
        filename = UUID.randomUUID() + suffix;
        File file1 = new File(basePath);
        if (!file1.exists()) {
            file1.mkdir();
        }
        try {
            file.transferTo(new File(basePath + filename));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(filename);
    }

    @GetMapping("/download")
    public void download(@RequestParam(name = "name") String filename, HttpServletResponse response) {
        filename = basePath + filename;
        try(FileInputStream fileInputStream = new FileInputStream(new File(filename));
            ServletOutputStream outputStream = response.getOutputStream()
        ) {
            response.setContentType("image/jpeg");
            byte[] bytes = new byte[1024];
            int len = 0;
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
