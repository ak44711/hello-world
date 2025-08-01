package org.example.demo.api;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;


import lombok.RequiredArgsConstructor;
import org.example.demo.domain.entity.ReferenceData;
import org.example.demo.service.ReferenceDataService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reference-data")
@RequiredArgsConstructor
public class ReferenceDataController {

    private final ReferenceDataService referenceDataService;

    @PostMapping
    public boolean add(@RequestBody ReferenceData data) {
        return referenceDataService.save(data);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable Long id) {
        return referenceDataService.removeById(id);
    }

    @PutMapping
    public boolean update(@RequestBody ReferenceData data) {
        return referenceDataService.updateById(data);
    }

    @GetMapping("/{id}")
    public ReferenceData get(@PathVariable Long id) {
        return referenceDataService.getById(id);
    }

    @GetMapping
    public List<ReferenceData> list() {
        return referenceDataService.list(new QueryWrapper<>());
    }
}
