package net.wenzuo.base.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.time.format.DateTimeFormatter;
import java.util.Locale;

/**
 * Json 处理工具类
 *
 * @author Catch
 */
@Slf4j
public class JsonUtil {

    private static final ObjectMapper OBJECT_MAPPER = configBuilder(null).build();

    /**
     * 将 Java 对象转为 Json 字符串
     *
     * @param <T>    泛型
     * @param object Java 对象
     * @return json 字符串
     */
    public static <T> String toJsonString(T object) {
        try {
            return object == null ? null : OBJECT_MAPPER.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 将 Json 字符串转为 Java 对象
     *
     * @param <T>        泛型
     * @param jsonString json 字符串
     * @param type       要转换的 java 类型
     * @return 接收 java 对象
     */
    public static <T> T parseObject(String jsonString, Class<T> type) {
        try {
            return jsonString == null ? null : OBJECT_MAPPER.readValue(jsonString, type);
        } catch (JsonProcessingException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    /**
     * 对 Jackson2ObjectMapperBuilder 进行常用配置
     *
     * @param builder 当为 null 时, 将创建
     * @return Jackson2ObjectMapperBuilder
     */
    public static Jackson2ObjectMapperBuilder configBuilder(Jackson2ObjectMapperBuilder builder) {
        if (builder == null) {
            builder = new Jackson2ObjectMapperBuilder();
        }
        // 配置国际化
        builder.locale(Locale.SIMPLIFIED_CHINESE);
        // 配置时区
        builder.timeZone("GMT+8");

        String dateFormat = "yyyy-MM-dd";
        String timeFormat = "HH:mm:ss";
        String datetimeFormat = "yyyy-MM-dd HH:mm:ss";

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(dateFormat);
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern(timeFormat);
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern(datetimeFormat);

        // ==================== 序列化 ====================
        // 序列化时的命名策略 SNAKE_CASE
        builder.propertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

        //若对象的属性值为null，序列化时不进行显示
        builder.serializationInclusion(JsonInclude.Include.NON_NULL);

        // 针对于 Date 及其子类的序列化
        builder.simpleDateFormat(datetimeFormat);

        // 针对于JDK新时间类。序列化时带有 T和毫秒数 的问题，自定义格式化字符串
        LocalDateSerializer localDateSerializer = new LocalDateSerializer(dateFormatter);
        LocalTimeSerializer localTimeSerializer = new LocalTimeSerializer(timeFormatter);
        LocalDateTimeSerializer localDateTimeSerializer = new LocalDateTimeSerializer(dateTimeFormatter);
        builder.serializers(localDateSerializer, localTimeSerializer, localDateTimeSerializer);

        // 默认关闭，即会把char[]数组序列化为String类型。开启后序列化为JSON数组。
        builder.featuresToEnable(SerializationFeature.WRITE_CHAR_ARRAYS_AS_JSON_ARRAYS);

        // 默认开启，将Date类型序列化为数字时间戳(毫秒表示)。关闭后，序列化为文本表现形式(2019-10-23T01:58:58.308+0000)
        // 若设置时间格式化。那么按格式化的时间输出。
        builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // 默认开启：如果一个类没有public的方法或属性时，会导致序列化失败。关闭后，会得到一个空JSON串。
        builder.featuresToDisable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        // 默认关闭，即以文本(ISO-8601)作为Key，开启后，以时间戳作为Key
        builder.featuresToEnable(SerializationFeature.WRITE_DATE_KEYS_AS_TIMESTAMPS);


        // ==================== 反序列化 ====================

        // 配置 JSON 串含有未知字段时, 反序列化依旧可以成功
        // 默认关闭，若POJO中不含有JSON中的属性，则抛出异常。关闭后，不解析该字段，而不会抛出异常。
        builder.featuresToDisable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        // 反序列化 LocalDateTime
        LocalDateDeserializer localDateDeserializer = new LocalDateDeserializer(dateFormatter);
        LocalTimeDeserializer localTimeDeserializer = new LocalTimeDeserializer(timeFormatter);
        LocalDateTimeDeserializer localDateTimeDeserializer = new LocalDateTimeDeserializer(dateTimeFormatter);
        builder.deserializers(localDateDeserializer, localTimeDeserializer, localDateTimeDeserializer);

        return builder;
    }

}
