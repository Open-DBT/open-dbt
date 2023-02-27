package com.highgo.opendbt.common.Interval;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.highgo.opendbt.common.annotation.IntervalFormatAnnotation;
import com.highgo.opendbt.common.annotation.IntervalTypeEnum;
import lombok.SneakyThrows;
import org.apache.commons.lang3.StringUtils;
import org.postgresql.util.PGInterval;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.Objects;

/**
 * @Description: Interval类型格式化
 * @Title: IntervalSerializer
 * @Package com.highgo.opendbt.annotionProcess
 * @Author:
 * @Copyright 版权归HIGHGO企业所有
 * @CreateTime: 2022/10/31 9:46
 */
public class IntervalSerializer extends JsonSerializer<String> implements ContextualSerializer {
    Logger logger = LoggerFactory.getLogger(getClass());
    private IntervalTypeEnum typeEnum;

    @SneakyThrows
    @Override
    public void serialize(String s, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) {
        jsonGenerator.writeString(parse(s));
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider serializerProvider, BeanProperty beanProperty) throws JsonMappingException {
        if (beanProperty != null) {
            if (Objects.equals(beanProperty.getType().getRawClass(), String.class)) {
                IntervalFormatAnnotation intervalFormatAnnotation = beanProperty.getAnnotation((IntervalFormatAnnotation.class));
                if (intervalFormatAnnotation == null) {
                    intervalFormatAnnotation = beanProperty.getContextAnnotation(IntervalFormatAnnotation.class);
                }
                IntervalSerializer intervalSerializer = new IntervalSerializer();
                if (intervalSerializer != null) {
                    intervalSerializer.typeEnum = intervalFormatAnnotation.type();
                }
                return intervalSerializer;
            }
            return serializerProvider.findValueSerializer(beanProperty.getType(), beanProperty);
        }
        return serializerProvider.findNullValueSerializer(beanProperty);
    }

    private String parse(String s) throws SQLException {
        logger.info("进入格式化属性~");
        PGInterval pgInterval = new PGInterval(s);
        if (StringUtils.isNotBlank(s)) {
            switch (typeEnum) {
                case YEAR_MONTH_DAY_HOUR_MINUTER:
                    s = String.format("%s%s%s%s%s", pgInterval.getYears() <= 0 ? "" : pgInterval.getYears() + "年", pgInterval.getMonths() <= 0 ? "" : pgInterval.getMonths() + "月", pgInterval.getDays() <= 0 ? "" : pgInterval.getDays() + "天", pgInterval.getHours() <= 0 ? 0 : pgInterval.getHours() + "小时", pgInterval.getMinutes() <= 0 ? "" : pgInterval.getMinutes() + "分钟");
                    break;
                default:
            }
        }
        return s;

    }

}
