package org.example.demo.api;

import org.example.demo.common.util.ResponseUtil;
import org.example.demo.domain.emuns.ResponseEnum;
import org.example.demo.domain.entity.Knowledge;
import org.example.demo.domain.vo.ResponseVO;
import org.example.demo.exception.CustomException;
import org.example.demo.service.IKnowledgeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/knowledge")
public class KnowledgeController {

    @Autowired
    private IKnowledgeService knowledgeService;

    @PostMapping("/add")
    public ResponseVO<Boolean> addKnowledge(@RequestBody Knowledge knowledge) {
        boolean result = knowledgeService.save(knowledge);
        return ResponseUtil.success(result);
    }

    @GetMapping("/list")
    public ResponseVO<List<Knowledge>> listKnowledge() {
        List<Knowledge> list = knowledgeService.list();
        return ResponseUtil.success(list);
    }

    @PutMapping("/update")
    public ResponseVO<Boolean> updateKnowledge(@RequestBody Knowledge knowledge) {
        boolean result = knowledgeService.updateById(knowledge);
        return ResponseUtil.success(result);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseVO<Boolean> deleteKnowledge(@PathVariable Long id) {
        boolean result = knowledgeService.removeById(id);
        return ResponseUtil.success(result);
    }

    @PostMapping("/import")
    public ResponseVO<Boolean> importKnowledge(@RequestParam("file") MultipartFile file) {
        try {
            String json = new String(file.getBytes(), java.nio.charset.StandardCharsets.UTF_8);
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            List<Knowledge> knowledgeList = mapper.readValue(json,
                    mapper.getTypeFactory().constructCollectionType(List.class, Knowledge.class));
            boolean result = knowledgeService.saveBatch(knowledgeList);
            return ResponseUtil.success(result);
        } catch (Exception e) {
            throw new CustomException(ResponseEnum.BAD_REQUEST, e.getMessage());
        }
    }
}
