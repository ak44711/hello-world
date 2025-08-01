package org.example.demo.service.serviceImpl;



import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


import org.example.demo.domain.entity.ReferenceData;
import org.example.demo.mapper.ReferenceDataMapper;
import org.example.demo.service.ReferenceDataService;
import org.springframework.stereotype.Service;

@Service
public class ReferenceDataServiceImpl extends ServiceImpl<ReferenceDataMapper, ReferenceData> implements ReferenceDataService {
}