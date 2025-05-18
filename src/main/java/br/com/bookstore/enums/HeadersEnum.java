package br.com.bookstore.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HeadersEnum {

	X_TOTAL_PAGES("X-Total-Pages"),
	X_CURRENT_PAGE("X-Current-Page"),
	X_TOTAL_ELEMENTS("X-Total-Elements"),
	X_PAGE_SIZE("X-Page-Size");
	
	private String value;
}