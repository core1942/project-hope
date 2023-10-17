package com.example.projecthope.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class PageResult<T> extends Result<List<T>> {

	/**
	 * 功能：数据总量
	 */
	private long count;


	public PageResult(boolean status, List<T> data, long code, String message, long total) {
		super(status, data, code, message);
		this.count = total;
	}

	/**
	 * 功能：返回success PageResult
	 *
	 * @param dataList
	 * @param total
	 * @param <R>
	 * @return
	 */
	public static <R> PageResult<R> successData(List<R> dataList, long total) {
		return new PageResult<>(true, dataList, CodeEnum.SUCCESS.getCode(), CodeEnum.SUCCESS.getMessage(), total);
	}

}
