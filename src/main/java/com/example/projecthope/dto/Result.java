package com.example.projecthope.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.function.Supplier;

/**
 * 功能：执行结果的封装对象
 *
 * @author chuchengyi
 */
@Getter
@Setter
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 2944008908509299972L;
    /**
     * 功能：是否成功
     */
    private boolean status;

    /**
     * 功能：返回码
     */
    private long code;

    /**
     * 功能：返回消息
     */
    private String message;

    /**
     * 功能：数据对象
     */
    private T data;


    public Result() {
        this.status = false;
    }

    /**
     * 功能：通过设置成功标识
     *
     * @param status
     */
    public Result(boolean status) {
        this.status = status;

    }

    /**
     * 功能：通过成功标志和数据对象来构造结果
     *
     * @param status
     * @param data
     */

    public Result(boolean status, T data) {
        this.status = status;
        this.data = data;
    }


    /**
     * 功能：通过成功标识返回的数据结果 和异常信息来构造对象
     *
     * @param status
     * @param data
     * @param code
     * @param message
     */
    public Result(boolean status, T data, long code, String message) {
        this.status = status;
        this.data = data;
        this.code = code;
        this.message = message;
    }


    /**
     * 功能：返回success Result
     *
     * @param data
     * @param <R>
     * @return
     */
    public static <R> Result<R> successData(R data) {
        return new Result<>(true, data, CodeEnum.SUCCESS.getCode(), CodeEnum.SUCCESS.getMessage());
    }

    /**
     * 能：返回 Result
     *
     * @param <R>
     * @return
     */
    public static <R> Result<R> successSupplierData(Supplier<R> supplier) {
        return new Result<>(true, supplier.get(), CodeEnum.SUCCESS.getCode(), CodeEnum.SUCCESS.getMessage());
    }


    /**
     * 能：返回空的 Result
     *
     * @param <R>
     * @return
     */
    public static <R> Result<R> successNullData() {
        return new Result<>(true, null, CodeEnum.SUCCESS.getCode(), CodeEnum.SUCCESS.getMessage());
    }


    /**
     * 功能：返回failure Result
     *
     * @param throwable
     * @param <R>
     * @return
     */
    public static <R> Result<R> failureData(Throwable throwable) {
        return new Result<>(false, null, CodeEnum.FAILURE.getCode(), throwable.getMessage());
    }

    /**
     * 功能：返回failure Result
     *
     * @param errMsg
     * @param <R>
     * @return
     */
    public static <R> Result<R> failureData(String errMsg) {
        return new Result<>(false, null, CodeEnum.FAILURE.getCode(), errMsg);
    }
}
