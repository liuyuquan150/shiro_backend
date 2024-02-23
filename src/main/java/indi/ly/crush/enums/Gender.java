package indi.ly.crush.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * <h2>性别</h2>
 *
 * @since 1.0
 * @author 云上的云
 * @formatter:off
 */
public enum Gender {
	/**
	 * <p>
	 *     性别之一: 女.
	 * </p>
	 */
	FEMALE("女"),
	/**
	 * <p>
	 *     性别之一: 男.
	 * </p>
	 */
	MALE("男"),
	/**
	 * <p>
	 *     性别之一: 私密.
	 * </p>
	 */
	UNKNOWN("私密");

	private final String description;

	Gender(String description) {
		this.description = description;
	}

	@JsonValue
	public String getDescription() {
		return description;
	}
}