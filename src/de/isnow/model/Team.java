package de.isnow.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;

import de.isnow.rest.JsonViews;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Team {
	
	@Getter
	@JsonView(JsonViews.CoarseJsonView.class)
	@JsonProperty("Country")
	private String code;
	
	@Getter
	@JsonView(JsonViews.CoarseJsonView.class)
	@JsonProperty("Team")
	private String name;
	
	
	@Getter
	@JsonView(JsonViews.ShortJsonView.class)
	@JsonProperty("FIFA ranking")
	private Integer ranking;
	
	@Getter
	@JsonView(JsonViews.CoarseJsonView.class)
	@JsonProperty("Group")
	private String group;
	
	@Getter
	@JsonView(JsonViews.MediumJsonView.class)
	@JsonProperty("Coach")
	private String coach;
	
	@Getter
	@JsonView(JsonViews.FullJsonView.class)
	@JsonProperty("Bio")
	private String description;
	
	@Getter
	@JsonView(JsonViews.FullJsonView.class)
	private String strengths;
	
	@Getter
	@JsonView(JsonViews.FullJsonView.class)
	private String weaknesses;
}
