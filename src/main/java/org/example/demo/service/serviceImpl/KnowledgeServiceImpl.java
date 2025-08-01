package org.example.demo.service.serviceImpl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import org.example.demo.domain.entity.Knowledge;
import org.example.demo.mapper.IKnowledgeMapper;
import org.example.demo.service.IKnowledgeService;
import org.springframework.stereotype.Service;

@Service
public class KnowledgeServiceImpl extends ServiceImpl<IKnowledgeMapper, Knowledge> implements IKnowledgeService {
    // 可扩展自定义方法
}