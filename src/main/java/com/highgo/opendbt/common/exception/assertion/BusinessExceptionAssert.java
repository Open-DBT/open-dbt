package com.highgo.opendbt.common.exception.assertion;

import com.highgo.opendbt.common.exception.BaseException;
import com.highgo.opendbt.common.exception.BusinessException;
import com.highgo.opendbt.common.exception.enums.IResponseEnum;

import java.text.MessageFormat;
//接口可以继承多个接口
public interface BusinessExceptionAssert extends IResponseEnum, Assert {
//jdk8以后接口里面可以包含方法体,但必须使用default或static修饰，
// 而且我们知道实现类如果继承了接口，实现类必须强制重现接口的方法，但是这两种方式是不需要强制重写的，相当于没有abstract修饰
    @Override
    default BaseException newException(Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);

        return new BusinessException(this, args, msg);
    }

    @Override
    default BaseException newException(Throwable t, Object... args) {
        String msg = MessageFormat.format(this.getMessage(), args);

        return new BusinessException(this, args, msg, t);
    }

}
