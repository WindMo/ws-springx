package ws.mybatis.spring.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.validation.annotation.Validated;
import ws.mybatis.spring.pojo.Person;

import java.util.List;

@Validated
@Mapper
public interface PersonMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Person record);

    int insertSelective(Person record);

    Person selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Person record);

    int updateByPrimaryKey(Person record);

    List<Person> selectAll();
}
