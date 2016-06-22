package de.isnow.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import de.isnow.model.Player;
import de.isnow.model.Team;

public class TeamDAO {

	public List<Team> getAll() throws JsonParseException, JsonMappingException, IOException {
		String str = getWarFileResource("assets/json/teams/teams.json");
		JsonNode n = parseSheetFormat(str).get("Teams");
		ObjectMapper objectMapper = new ObjectMapper();
		List<Team> teams = objectMapper.readValue(n.toString(), new TypeReference<List<Team>>(){});		
		return teams;
	}
	
	/*public Team getOne(String id) throws JsonParseException, JsonMappingException, IOException {
		List<Team> teams = getAll();
		List<Team> filteredTeams = teams
			.stream()
			.filter(t -> t.getName().equalsIgnoreCase(id))
			.collect(Collectors.toList());
		if (filteredTeams.isEmpty())
			return null;
		return filteredTeams.get(0);
	}*/
	
	public Team getOne(String id) throws JsonParseException, JsonMappingException, IOException {
		List<Team> teams = getAll();
		List<Team> filteredTeams = teams
			.stream()
			.filter(t -> t.getCode().equalsIgnoreCase(id))
			.collect(Collectors.toList());
		if (filteredTeams.isEmpty())
			return null;
		return filteredTeams.get(0);
	}
	

	public Team findByName(String name) throws JsonParseException, JsonMappingException, IOException {
		List<Team> teams = getAll();
		List<Team> filteredTeams = teams
			.stream()
			.filter(t -> t.getName().equalsIgnoreCase(name))
			.collect(Collectors.toList());
		if (filteredTeams.isEmpty())
			return null;
		return filteredTeams.get(0);
	}
	
	public List<Player> getPlayers(String teamId) throws JsonParseException, JsonMappingException, IOException {
		Team team = getOne(teamId);
		String normName = team
				.getName()
				.replaceAll("\\s", "-")
				.toLowerCase();
		String str = getWarFileResource("assets/json/teams/"+normName+"-players.json");		
		JsonNode n = parseSheetFormat(str).get("Players");
		ObjectMapper objectMapper = new ObjectMapper();
		List<Player> players = objectMapper.readValue(n.toString(), new TypeReference<List<Player>>(){});
		return players;
	}

	private static String getWarFileResource(String fileName) throws UnsupportedEncodingException, FileNotFoundException, IOException {
		URL url = Thread.currentThread().getContextClassLoader().getResource("/META-INF");
		String fullPath = url.toString().replaceFirst("file:\\/", "");
		File parent = new File(fullPath).getParentFile().getParentFile().getParentFile();
		File file = new File(parent, fileName);
		
		try (BufferedReader reader = new BufferedReader(
				   new InputStreamReader(new FileInputStream(file), "UTF8"))) {
			String str = reader.lines().collect(Collectors.joining("\n"));
			return str;
		}
	}
	
	private static JsonNode parseSheetFormat(String str) throws JsonProcessingException, IOException {
		ObjectMapper objectMapper = new ObjectMapper();
		JsonNode node = objectMapper.readTree(str);
		if (node.isObject()) {
		    ObjectNode obj = (ObjectNode) node;
		    if (obj.has("sheets")) {
		    	JsonNode n = obj.get("sheets");
		    	return n;
		    }
		}
		return null;
	}

}
