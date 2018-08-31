package com.clsaa.maat.result;


import com.clsaa.maat.result.exception.*;


/**
 * 常用的一些断言，不满足断言条件将抛出一些内置的运行时异常，可以指定业务错误码和业务提示消息。<br>
 * <pre>
 * {
 * "timestamp": 1524388994224,
 * "path": "/user/2",
 * "status": 500,
 * "code": -1,
 * "message": "/ by zero",
 * "error": "java.lang.ArithmeticException: / by zero"
 * }
 * </pre>
 * <p>
 * 如果常见断言不满足业务，各系统可继承 {@link StandardBusinessException}，可直接抛出。例如：
 *
 * @author 任贵杰
 */
public abstract class BizAssert {

    private BizAssert() {
        super();
    }

    /**
     * 判定记录expression=true ,如果expression=false，则抛出 {@link NotFoundException} ，对应http status: 404
     *
     * @param expression 断言表达式
     * @param bizCode    业务码信息，不能为null
     */
    public static void found(boolean expression, Object bizCode) {
        if (!expression) {
            justNotFound(bizCode);
        }
    }

    /**
     * <p>
     * 记录不存在，异常对应的Http 状态码：404<br>
     * 直接抛出{@link NotFoundException}，指定错误信息。
     * </p>
     *
     * @param message 业务提示信息
     */
    public static void justNotFound(String message) {
        throw new NotFoundException(message);
    }

    /**
     * <p>
     * 记录不存在，异常对应的Http 状态码：404<br>
     * 直接抛出{@link NotFoundException},可以指定业务系统的错误码和提示消息
     * </p>
     *
     * @param errorCode 业务错误码
     * @param message   业务提示信息
     */
    public static void justNotFound(int errorCode, String message) {
        throw new NotFoundException(errorCode, message);
    }

    /**
     * <p>
     * 记录是否存在，异常对应的Http 状态码：404<br>
     * expression 为false时抛出{@link NotFoundException}，指定错误信息。
     * </p>
     *
     * @param expression 业务boolean 表达式
     * @param message    业务提示信息
     */
    public static void found(boolean expression, String message) {
        if (!expression) {
            justNotFound(message);
        }
    }

    /**
     * <p>
     * 判定记录expression=true <br>
     * 如果expression=false时抛出{@link NotFoundException}，
     * 异常对应的Http 状态码：404,可以指定业务系统的错误码和提示消息。
     * </p>
     *
     * @param expression 断言表达式
     * @param errorCode  业务错误码
     * @param message    业务提示信息
     */
    public static void found(boolean expression, int errorCode, String message) {
        if (!expression) {
            justNotFound(errorCode, message);
        }
    }

    /**
     * 直接抛出 {@link NotFoundException}，对应的Http 状态码：404，同时将输出 {@code RestResult}
     *
     * @param bizCode 业务码信息
     */
    public static void justNotFound(Object bizCode) {
        RestResult result = RestResultProviders.getRestResult(bizCode);
        justNotFound(result.getCode(), result.getMessage());
    }


    /**
     * <p>
     * 参数是否非法，异常对应的Http 状态码：400<br>
     * expression为false时抛出{@link InvalidParameterException},可以指定业务系统的错误码和提示消息.
     * </p>
     *
     * @param expression 断言表达式
     * @param errorCode  业务错误码
     * @param message    业务提示信息
     * @since 1.0.0
     */
    public static void validParam(boolean expression, int errorCode,
                                  String message) {
        if (!expression) {
            justInvalidParam(errorCode, message);
        }
    }

    /**
     * <p>
     * 参数是否非法，异常对应的Http 状态码：400<br>
     * expression 为false时抛出{@link InvalidParameterException},可以指定业务提示消息。
     * </p>
     *
     * @param expression 断言表达式
     * @param message    业务提示信息
     * @since 1.0.0
     */
    public static void validParam(boolean expression, String message) {
        if (!expression) {
            justInvalidParam(message);
        }
    }


    /**
     * 参数是否异常，如果false，则抛出 {@link InvalidParameterException}，对应 http status : 400
     *
     * @param expression 断言表达式
     * @param bizCode    业务码信息
     * @since 1.0.0
     */
    public static void validParam(boolean expression, Object bizCode) {
        if (!expression) {
            justInvalidParam(bizCode);
        }
    }

    /**
     * 直接抛出参数异常，对应Http 状态码： 400
     *
     * @param message 业务提示信息
     * @since 1.0.0
     */
    public static void justInvalidParam(int errorCode, String message) {
        throw new InvalidParameterException(errorCode, message);
    }

    /**
     * 直接抛出业务异常，对应Http 状态码： 400
     *
     * @param message 业务提示信息
     * @since 1.0.0
     */
    public static void justInvalidParam(String message) {
        throw new InvalidParameterException(message);
    }

    /**
     * 直接抛出非法参数异常，对应的Http 状态码：400，同时将输出 {@code RestResult}
     *
     * @param bizCode 业务码信息
     */
    public static void justInvalidParam(Object bizCode) {
        RestResult restResult = RestResultProviders.getRestResult(bizCode);
        justInvalidParam(restResult.getCode(), restResult.getMessage());
    }


    /**
     * <p>
     * 是否允许访问，异常对应的Http 状态码：403<br>
     * expression 为false时抛出{@link AccessDeniedException},可以指定业务系统提示消息
     * </p>
     *
     * @param expression 断言表达式
     * @param message    业务提示信息
     * @since 1.0.0
     */
    public static void allowed(boolean expression, String message) {
        if (!expression) {
            justDenied(message);
        }
    }

    /**
     * <p>
     * 是否允许访问，异常对应的Http 状态码：403<br>
     * expression 为false时抛出{@link AccessDeniedException},可以指定业务系统的错误码和提示消息
     * </p>
     *
     * @param expression 断言表达式
     * @param message    业务提示信息
     * @since 1.0.0
     */
    public static void allowed(boolean expression, int errorCode,
                               String message) {
        if (!expression) {
            justDenied(errorCode, message);
        }
    }

    /**
     * 是否允许访问，如果 expression = false ,则不允许访问，对应Http 状态码为： 403;
     *
     * @param expression 断言表达式
     * @param bizcode    业务码信息
     * @since 1.0.0
     */
    public static void allowed(boolean expression, Object bizcode) {
        if (!expression) {
            justDenied(bizcode);
        }
    }


    /**
     * 直接抛出访问被拒绝，对应Http 状态码： 403
     *
     * @param message 业务提示信息
     * @since 1.0.0
     */
    public static void justDenied(int errorCode, String message) {
        throw new AccessDeniedException(errorCode, message);
    }

    /**
     * 直接抛出访问被拒绝异常，对应Http 状态码： 403
     *
     * @param message 业务提示信息
     * @since 1.0.0
     */
    public static void justDenied(String message) {
        throw new AccessDeniedException(message);
    }

    /**
     * 直接抛出访问被拒绝异常，对应的Http 状态码：403，同时将输出 {@code RestResult}
     *
     * @since 1.0.0
     */
    public static void justDenied(Object bizCode) {
        RestResult result = RestResultProviders.getRestResult(bizCode);
        justDenied(result.getCode(), result.getMessage());
    }

    /**
     * <p>
     * 是否已授权，异常对应的Http 状态码：401<br>
     * expression 为false时抛出{@link UnauthorizedException}，指定错误信息。
     * </p>
     *
     * @param expression 断言表达式
     * @param message    业务提示信息
     * @since 1.0.0
     */
    public static void authorized(boolean expression, String message) {
        if (!expression) {
            justUnauthorized(message);
        }
    }

    /**
     * <p>
     * 是否已授权，异常对应的Http 状态码：401<br>
     * expression 为false时抛出{@link UnauthorizedException},可以指定业务系统的错误码和提示消息
     * </p>
     *
     * @param expression 断言表达式
     * @param errorCode  业务错误码
     * @param message    业务提示信息
     * @since 1.0.0
     */
    public static void authorized(boolean expression, int errorCode,
                                  String message) {
        if (!expression) {
            justUnauthorized(errorCode, message);
        }
    }

    /**
     * 是否授权,对应http 状态码 401，如果 expression = false ,则直接抛出异常 {@link UnauthorizedException};
     *
     * @since 1.0.0
     */
    public static void authorized(boolean expression, Object bizCode) {
        if (!expression) {
            justUnauthorized(bizCode);
        }
    }


    /**
     * <p>
     * 业务逻辑是否正常，异常对应的Http 状态码：417<br>
     * expression 为false时抛出{@link StandardBusinessException},可以指定业务系统的错误码和提示消息
     * </p>
     *
     * @param expression 断言表达式
     * @param errorCode  业务错误码
     * @param message    业务提示信息
     * @since 1.0.0
     */
    public static void pass(boolean expression, int errorCode, String message) {
        if (!expression) {
            justFailed(errorCode, message);
        }
    }

    /**
     * <p>
     * 业务逻辑是否正常，异常对应的Http 状态码：417<br>
     * expression 为false时抛出{@link StandardBusinessException},可以指定业务系统提示消息
     * </p>
     *
     * @param expression 断言表达式
     * @param message    业务提示信息
     * @since 1.0.0
     */
    public static void pass(boolean expression, String message) {
        if (!expression) {
            justFailed(message);
        }
    }

    /**
     * 业务逻辑是否正常，如果 expression = false ,则直接抛出异常 {@link StandardBusinessException}，对应http status:417
     *
     * @param expression 断言表达式
     * @param bizCode    业务码信息
     * @since 1.0.0
     */
    public static void pass(boolean expression, Object bizCode) {
        if (!expression) {
            justFailed(bizCode);
        }
    }

    /**
     * 直接抛出业务失败异常，对应Http 状态码： 417
     *
     * @param message 业务提示信息
     * @since 1.0.0
     */
    public static void justFailed(int errorCode, String message) {
        throw new StandardBusinessException(errorCode, message);
    }

    /**
     * 直接抛出业务失败异常，对应Http 状态码： 417
     *
     * @param message 业务提示信息
     * @since 1.0.0
     */
    public static void justFailed(String message) {
        throw new StandardBusinessException(message);
    }

    /**
     * 直接抛出业务异常，对应的Http 状态码：417，同时将输出 {@code RestResult}
     *
     * @param bizcode 业务码信息
     */
    public static void justFailed(Object bizcode) {
        RestResult result = RestResultProviders.getRestResult(bizcode);
        justFailed(result.getCode(), result.getMessage());
    }

    /**
     * 未授权，异常对应的Http 状态码：401<br>
     * 直接抛出{@link UnauthorizedException}，可指定错误信息。
     *
     * @param message 业务提示信息
     * @since 1.0.0
     */
    public static void justUnauthorized(String message) {
        throw new UnauthorizedException(message);
    }

    /**
     * 未授权，对应的Http 状态码：401<br>
     * 直接抛出{@link UnauthorizedException},可以指定业务系统的错误码和提示消息
     *
     * @param errorCode 业务错误码
     * @param message   业务提示信息
     * @since 1.0.0
     */
    public static void justUnauthorized(int errorCode, String message) {
        throw new UnauthorizedException(errorCode, message);
    }

    /**
     * 直接抛出未授权异常，对应的Http 状态码：401，同时将输出 {@code RestResult}
     *
     * @param bizcode 业务码信息，不能为空
     * @since 1.0.0
     */
    public static void justUnauthorized(Object bizcode) {
        RestResult result = RestResultProviders.getRestResult(bizcode);
        justUnauthorized(result.getCode(), result.getMessage());
    }
}
