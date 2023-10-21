package com.example.projecthope.service;

import com.example.projecthope.dto.Bind;
import com.example.projecthope.dto.QueryReq;
import com.example.projecthope.entity.ProjectHope;
import com.example.projecthope.processor.ExcelProcessor;
import com.example.projecthope.repository.ProjectHopeRepository;
import com.example.projecthope.util.Container;
import com.example.projecthope.vo.UploadDetail;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.file.PathUtils;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PreDestroy;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.time.Instant;
import java.util.List;

@Service
@Slf4j
public class HopeService {

    private final static Path TEMP_DIR;
    private final static int EXPORT_MAX_SIZE = 300;
    private static ExampleMatcher MATCHER = ExampleMatcher.matching();


    static {
        for (Bind bind : ExcelProcessor.BINDS) {
            MATCHER = MATCHER.withIgnoreCase(bind.getField().getName()).withMatcher(bind.getField().getName(), ExampleMatcher.GenericPropertyMatcher::contains);
        }
        try {
            TEMP_DIR = Files.createDirectories(Paths.get(System.getProperty("java.io.tmpdir") + "/hopeProject"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Autowired
    private ProjectHopeRepository repository;


    public UploadDetail upload(MultipartFile multipartFile) throws IOException {
        try (InputStream inputStream = multipartFile.getInputStream()) {
            Workbook wb = WorkbookFactory.create(inputStream);
            return handleSheet(multipartFile.getOriginalFilename(), wb);
        }
    }

    public Resource download(String fileName) throws MalformedURLException {
        Path path = Paths.get(TEMP_DIR.toString(), fileName);
        return new FileSystemResource(path);
    }

    private UploadDetail handleSheet(String fileName, Workbook wb) {
        Workbook errWb = null;
        UploadDetail uploadDetail = new UploadDetail();
        ExcelProcessor excelProcessor = ExcelProcessor.create(wb);
        Long bachId = Instant.now().getEpochSecond();
        for (ProjectHope projectHope : excelProcessor) {
            if (projectHope != null) {
                projectHope.setBachId(bachId);
                try {
                    ExcelProcessor.check(projectHope);
                    save(projectHope);
                    uploadDetail.addSuccess();
                } catch (Exception e) {
                    errWb = handleErr(projectHope, errWb, e.getMessage());
                    uploadDetail.addErr();
                }
            }
        }
        String tempFile = writeToTempDir(fileName, errWb);
        uploadDetail.setTempFileName(tempFile);
        return uploadDetail;
    }

    public void save(ProjectHope projectHope) {
        try {
            projectHope.setIsDelete(0);
            repository.save(projectHope);
        } catch (DataIntegrityViolationException e) {
            if (! (e.getCause() instanceof ConstraintViolationException)) {
                throw e;
            }
            Integer id = repository.findIdByIdentity(projectHope.getIdentity());
            projectHope.setId(id);
            repository.save(projectHope);
        }
    }

    private Workbook handleErr(ProjectHope projectHope, Workbook wb, String errMsg) {
        try {
            if (wb == null) {
                InputStream standardXlsx = HopeService.class.getResourceAsStream("/excel/err_standard.xlsx");
                wb = WorkbookFactory.create(standardXlsx);
            }
            ExcelProcessor excelProcessor = ExcelProcessor.create(wb);
            excelProcessor.writeErr(projectHope, errMsg);
        } catch (Exception e) {
            log.error("HopeService[handleErr] occurred error projectHope:{}", projectHope, e);
        }
        return wb;
    }


    private String writeToTempDir(String fileName, Workbook workbook) {
        try {
            if (workbook != null) {
                String[] split = fileName.split("\\.");
                Path tempFile = Files.createTempFile(TEMP_DIR, split[0] + "-", ".xlsx");
                try (OutputStream outputStream = Files.newOutputStream(tempFile); Workbook wb = workbook) {
                    wb.write(outputStream);
                }
                return tempFile.getFileName().toString();
            }
        } catch (Exception e) {
            log.error("HopeService[writeToTempDir] occurred error fileName:{}", fileName);
        }
        return null;
    }

    @PreDestroy
    public void destroy() {
        try {
            PathUtils.deleteDirectory(TEMP_DIR);
            log.info("清理临时文件夹:{}", TEMP_DIR);
        } catch (Exception e) {
            log.error("删除临时文件失败：", e);
        }
    }

    public void delete(List<Integer> ids) {
        repository.updateIsDeleteById(ids, 1);
    }

    public ProjectHope queryById(Integer id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("记录不存在!"));
    }

    public void update(ProjectHope projectHope) {
        this.save(projectHope);
    }

    public Page<ProjectHope> query(QueryReq queryReq) {
        PageRequest pageRequest = PageRequest.of(queryReq.getPageNum() - 1, queryReq.getPageSize()).withSort(Sort.by("createdAt").descending());
        ProjectHope projectHope = queryReq.getProjectHope() == null ? new ProjectHope() : queryReq.getProjectHope();
        projectHope.setIsDelete(0);
        return repository.findAll(Example.of(projectHope, MATCHER), pageRequest);
    }

    public String export(ProjectHope projectHope) throws IOException {
        int pageNum = 1;
        Page<ProjectHope> query = queryProjectHopes(projectHope, pageNum);
        if (query.getTotalElements() <= 0) {
            throw new RemoteException("查询记录为空");
        }
        InputStream standardXlsx = HopeService.class.getResourceAsStream("/excel/standard.xlsx");
        XSSFWorkbook workbook = (XSSFWorkbook) WorkbookFactory.create(standardXlsx);
        ExcelProcessor excelProcessor = ExcelProcessor.createSXSSF(workbook, EXPORT_MAX_SIZE);
        int startRowNum = workbook.getSheetAt(0).getLastRowNum();
        Container container = new Container(startRowNum);
        boolean countinue;
        do {
            query.stream().forEach(excelProcessor::write);
            countinue = ++pageNum <= query.getTotalPages();
            if (countinue) {
                query = queryProjectHopes(projectHope, pageNum);
            }
        } while (countinue);
        String tempFile = writeToTempDir("金寨县希望工程受助学生统计.xlsx", excelProcessor.sxssfWorkbook);
        excelProcessor.sxssfWorkbook.dispose();
        return tempFile;
    }


    private Page<ProjectHope> queryProjectHopes(ProjectHope projectHope, int pageNum) {
        QueryReq queryReq = new QueryReq(projectHope);
        queryReq.setPageNum(pageNum);
        queryReq.setPageSize(EXPORT_MAX_SIZE);
        return query(queryReq);
    }
}
