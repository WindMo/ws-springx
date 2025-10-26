/*
 * SPDX-License-Identifier: MIT
 * Copyright (c) 2025 莫千风
 */

package ws.spring.mybatisflex;

import com.mybatisflex.core.BaseMapper;
import com.mybatisflex.core.exception.FlexAssert;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.util.MapperUtil;
import org.apache.ibatis.exceptions.TooManyResultsException;

import java.io.Serializable;
import java.util.Arrays;

/**
 * <p>更符合mybatis设计理念的{@linkplain BaseMapper BaseMapper}
 * <p>
 * 主要修改如下：
 *     <ul>
 *          <li>selectOne系列方法不再强制设置{@linkplain QueryWrapper#limit(Number) limit}为1，当查询结果不唯一时抛出{@link TooManyResultsException}异常</li>
 *          <li>批量操作支持动态个数参数</li>
 *     </ul>
 * </p>
 *
 * @author WindShadow
 * @version 2025-07-13.
 */
public interface GenericBaseMapper<T> extends BaseMapper<T> {

    @Override
    default T selectOneByQuery(QueryWrapper queryWrapper) {
        return MapperUtil.getSelectOneResult(selectListByQuery(queryWrapper));
    }

    @Override
    default <R> R selectOneByQueryAs(QueryWrapper queryWrapper, Class<R> asType) {
        return MapperUtil.getSelectOneResult(selectListByQueryAs(queryWrapper, asType));
    }

    @Override
    default T selectOneWithRelationsByQuery(QueryWrapper queryWrapper) {
        return MapperUtil.queryRelations(this, MapperUtil.getSelectOneResult(selectListByQuery(queryWrapper)));
    }

    default <R> R selectOneWithRelationsByQueryAs(QueryWrapper queryWrapper, Class<R> asType) {
        return MapperUtil.queryRelations(this, MapperUtil.getSelectOneResult(selectListByQueryAs(queryWrapper, asType)));
    }

    default int insertBatch(T... entities) {

        FlexAssert.notNull(entities, "The entities must not be null");
        return entities.length == 1 ? insert(entities[0]) : insertBatch(Arrays.asList(entities));
    }

    default int deleteBatchByIds(Serializable... ids) {

        FlexAssert.notNull(ids, "The ids must not be null");
        return ids.length == 1 ? deleteById(ids[0]) : deleteBatchByIds(Arrays.asList(ids));
    }
}
