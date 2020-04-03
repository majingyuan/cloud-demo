package com.jd.nebulae.common.annotation;

import java.lang.annotation.*;

/**
 * 配合{@link com.jd.nebulae.common.tools.Conditions#hasNullOrEmpty(Object...)}使用
 *
 * @see com.jd.nebulae.common.tools.Conditions
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NotNull {
}
