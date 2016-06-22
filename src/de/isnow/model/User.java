package de.isnow.model;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.QueryHint;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.eclipse.persistence.annotations.Cache;
import org.eclipse.persistence.annotations.CacheType;
import org.eclipse.persistence.annotations.ReadOnly;
import org.eclipse.persistence.config.CacheUsage;
import org.eclipse.persistence.config.HintValues;
import org.eclipse.persistence.config.QueryHints;
import org.eclipse.persistence.config.QueryType;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;

import de.isnow.rest.JsonViews;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;



@Entity
@Table(name = "USERS")
@ReadOnly
@Cacheable(true)
@Cache(
	  type=CacheType.FULL, // Cache everything. 
	  			// Not a good choice for lots of instance obj
	  size=200  // Use 200 obj as the initial cache size.
	)
@NamedQueries ({
	@NamedQuery(name = "User.getAll", query = "SELECT u FROM User u", hints = {
			@QueryHint(name = QueryHints.QUERY_RESULTS_CACHE, value = "TRUE"),
			@QueryHint(name = QueryHints.CACHE_STATMENT, value = HintValues.TRUE),
			@QueryHint(name = QueryHints.QUERY_TYPE, value = QueryType.ReadAll),
			@QueryHint(name = QueryHints.JDBC_FETCH_SIZE, value = "500"),
			@QueryHint(name = QueryHints.CACHE_USAGE, value = CacheUsage.CheckCacheThenDatabase),
			@QueryHint(name = QueryHints.READ_ONLY, value = "TRUE") }),
			
	@NamedQuery(name = "User.resolve", query = "SELECT u FROM User u where u.uid = :uid", hints = {
			@QueryHint(name = QueryHints.QUERY_RESULTS_CACHE, value = "TRUE"),
			@QueryHint(name = QueryHints.CACHE_STATMENT, value = HintValues.TRUE),
			@QueryHint(name = QueryHints.QUERY_TYPE, value = QueryType.ReadAll),
			@QueryHint(name = QueryHints.JDBC_FETCH_SIZE, value = "500"),
			@QueryHint(name = QueryHints.CACHE_USAGE, value = CacheUsage.CheckCacheThenDatabase),
			@QueryHint(name = QueryHints.READ_ONLY, value = "TRUE") })		
})

@JsonIgnoreProperties({ 
	"_persistence_fetchGroup",
	"_persistence_studyType_vh"
})
@ToString(of={"uid",  "name"})
@EqualsAndHashCode(of={"uid", "name"})
public class User {
	
	@Id
	@NonNull
	@Column (name="UID", unique=true)
	@Getter
	@JsonView(JsonViews.CoarseJsonView.class)
	private String uid;
	
	@Column (name="FULL_NAME")
	@Getter
	@JsonView(JsonViews.ShortJsonView.class)
	private String name;
	
	@Column (name="EMAIL")
	@Getter
	@JsonView(JsonViews.MediumJsonView.class)
	private String email;
	
	@Column (name="IS_GROUP_ADMIN")
	@JsonIgnore
	private String groupAdmin="N";
	
	@Column (name="IS_DEVELOPER")
	@JsonIgnore
	private String developer="N";
	
	@Column (name="IS_DISABLED")
	@Getter
	@JsonIgnore
	private String disabled="N";
	
	@Transient
	@JsonView(JsonViews.MediumJsonView.class)
	private Map<String, Object> props = new LinkedHashMap<String, Object>();


	@JsonIgnore
	public boolean isGroupAdmin() {
		return (groupAdmin.equalsIgnoreCase("Y"));
	}

	@JsonIgnore
	public boolean isDeveloper() {
		return (developer.equalsIgnoreCase("Y"));
	}
	
	public Object getProperty (String key){
		return props.get(key);
	}
	
	public void setProperty (String key, Object value) {
		props.put(key, value);
	}

	public User() {	
	}

	public User(String uid, String name) {
		super();
		this.uid = uid;
		this.name = name;
	}
	
	
	
}
