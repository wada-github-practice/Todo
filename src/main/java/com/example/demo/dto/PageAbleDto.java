package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PageAbleDto {
	private int size;
	private int page;
	private String username;
}
