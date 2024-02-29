package ws.mybatis.spring.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ws.mybatis.spring.annotation.EnablePage;
import ws.mybatis.spring.annotation.Padding;
import ws.mybatis.spring.annotation.PageNumber;
import ws.mybatis.spring.annotation.PageSize;
import ws.mybatis.spring.mapper.PersonMapper;
import ws.mybatis.spring.pojo.Person;
import ws.spring.web.rest.response.PageData;

import java.util.List;

/**
 * @author WindShadow
 * @version 2021-12-27.
 */

@Slf4j
@EnablePage
@Validated
@RestController
@RequestMapping(value = "/person", produces = MediaType.APPLICATION_JSON_VALUE)
public class PageController {

    @Autowired
    private PersonMapper personMapper;

    @GetMapping("/normal")
    public List<Person> queryPersonNormal(@PageNumber Integer pageNum, @PageSize Integer pageSize) {

        log.info("pageNum: {}, pageSize: {}", pageNum, pageSize);
        return personMapper.selectAll();
    }

    @EnablePage(pageNumberParam = "pageNum", pageSizeParam = "pageSize")
    @GetMapping("/specify-param")
    public List<Person> queryPersonSpecifyParam(@PageNumber Integer pageNum, @PageSize Integer pageSize) {

        log.info("pageNum: {}, pageSize: {}", pageNum, pageSize);
        return personMapper.selectAll();
    }

    @EnablePage(enable = false)
    @GetMapping("/close")
    public List<Person> queryPersonClose(@PageNumber Integer pageNum, @PageSize Integer pageSize) {

        log.info("pageNum: {}, pageSize: {}", pageNum, pageSize);
        return personMapper.selectAll();
    }

    @GetMapping("/string-param")
    public List<Person> queryPersonWithDiffParamType(@PageNumber String pageNum, @PageSize String pageSize) {

        log.info("pageNum: {}, pageSize: {}", pageNum, pageSize);
        return personMapper.selectAll();
    }

    @EnablePage(paddings = {@Padding(pageNumberProperty = "pageNum", pageSizeProperty = "pageSize", totalProperty = "total")})
    @GetMapping("/padding/page-data")
    public PageData<Person> queryPersonNormalToPageData() {

        return PageData.of(personMapper.selectAll());
    }

    @EnablePage(paddings = {@Padding(type = Person.class, pageNumberProperty = "pageNum", pageSizeProperty = "pageSize", totalProperty = "total")})
    @GetMapping("/padding/page-data/miss")
    public PageData<Person> queryPersonNormalToMiss() {

        return PageData.of(personMapper.selectAll());
    }
}
