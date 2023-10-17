package com.example.projecthope.controller;

import com.example.projecthope.dto.PageResult;
import com.example.projecthope.dto.QueryReq;
import com.example.projecthope.dto.Result;
import com.example.projecthope.entity.ProjectHope;
import com.example.projecthope.service.HopeService;
import com.example.projecthope.vo.UploadDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class HopeController {

    @Autowired
    private HopeService hopeService;


    @PostMapping("/upload")
    @ResponseBody
    public Result<UploadDetail> upload(@RequestPart("file")  MultipartFile multipartFile) throws IOException {
        return Result.successData(hopeService.upload(multipartFile));
    }

    @GetMapping("/download/{fileName:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String fileName) throws MalformedURLException, UnsupportedEncodingException {
        if (fileName.equals("standard-template")) {
            fileName = "金寨县希望工程受助学生信息统计.xlsx";
            String replace = URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
            String contentHeader = "attachment; filename*=UTF-8''" + replace;
            ClassPathResource classPathResource = new ClassPathResource("excel/standard.xlsx");
            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType("application/octet-stream;charset=UTF-8"))
                    .header(HttpHeaders.CONTENT_DISPOSITION, contentHeader)
                    .body(classPathResource);
        }
        String replace = URLEncoder.encode(fileName, "UTF-8").replace("+", "%20");
        String contentHeader = "attachment; filename*=UTF-8''" + replace;
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream;charset=UTF-8"))
                .header(HttpHeaders.CONTENT_DISPOSITION, contentHeader)
                .body(hopeService.download(fileName));
    }

    @PostMapping("/export")
    @ResponseBody
    public Result<String> export(@RequestBody ProjectHope projectHope) throws IOException {
        return Result.successData(hopeService.export(projectHope));
    }

    @GetMapping("/test")
    @ResponseBody
    public Result<String> test() {
        boolean countinue;
        do {
            countinue = true;
            if (countinue) {
                System.out.println(countinue);
            }
        } while (countinue);
        return Result.successData("ok");
    }


    @PostMapping("/delete")
    @ResponseBody
    public Result<Void> delete(@RequestBody List<Integer> ids) {
        hopeService.delete(ids);
        return Result.successNullData();
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Integer id, Model model) {
        try {
            ProjectHope projectHope = hopeService.queryById(id);
            model.addAttribute("projectHope", projectHope);
        } catch (Exception e) {
            return "err";
        }
        return "/edit";
    }

    @PostMapping("/update")
    @ResponseBody
    public Result<Void> update(@RequestBody ProjectHope projectHope) {
        hopeService.update(projectHope);
        return Result.successNullData();
    }


    @PostMapping("/query")
    @ResponseBody
    public PageResult<ProjectHope> query(@RequestBody QueryReq queryReq) {
        Page<ProjectHope> projectHopes = hopeService.query(queryReq);
        return PageResult.successData(projectHopes.get().collect(Collectors.toList()), projectHopes.getTotalElements());
    }

    @GetMapping("/stop")
    @ResponseBody
    public Result<String> stop() {
        System.exit(0);
        return Result.successData("关闭成功!");
    }
}
