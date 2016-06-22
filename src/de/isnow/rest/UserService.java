package de.isnow.rest;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import de.isnow.WebApplication;
import de.isnow.dao.UserDAO;
import de.isnow.model.User;
import io.swagger.annotations.Api;


@Path("/user")
@Api(value = "User Information")
public class UserService {
	private UserDAO uDao;
	private boolean inited = false;
	private ObjectMapper mapper;
	private @Context SecurityContext sc;
	
	private final static Logger LOG = LogManager.getLogger(UserService.class.getName());
	
	/**
	 * the <em>realm</em> parameter is set on the login form as a hidden parameter (currently, 
	 * it could be extended to be a HTML &lt;select&gt; control so the user can choose
	 * into which project she wants to log in). 
	 * On submit, realm parameter input's value is transferred to a cookie with the name 'j_realm'
	 * and this cookie in turn is read in the Javascript <em>userService</em> in AngularJS.
	 * The Javascript userService gets called from ng-header to retrieve information about
	 * the user from the backend. 
	 * 
	 * This is the backend function that answers to the Javascript userService call. Its 
	 * <em>realm</em> parameter comes from the cookie as described above.
	 */
	@GET
	@Path("/info")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response loginGet (
			@QueryParam("realm") String realm,
			@Context SecurityContext sc,
			@Context ServletContext ctx,
			@Context HttpServletRequest req) throws JsonProcessingException {
		
		init();
		if (!WebApplication.isReady()) {
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
		}
		
		User user = (User) ctx.getAttribute("user");
		if (null == user)
			return null;
		user.setProperty("developer", user.isDeveloper());
		user.setProperty("groupAdmin", user.isGroupAdmin());
		user.setProperty("realm", realm);
		ObjectWriter w = mapper.writerWithView(JsonViews.FullJsonView.class);
		return Response.ok(")]}',\n"+w.writeValueAsString(user)).build();
	}
	
	@POST
	@Path("/login")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	public Response loginPost (
			@FormParam("realm") String realm,
			@FormParam("username") String username,
			@FormParam("password") String password,
			@Context SecurityContext sc,
			@Context ServletContext ctx,
			@Context HttpServletRequest req) throws JsonProcessingException {
		
		init();
		if (!WebApplication.isReady()) {
			return Response.status(Response.Status.SERVICE_UNAVAILABLE).build();
		}
		
		if (null == username)
			return null;
		User user = uDao.getUser(username, ctx);
		if (null == user)
			return null;
		ctx.setAttribute("user", user);
		user.setProperty("developer", user.isDeveloper());
		user.setProperty("groupAdmin", user.isGroupAdmin());
		user.setProperty("realm", realm);
		ObjectWriter w = mapper.writerWithView(JsonViews.FullJsonView.class);
		return Response.ok(")]}',\n"+w.writeValueAsString(user)).build();
	}
	
	@GET
	@Path("/logout")
	@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
	public Response logout(
			@Context HttpServletRequest req,
			@Context ServletContext ctx) {
		init();
		ctx.setAttribute("user", null);
		String currSessionId = req.getSession().getId();
		LOG.debug("prior to invalidation: SessionId: " + currSessionId + ", is valid: " + req.isRequestedSessionIdValid());
				
		if (req.getSession() != null){
			req.getSession().invalidate();
		}
		
		LOG.debug("after invalidation: SessionId: " + req.getSession(true).getId() + ", is old id still valid: " + req.isRequestedSessionIdValid());
		
		/*
		 * need to find the username from the session-context
		 * or cookie or whatever
		 */

		return Response.ok().build();
	}
	
	
	
	
	
	private void init() {
		if (!inited) {
			uDao = new UserDAO();
			inited = true;
			mapper = new ObjectMapper();
			mapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
			mapper.setVisibility(PropertyAccessor.FIELD, Visibility.PROTECTED_AND_PUBLIC);
		}
	}
}
