package ws.mybatis.spring.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ws.mybatis.spring.WsMybatisSpringExtendApplicationTests;
import ws.mybatis.spring.mapper.PersonMapper;
import ws.mybatis.spring.pojo.Person;
import ws.spring.web.rest.response.PageData;

import java.util.List;
import java.util.function.Consumer;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author WindShadow
 * @version 2021-12-27.
 * @see PageController
 */

@Slf4j
@AutoConfigureMockMvc
public class PageControllerTests extends WsMybatisSpringExtendApplicationTests {

    @Autowired
    private PersonMapper personMapper;

    private static String toJsonString(Object o) {

        try {
            return new ObjectMapper().writeValueAsString(o);
        } catch (JsonProcessingException e) {
            log.error("toJsonString failed", e);
            throw new UnsupportedOperationException(e);
        }
    }

    private List<Person> selectPerson(int pageNum, int pageSize) {

        return selectPerson(pageNum, pageSize, null);
    }

    private List<Person> selectPerson(int pageNum, int pageSize, Consumer<Long> consumeTotal) {

        Page<?> page = PageHelper.startPage(pageNum, pageSize);
        List<Person> people = personMapper.selectAll();
        if (consumeTotal != null) {
            consumeTotal.accept(page.getTotal());
        }
        return people;
    }

    @Test
    public void queryPersonNormalTest(@Autowired MockMvc mvc) throws Exception {

        int pageNum = 1;
        int pageSize = 3;

        List<Person> expectedResult = selectPerson(pageNum, pageSize);
        String expectedResultJson = toJsonString(expectedResult);
        Assertions.assertNotNull(expectedResultJson);

        mvc.perform(get("/person/normal?page_num={page_num}&page_size={page_size}", pageNum, pageSize).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(expectedResultJson));
    }

    @Test
    public void queryPersonSpecifyParamTest(@Autowired MockMvc mvc) throws Exception {

        int pageNum = 1;
        int pageSize = 3;

        List<Person> expectedResult = selectPerson(pageNum, pageSize);
        String expectedResultJson = toJsonString(expectedResult);
        mvc.perform(get("/person/specify-param?pageNum={pageNum}&pageSize={pageSize}", pageNum, pageSize).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(expectedResultJson));
    }

    @Test
    public void queryPersonCloseTest(@Autowired MockMvc mvc) throws Exception {

        int pageNum = 1;
        int pageSize = 3;

        List<Person> expectedResult = personMapper.selectAll();
        String expectedResultJson = toJsonString(expectedResult);
        mvc.perform(get("/person/close?page_num={page_num}&page_size={page_size}", pageNum, pageSize).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(expectedResultJson));
    }

    @Test
    @Disabled
    public void queryPersonWithDiffParamType(@Autowired MockMvc mvc) throws Exception {

        int pageNum = 1;
        int pageSize = 3;

        List<Person> expectedResult = selectPerson(pageNum, pageSize);
        String expectedResultJson = toJsonString(expectedResult);
        mvc.perform(get("/person/string-param?page_num={page_num}&page_size={page_size}", pageNum, pageSize).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(expectedResultJson));

    }

    @Test
    public void queryPersonNormalToPageDataTest(@Autowired MockMvc mvc) throws Exception {

        int pageNum = 1;
        int pageSize = 3;
        PageData<Person> expectedResult = new PageData<>();
        List<Person> people = selectPerson(pageNum, pageSize, expectedResult::setTotal);
        expectedResult.setPageNum(pageNum);
        expectedResult.setPageSize(pageSize);
        expectedResult.setElements(people);
        String expectedResultJson = toJsonString(expectedResult);
        mvc.perform(get("/person/padding/page-data?page_num={page_num}&page_size={page_size}", pageNum, pageSize).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(expectedResultJson));

    }

    @Test
    public void queryPersonNormalToMiss(@Autowired MockMvc mvc) throws Exception {

        int pageNum = 1;
        int pageSize = 3;
        PageData<Person> expectedResult = new PageData<>();
        List<Person> people = selectPerson(pageNum, pageSize, null);
        expectedResult.setPageNum(pageNum);
        expectedResult.setPageSize(pageSize);
        expectedResult.setElements(people);
        expectedResult.setTotal(people.size());
        String expectedResultJson = toJsonString(expectedResult);
        mvc.perform(get("/person/padding/page-data/miss?page_num={page_num}&page_size={page_size}", pageNum, pageSize).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(expectedResultJson));

    }
}
