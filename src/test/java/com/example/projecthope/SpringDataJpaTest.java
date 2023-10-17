package com.example.projecthope;

import com.example.projecthope.entity.ProjectHope;
import com.example.projecthope.repository.ProjectHopeRepository;
import com.example.projecthope.service.HopeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Optional;

@SpringBootTest
class SpringDataJpaTest {

    @Autowired
    private ProjectHopeRepository repository;
    @Autowired
    private HopeService hopeService;

    @Test
    void test() throws IOException {
        Optional<ProjectHope> byId = repository.findById(1);
        ProjectHope projectHope = byId.get();
        projectHope.setId(null);
        projectHope.setName("哈哈哈哈");
        projectHope.setIsDelete(null);
        hopeService.save(projectHope);
    }
}
