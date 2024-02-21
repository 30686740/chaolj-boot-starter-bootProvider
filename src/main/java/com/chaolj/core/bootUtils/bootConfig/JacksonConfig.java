package com.chaolj.core.bootUtils.bootConfig;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.convert.converter.Converter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {
    @Bean
    public Converter<String, Date> dateConverter() {
        return new Converter<String, Date>() {
            @Override
            public Date convert(String source) {
                return StringToJdkDate(source);
            }
        };
    }

    @Bean
    public Converter<String, java.sql.Date> sqlDateConverter() {
        return new Converter<String, java.sql.Date>() {
            @Override
            public java.sql.Date convert(String source) {
                return StringToSqlDate(source);
            }
        };
    }

    @Bean
    public Converter<String, Timestamp> timestampConverter() {
        return new Converter<String, Timestamp>() {
            @Override
            public Timestamp convert(String source) {
                return StringToTimestamp(source);
            }
        };
    }

    @Bean
    public Converter<String, LocalDateTime> localDateTimeConverter() {
        return new Converter<String, LocalDateTime>() {
            @Override
            public LocalDateTime convert(String source) {
                return StringToLocalDateTime(source);
            }
        };
    }

    @Bean
    public Converter<String, LocalDate> localDateConverter() {
        return new Converter<String, LocalDate>() {
            @Override
            public LocalDate convert(String source) {
                return StringToLocalDate(source);
            }
        };
    }

    @Bean
    public Converter<String, LocalTime> localTimeConverter() {
        return new Converter<String, LocalTime>() {
            @Override
            public LocalTime convert(String source) {
                return StringToLocalTime(source);
            }
        };
    }

    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public Jackson2ObjectMapperBuilderCustomizer myJacksonCustomizer() {
        return builder -> {
            // region 序列化

            builder.serializerByType(Date.class, new JsonSerializer<Date>() {
                @Override
                public void serialize(Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                    var format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    gen.writeString(format.format(value));
                }
            });

            builder.serializerByType(java.sql.Date.class, new JsonSerializer<java.sql.Date>() {
                @Override
                public void serialize(java.sql.Date value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                    var format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    gen.writeString(format.format(value));
                }
            });

            builder.serializerByType(Timestamp.class, new JsonSerializer<Timestamp>() {
                @Override
                public void serialize(Timestamp value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                    var format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    gen.writeString(format.format(value));
                }
            });

            builder.serializerByType(LocalDateTime.class, new JsonSerializer<LocalDateTime>() {
                @Override
                public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                    gen.writeString(value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
                }
            });

            builder.serializerByType(LocalDate.class, new JsonSerializer<LocalDate>() {
                @Override
                public void serialize(LocalDate value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                    gen.writeString(value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
                }
            });

            builder.serializerByType(LocalTime.class, new JsonSerializer<LocalTime>() {
                @Override
                public void serialize(LocalTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                    gen.writeString(value.format(DateTimeFormatter.ofPattern("HH:mm:ss")));
                }
            });

            builder.serializerByType(Float.class, new JsonSerializer<Float>() {
                @Override
                public void serialize(Float value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                    if (value == null) {
                        gen.writeNull();
                    }
                    else {
                        var format = new DecimalFormat("#0.0#######");
                        gen.writeRawValue(format.format(value));
                    }
                }
            });

            builder.serializerByType(Double.class, new JsonSerializer<Double>() {
                @Override
                public void serialize(Double value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
                    if (value == null) {
                        gen.writeNull();
                    }
                    else {
                        var format = new DecimalFormat("#0.0#######");
                        gen.writeRawValue(format.format(value));
                    }
                }
            });

            // endregion

            // region 反序列化

            builder.deserializerByType(Date.class, new JsonDeserializer<Date>() {
                @Override
                public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                    var source = jsonParser.getText();
                    return StringToJdkDate(source);
                }
            });

            builder.deserializerByType(java.sql.Date.class, new JsonDeserializer<Date>() {
                @Override
                public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                    var source = jsonParser.getText();
                    return StringToSqlDate(source);
                }
            });

            builder.deserializerByType(Timestamp.class, new JsonDeserializer<Timestamp>() {
                @Override
                public Timestamp deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                    var source = jsonParser.getText();
                    return StringToTimestamp(source);
                }
            });

            builder.deserializerByType(LocalDateTime.class, new JsonDeserializer<LocalDateTime>() {
                @Override
                public LocalDateTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                    var source = jsonParser.getText();
                    return StringToLocalDateTime(source);
                }
            });

            builder.deserializerByType(LocalDate.class, new JsonDeserializer<LocalDate>() {
                @Override
                public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                    var source = jsonParser.getText();
                    return StringToLocalDate(source);
                }
            });

            builder.deserializerByType(LocalTime.class, new JsonDeserializer<LocalTime>() {
                @Override
                public LocalTime deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
                    var source = jsonParser.getText();
                    return StringToLocalTime(source);
                }
            });

            // endregion

            builder.timeZone("GMT+8");
            //builder.serializationInclusion(JsonInclude.Include.NON_NULL);
            builder.failOnUnknownProperties(false);
            builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
            builder.propertyNamingStrategy(PropertyNamingStrategy.UPPER_CAMEL_CASE);
        };
    }

    public static java.sql.Date StringToSqlDate(String source) {
        if (StrUtil.isBlank(source)) return null;
        if (source.equalsIgnoreCase("null")) return null;

        try {
            return DateUtil.parse(source).toSqlDate();
        } catch (Exception e) {
            throw new RuntimeException("Date解析失败，内容(" + source + ")，不符合格式要求！" + e.getMessage());
        }
    }

    public static Date StringToJdkDate(String source) {
        if (StrUtil.isBlank(source)) return null;
        if (source.equalsIgnoreCase("null")) return null;

        try {
            return DateUtil.parse(source).toJdkDate();
        } catch (Exception e) {
            throw new RuntimeException("Date解析失败，内容(" + source + ")，不符合格式要求！" + e.getMessage());
        }
    }

    public static Timestamp StringToTimestamp(String source) {
        if (StrUtil.isBlank(source)) return null;
        if (source.equalsIgnoreCase("null")) return null;

        try {
            return DateUtil.parse(source).toTimestamp();
        } catch (Exception e) {
            throw new RuntimeException("Timestamp解析失败，内容(" + source + ")，不符合格式要求！" + e.getMessage());
        }
    }

    public static LocalDateTime StringToLocalDateTime(String source) {
        if (StrUtil.isBlank(source)) return null;
        if (source.equalsIgnoreCase("null")) return null;

        try {
            return DateUtil.parse(source).toLocalDateTime();
        } catch (Exception e) {
            throw new RuntimeException("LocalDateTime解析失败，内容(" + source + ")，不符合格式要求！" + e.getMessage());
        }
    }

    public static LocalDate StringToLocalDate(String source) {
        if (StrUtil.isBlank(source)) return null;
        if (source.equalsIgnoreCase("null")) return null;

        try {
            return DateUtil.parse(source).toLocalDateTime().toLocalDate();
        } catch (Exception e) {
            throw new RuntimeException("LocalDate解析失败，内容(" + source + ")，不符合格式要求！" + e.getMessage());
        }
    }

    public static LocalTime StringToLocalTime(String source) {
        if (StrUtil.isBlank(source)) return null;
        if (source.equalsIgnoreCase("null")) return null;

        try {
            var formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
            return LocalTime.parse(source, formatter);
        } catch (Exception e) {
            throw new RuntimeException("LocalTime解析失败，内容(" + source + ")，不符合格式要求！" + e.getMessage());
        }
    }
}
