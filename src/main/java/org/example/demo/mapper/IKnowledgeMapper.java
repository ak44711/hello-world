package org.example.demo.mapper;



import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.demo.domain.entity.Knowledge;

@Mapper
public interface IKnowledgeMapper extends BaseMapper<Knowledge> {
}
