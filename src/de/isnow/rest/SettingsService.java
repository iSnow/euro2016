package de.isnow.rest;

import java.io.IOException;
import java.util.Map;
import java.util.TreeMap;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import de.isnow.WebApplication;
import io.swagger.annotations.Api;

@Path("/settings")
@Api(value = "Project Settings Service")
public class SettingsService {
	private ObjectMapper mapper;
	private @Context SecurityContext sc;
	
	
	@GET
	@Path("/system")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response getSystemSettings() throws JsonGenerationException, JsonMappingException, IOException {
		init();
		Map<String, Object> settings = new TreeMap<String, Object>();
		
		
		settings.put("instance", WebApplication.getDeploymentInstance());
		return Response.ok(")]}',\n"+mapper.writeValueAsString(settings)).build();
	}



	private void init() {
		mapper = new ObjectMapper();
		mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
		mapper.setVisibility(PropertyAccessor.FIELD, Visibility.PROTECTED_AND_PUBLIC);
	}
}
