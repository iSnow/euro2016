package de.isnow.dao;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;

import javax.persistence.EntityManager;
import javax.servlet.ServletContext;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import de.isnow.model.User;


public class UserDAO {
	private static EntityManager em;

	public UserDAO() {
	}

	public User getUser(String uid, ServletContext context) {
		User user = null;
		try {
			InputStream inputStream = 
					context.getResourceAsStream("/assets/csv/users.csv");
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream ));
			Iterable<CSVRecord> userRecords = CSVFormat
					.EXCEL
					.withHeader(new String[]{"UID", "NAME"})
					.withDelimiter(';')
					.parse(reader);
			
			Iterator<CSVRecord> iter = userRecords.iterator();
			while (iter.hasNext()) {
				CSVRecord userRec = iter.next();
				if (userRec.get("UID").equalsIgnoreCase(uid)) {
					user = new User(uid, userRec.get("NAME"));
				}
			}
			
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return user;
	}
}
