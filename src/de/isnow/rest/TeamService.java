package de.isnow.rest;

import java.io.IOException;

import javax.validation.constraints.NotNull;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import de.isnow.dao.TeamDAO;
import de.isnow.model.Team;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Path("/teams")
@Api(value = "Teams Service")
public class TeamService {
	private boolean inited = false;
	private ObjectMapper mapper;

	
	@GET
	@Path("/all")
	@Produces({MediaType.APPLICATION_JSON})
	@ApiOperation(value="Return information for all teams.")
	public Response getAll(
			@QueryParam("granularity") String granularityStr) throws IOException {
		init();
		TeamDAO tDao = new TeamDAO();
		Class<? extends JsonViews.CoarseJsonView> granularity = JsonViews.parseGranularity(granularityStr);
		ObjectWriter w = mapper.writerWithView(granularity);
		return Response.ok(")]}',\n"+w.writeValueAsString(tDao.getAll())).build();
	}
	
	@GET
	@Path("/details")
	@ApiOperation(value="Return detail information for a team.",
		notes="Return the team information for the team denoted by <code>teamid</code> parameter. "
				+ " This parameter expects a human-readable name (eg. 'France').<br>"
				+ " If <code>granularity</code> is not set, return medium granularity")
	@ApiResponses(value = { 
		      @ApiResponse(code = 200, message = "Return team information"),
		      @ApiResponse(code = 404, message = "No team for <code>teamid</code> found")
	})
	@Produces({MediaType.APPLICATION_JSON})
	public Response getDetails(
			@QueryParam("teamid") 
			@NotNull 
			@ApiParam(value = "Human-readable name for team") 
			String id,
			@QueryParam("granularity") 
			@ApiParam(value = "Amount of infomation detail")
			String granularityStr) throws IOException {
		init();
		TeamDAO tDao = new TeamDAO();
		Team team = tDao.findByName(id);
		if (null == team)
			return Response.status(404).build();
		Class<? extends JsonViews.CoarseJsonView> granularity = JsonViews.parseGranularity(granularityStr);
		ObjectWriter w = mapper.writerWithView(granularity);
		return Response.ok(")]}',\n"+w.writeValueAsString(team)).build();
	}

	@GET
	@Path("/players")
	@Produces({MediaType.APPLICATION_JSON})
	public Response getPlayers(
			@QueryParam("teamid") @NotNull String id,
			@QueryParam("granularity") String granularityStr) throws IOException {
		init();
		TeamDAO tDao = new TeamDAO();
		Class<? extends JsonViews.CoarseJsonView> granularity = JsonViews.parseGranularity(granularityStr);
		ObjectWriter w = mapper.writerWithView(granularity);
		String countryId = id.toUpperCase(); 
		return Response.ok(")]}',\n"+w.writeValueAsString(tDao.getPlayers(countryId))).build();
	}
	
	private void init() {
		if (!inited) {
			mapper = new ObjectMapper();
			mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
			mapper.setVisibility(PropertyAccessor.FIELD, Visibility.PROTECTED_AND_PUBLIC);
		}
	}
}
