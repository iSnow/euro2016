package de.isnow.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import de.isnow.rest.JsonViews;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Player {

	@Getter
	@JsonView(JsonViews.CoarseJsonView.class)
	private String name;
	
	@Getter
	@JsonView(JsonViews.CoarseJsonView.class)
	private String bio;
	
	@Getter
	@JsonView(JsonViews.FullJsonView.class)
	@JsonProperty("photo done?")
	private String hasPhoto;

	@Getter
	@JsonView(JsonViews.FullJsonView.class)
	private String position;
	

	@Getter
	@JsonView(JsonViews.CoarseJsonView.class)
	private String club;

	@Getter
	@JsonView(JsonViews.CoarseJsonView.class)
	private String league;
}
