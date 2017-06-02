package com.example.controller;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.request;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController

public class Controller {

	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST) 
	public Response postLogin(@RequestBody String json) throws JSONException {
		System.out.println(json);
		JSONObject jsonObj = new JSONObject(json);
		String username = jsonObj.getString("username");
		String password = jsonObj.getString("password");
		int rows = jdbcTemplate.queryForList("SELECT * FROM Users WHERE username = " + "\'" + username + "\' AND password = " + "\'" + password + "\'").size();
		List<Map<String, Object>> data = jdbcTemplate.queryForList("SELECT ssn FROM Users WHERE username = " + "\'" + username + "\' AND password = " + "\'" + password + "\'");
		List<Map<String, Object>> dataExtra = jdbcTemplate.queryForList("SELECT Id FROM Account A, Users U WHERE U.username = " + "\'" + username + "\' AND U.password = " + "\'" + password + "\' AND U.ssn = A.Customer");
		
		String key = null;
        String value = null;
        String valueExtra = null;

		for (Map<String, Object> map : data) {
		    for (Map.Entry<String, Object> entry : map.entrySet()) {
		        key = entry.getKey();
		        value = (String)entry.getValue().toString();	// GRAB SSN
		    }
		}
		
		for (Map<String, Object> map : dataExtra) {
		    for (Map.Entry<String, Object> entry : map.entrySet()) {
		        key = entry.getKey();
		        valueExtra = (String)entry.getValue().toString();	// GRAB ACCOUNT ID
		    }
		}
		
		if (rows >= 1) {
			Response response = new Response("Done", value, valueExtra);
			return response;
		}
		else {
			Response response = new Response("Error", json);
			return response;
		}
		
	}

	@RequestMapping(value = "/signup", method = RequestMethod.POST)
	public Response postSignup(@RequestBody String json) throws JSONException {
		System.out.println(json);
		JSONObject jsonObj = new JSONObject(json);
		String username = jsonObj.getString("username");
		String email = jsonObj.getString("email");
		String password = jsonObj.getString("password");
		String FirstName = jsonObj.getString("firstname");
		String LastName = jsonObj.getString("lastname");
		String SSN = jsonObj.getString("socialsecuritynumber");
		String creditcardnumber = jsonObj.getString("creditcardnumber");
		String Address = jsonObj.getString("address");
		int ZipCode = Integer.parseInt(jsonObj.getString("zipcode"));
		String Telephone = jsonObj.getString("telephone");
		String city = jsonObj.getString("city");
		String state = jsonObj.getString("state");
		
		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM.dd");
		
		int rowsAffected = jdbcTemplate.queryForList("SELECT * FROM Location WHERE ZipCode = " + ZipCode).size();
		
		int rows = 0;
		
		if (rowsAffected == 1) {	// DUPLICATE EXISTS
			rows =	jdbcTemplate.update("INSERT INTO Person (SSN, LastName, FirstName, Address, ZipCode, Telephone) VALUES (" + "\'"
					+ SSN + "\' " + ", " + "\'" + LastName + "\', " + "\'" + FirstName + "\', " + "\'" + Address
					+ "\', " + ZipCode + ", \'" + Telephone + "\'" + ")") +
			jdbcTemplate.update("INSERT INTO Customer (Id, Email, Rating, CreditCardNumber) VALUES (" + "\'" + SSN
					+ "\'" + ", " + "\'" + email + "\', " + 1 + ", " + "\'" + creditcardnumber + "\'" + ")") + 
			jdbcTemplate.update("INSERT INTO Account (DateOpened, Type, Customer) VALUES (" + "\'" + ft.format(dNow) + "\', " + "\'" + "limited" + "\', " + "\'" + SSN + "\')") +
			jdbcTemplate.update("INSERT INTO Users(username, password, ssn) VALUES (" + "\'" + username + "\', " + "\'" + password + "\'," + "\'" + SSN + "\')");
			
		}
		else if (rowsAffected == 0) {	// NO ZipCode IN LOCATION
			rows = jdbcTemplate
					.update("INSERT INTO Location (ZipCode, City, State) VALUES (" + ZipCode + ", \'" + city + "\', "
							+ "\'" + state + "\')")
					+ jdbcTemplate
							.update("INSERT INTO Person (SSN, LastName, FirstName, Address, ZipCode, Telephone) VALUES ("
									+ "\'" + SSN + "\' " + ", " + "\'" + LastName + "\', " + "\'" + FirstName + "\', "
									+ "\'" + Address + "\', " + ZipCode + ", \'" + Telephone + "\'" + ")")
					+ jdbcTemplate.update(
							"INSERT INTO Customer (Id, Email, Rating, CreditCardNumber) VALUES (" + "\'" + SSN + "\'"
									+ ", " + "\'" + email + "\', " + 1 + ", " + "\'" + creditcardnumber + "\'" + ")")
					+ jdbcTemplate.update("INSERT INTO Account (DateOpened, Type, Customer) VALUES (" + "\'"
							+ ft.format(dNow) + "\', " + "\'" + "limited" + "\', " + "\'" + SSN + "\')")
					+ jdbcTemplate.update("INSERT INTO Users(username, password, ssn) VALUES (" + "\'" + username
							+ "\', " + "\'" + password + "\'," + "\'" + SSN + "\')");
		}
		
		if (rows >= 4) {
			Response response = new Response("Done", json);
			return response;
		}
		else {
			Response response = new Response("Error", json);
			return response;
		}
	}
	
	// THIS ONE
	@RequestMapping(value = "/accountcheck/{ssn}", method = RequestMethod.GET)
	public Response getAccountStatus(@PathVariable("ssn") String ssn) {
		System.out.println(ssn);
		int managerStatus = jdbcTemplate.queryForList("SELECT * FROM Employee WHERE SSN = " + "\'" + ssn + "\' AND ManagerStatus = 1").size();
		int custRepStatus = jdbcTemplate.queryForList("SELECT * FROM Employee WHERE SSN = " + "\'" + ssn + "\' AND ManagerStatus = 0").size();
		int customerStatus = jdbcTemplate.queryForList("SELECT * FROM Customer WHERE Id = " + "\'" + ssn + "\'").size();
		
		if (managerStatus == 1) {
			Response response = new Response("Done", "100");	// MANAGER
			return response;
		}
		else if (custRepStatus == 1) {
			Response response = new Response("Done", "200");	// CUST. REP
			return response;
		}
		else if (customerStatus == 1) {
			Response response = new Response("Done", "300");	// CUSTOMER
			return response;
		}
		else {
			Response response = new Response("Error", ssn);
			return response;
		}
		
	}

	@RequestMapping(value = "query/1a", method = RequestMethod.POST)
	public Response postMovie(@RequestBody String json) throws JSONException {
		System.out.println(json);
		
JSONObject jsonObj = new JSONObject(json);
		int id = Integer.parseInt(jsonObj.getString("id"));
		String name = jsonObj.getString("name");
		String type = jsonObj.getString("type");
		int rating = Integer.parseInt(jsonObj.getString("rating"));
		int distrfee = Integer.parseInt(jsonObj.getString("distrfee"));
		int numcopies = Integer.parseInt(jsonObj.getString("numcopies"));
		int rows = jdbcTemplate.update("INSERT INTO Movie(Id, Name, Type, Rating, DistrFee, NumCopies) VALUES (" + id + ", "
				+ "\'" + name + "\', " + "\'" + type + "\', " + rating + ", " + distrfee + ", " + numcopies + ")");
		if (rows >= 1) {
			Response response = new Response("Done", json);
			return response;
		}
		else {
			Response response = new Response("Error", json);
			return response;
		}
	}
	


	@RequestMapping(value = "query/1b", method = RequestMethod.POST)
	public Response editMovie(@RequestBody String json) throws JSONException {
		System.out.println(json);
		JSONObject jsonObj = new JSONObject(json);
		String attr = jsonObj.getString("attr");
		String attrval = jsonObj.getString("attrval");
		int id = Integer.parseInt(jsonObj.getString("id"));
		int rows = jdbcTemplate.update("UPDATE Movie SET " + attr + "=" + "\"" + attrval + "\"" + " WHERE Id = " + id);
		
		if (rows >= 1) {
			Response response = new Response("Done", json);
			return response;
		}
		else {
			Response response = new Response("Error", json);
			return response;
		}
	}

	@RequestMapping(value = "query/1c/{id}", method = RequestMethod.DELETE)
	public Response deleteMovie(@PathVariable("id") int id) {
		System.out.println(id);
		int rows = jdbcTemplate.update("DELETE FROM AppearedIn WHERE MovieId = " + id)
				+ jdbcTemplate.update("DELETE FROM MovieQ WHERE MovieId = " + id)
				+ jdbcTemplate.update("DELETE FROM Rental WHERE MovieId = " + id)
				+ jdbcTemplate.update("DELETE FROM Movie WHERE Id = " + id);
		if (rows >= 1) {
			Response response = new Response("Done", null);
			return response;
		}
		else {
			Response response = new Response("Error", null);
			return response;
		}
	}

	@RequestMapping(value = "query/2a", method = RequestMethod.POST)
	public Response postEmployee(@RequestBody String json) throws JSONException {
		System.out.println(json);
		JSONObject jsonObj = new JSONObject(json);
		int ID = Integer.parseInt(jsonObj.getString("id"));
		String SSN = jsonObj.getString("ssn");
		String StartDate = jsonObj.getString("startdate");
		
		
		int HourlyRate = Integer.parseInt(jsonObj.getString("hourlyrate"));
		int Status = Integer.parseInt(jsonObj.getString("managerstatus"));
		int rows = jdbcTemplate.update("INSERT INTO Employee(ID, SSN, StartDate, HourlyRate, ManagerStatus) VALUES (" + ID + ", " + "\'" + SSN + "\', " + "\'" + 
				StartDate + "\', " + HourlyRate + ", " + Status + ")");
		if (rows >= 1) {
			Response response = new Response("Done", json);
			return response;
		}
		else {
			Response response = new Response("Error", json);
			return response;
		}
	}
	
	
	
	@RequestMapping(value = "query/2b", method = RequestMethod.POST)
	public Response editEmployee(@RequestBody String json) throws JSONException {
		System.out.println(json);
		JSONObject jsonObj = new JSONObject(json);
		String attr = jsonObj.getString("attr");
		String attrval = jsonObj.getString("attrval");
		int id = Integer.parseInt(jsonObj.getString("id"));
		int rows = jdbcTemplate.update("UPDATE Employee SET " + attr + "=" + "\"" + attrval + "\"" + " WHERE ID = " + id);
		if (rows >= 1) {
			Response response = new Response("Done", json);
			return response;
		}
		else {
			Response response = new Response("Error", json);
			return response;
		}
	}

	@RequestMapping(value = "query/2c/{ssn}", method = RequestMethod.DELETE)
	public Response deleteEmployee(@PathVariable("ssn") String ssn) {
		System.out.println(ssn);
		int rows = jdbcTemplate.update("DELETE FROM Employee WHERE SSN = " + "\'" + ssn + "\'");
		
		int affectedRows = jdbcTemplate.queryForList("SELECT * FROM Customer WHERE Id = " + "\'" + ssn + "\'").size();
		
		if (affectedRows == 0) {
			jdbcTemplate.update("DELETE FROM Person WHERE SSN = " + "\'" + ssn + "\'");
			rows += 1;
		}
		
		if (rows >= 1) {
			Response response = new Response("Done", ssn);
			return response;
		}
		else {
			Response response = new Response("Error", ssn);
			return response;
		}
	}

	@RequestMapping(value = "/query/3/{date}", method = RequestMethod.GET)
	public List<Map<String, Object>> getSalesReport(@PathVariable("date") String date) {
		System.out.println(date);
//		jdbcTemplate.execute("DROP VIEW IF EXISTS Overall_Income");
//		jdbcTemplate.execute(
//				"CREATE VIEW Overall_Income AS SELECT DateOpened, Type, case when Type = 'limited' THEN 10 WHEN Type = 'unlimited-1' THEN 15 WHEN Type = 'unlimited-2' THEN 20 WHEN Type = 'unlimited-3' THEN 25 END AS SubFee FROM Account");
//		List<Map<String, Object>> L = jdbcTemplate.queryForList("SELECT SUM(O.SubFee) AS Sales FROM Overall_Income O WHERE O.DateOpened > " + "\'" + date + "\'");
//		jdbcTemplate.execute("DROP VIEW Overall_Income");
		jdbcTemplate.execute("DROP TABLE IF EXISTS Cost");
		jdbcTemplate.execute("CREATE TABLE Cost (AccountType varchar(255), MonthlyFee int, PRIMARY KEY(AccountType)  )");
		jdbcTemplate.update("INSERT INTO Cost VALUES('Limited', 10)");
		jdbcTemplate.update("INSERT INTO Cost VALUES('Unlimited-1', 15)");
		jdbcTemplate.update("INSERT INTO Cost VALUES('Unlimited-2', 20)");	 
		jdbcTemplate.update("INSERT INTO Cost VALUES('Unlimited-3', 25)");
		List<Map<String, Object>> L = jdbcTemplate.queryForList("SELECT SUM(C.MonthlyFee) FROM Account A, Cost C WHERE A.DateOpened > " + "\'" + date + "\' " +  "AND A.Type = C.AccountType");
		
		return L;
	}

	@RequestMapping(value = "/query/4", method = RequestMethod.GET)
	public List<Map<String, Object>> getMovieListing() {
//		jdbcTemplate.execute("DROP VIEW IF EXISTS ActorId_Movie");
//		jdbcTemplate.execute(
//				"CREATE VIEW ActorId_Movie AS SELECT M.Name, M.Type, M.DistrFee, M.NumCopies, M.Rating, A.ActorId FROM Movie M, AppearedIn A WHERE M.Id = A.MovieId");
//		List<Map<String, Object>> L = jdbcTemplate.queryForList(
//				"SELECT AM.Name, AM.Type, AM.DistrFee, AM.NumCopies, AM.Rating, A.Name AS ActorName FROM ActorId_Movie AM, Actor A WHERE AM.ActorId = A.Id");
//		jdbcTemplate.execute("DROP VIEW ActorId_Movie");
		List<Map<String, Object>> L = jdbcTemplate.queryForList("SELECT Id, Name, Type, Rating FROM Movie");
		return L;
	}

	@RequestMapping(value = "query/5a/{name}", method = RequestMethod.GET)
	public List<Map<String, Object>> getMovieRentalsByName(@PathVariable("name") String name) {
		System.out.println(name);
		return jdbcTemplate.queryForList(
				"SELECT R.AccountId, R.CustRepId, R.OrderId, M.Name FROM Movie M, Rental R WHERE M.Id = R.MovieId AND M.Name =" + "\"" + name + "\"");
	}

	@RequestMapping(value = "/query/5b/{type}", method = RequestMethod.GET)
	public List<Map<String, Object>> getMovieRentalsByType(@PathVariable("type") String type) {
		System.out.println(type);
		return jdbcTemplate.queryForList(
				"SELECT R.AccountId, R.CustRepId, R.OrderId, M.Name FROM Movie M, Rental R WHERE M.Id = R.MovieId AND M.Type = " + "\"" + type + "\"");
	}

	@RequestMapping(value = "/query/5c/{ssn}", method = RequestMethod.GET)
	public List<Map<String, Object>> getMovieRentalsByCustomer(@PathVariable("ssn") String ssn) {
		System.out.println(ssn);
		return jdbcTemplate.queryForList(
				"SELECT R.AccountId, R.CustRepId, R.OrderId, M.Name FROM Movie M, Rental R, Account A, Customer C, Person P "
						+ "WHERE M.Id = R.MovieId AND R.AccountId = A.Id AND A.Customer = C.Id AND C.Id = P.SSN AND P.SSN = " + "\"" + ssn + "\"");
	}

	@RequestMapping(value = "/query/6", method = RequestMethod.GET)
	public List<Map<String, Object>> getCustRepOrders() {
		jdbcTemplate.execute("Drop view if exists CustRepOrders");
		jdbcTemplate.execute(
				"create view CustRepOrders as SELECT R.CustRepId, COUNT(*) as NumRentals FROM Rental R GROUP BY R.CustRepId");
		List<Map<String, Object>> L = jdbcTemplate.queryForList(
				"select O.CustRepId, O.NumRentals From CustRepOrders O where NumRentals = (SELECT MAX(NumRentals) FROM CustRepOrders)");
		jdbcTemplate.execute("Drop view CustRepOrders");
		return L;
	}

	@RequestMapping(value = "/query/7", method = RequestMethod.GET)
	public List<Map<String, Object>> getMostActiveCustomers() {
		jdbcTemplate.execute("Drop view if exists MostActiveCustomers");
		jdbcTemplate.execute(
				"create view MostActiveCustomers as SELECT R.AccountId, COUNT(*) as NumOrders FROM Rental R GROUP BY R.AccountId");
		List<Map<String, Object>> L = jdbcTemplate.queryForList(
				"SELECT * FROM MostActiveCustomers GROUP BY NumOrders ORDER BY NumOrders DESC");
		//select M.AccountId, M.NumOrders From MostActiveCustomers M where NumOrders = (SELECT MAX(NumOrders) FROM MostActiveCustomers)
		jdbcTemplate.execute("Drop view MostActiveCustomers");
		return L;
	}

	@RequestMapping(value = "/query/8", method = RequestMethod.GET)
	public List<Map<String, Object>> getActivelyRentedMovie() {
//		return jdbcTemplate.queryForList(
//				"SELECT R.MovieId, COUNT(*) as NumRentals FROM Rental R GROUP BY R.MovieId ORDER BY NumRentals DESC");
		jdbcTemplate.execute("DROP VIEW IF EXISTS MovieOrder");
		jdbcTemplate.execute("CREATE VIEW MovieOrder(MovieId, NumOrders) AS SELECT R.MovieId, COUNT(R.MovieId) FROM Rental R GROUP BY R.MovieId");

		List<Map<String, Object>> L = jdbcTemplate.queryForList("SELECT M.ID, M.Name, M.RATING, O.NumOrders FROM MovieOrder O, Movie M " +
		"WHERE O.MovieId = M.ID AND O.NumOrders >= (SELECT MAX(R.NumOrders) FROM MovieOrder R)");
		jdbcTemplate.execute("Drop view MovieOrder"); 
		return L;
	}

	@RequestMapping(value = "query/9", method = RequestMethod.POST)
	public Response postOrder(@RequestBody String json) throws JSONException {
		System.out.println(json);
		JSONObject jsonObj = new JSONObject(json);
		int Id = Integer.parseInt(jsonObj.getString("accountid"));
		int CustRepId = Integer.parseInt(jsonObj.getString("custrepid"));
		int OrderId = Integer.parseInt(jsonObj.getString("orderid"));
		int MovieId = Integer.parseInt(jsonObj.getString("movieid"));
		String DateTime = jsonObj.getString("datetime");
		String ReturnDate = jsonObj.getString("returndate");
		
		DateTime = DateTime.replace("T", " ").concat(":00");
		
		// AFTER
		
		jdbcTemplate.execute("DROP VIEW IF EXISTS Currently_Held");
		jdbcTemplate.execute(
				"CREATE VIEW Currently_Held AS SELECT R.AccountId, R.MovieId, O.DateTime, O.ReturnDate FROM Rental R, `Order` O WHERE R.OrderId = O.Id AND R.AccountId = "
						+ Id);
		List<Map<String, Object>> L = jdbcTemplate
				.queryForList("SELECT AccountId, MovieId, DateTime, ReturnDate FROM Currently_Held WHERE CURDATE() < ReturnDate");
		jdbcTemplate.execute("DROP VIEW Currently_Held");
		
		int currentlyHeld = 0;
		
		for (Map<String, Object> map : L) {
			currentlyHeld += 1;
		}
		
		// CHECK ACCOUNT TYPE
		int limited = jdbcTemplate.queryForList("SELECT * FROM Account WHERE Id = " + Id + " AND Type = " + "\'limited\';").size();
		int unlimited1 = jdbcTemplate.queryForList("SELECT * FROM Account WHERE Id = " + Id + " AND Type = " + "\'unlimited-1\';").size();
		int unlimited2 = jdbcTemplate.queryForList("SELECT * FROM Account WHERE Id = " + Id + " AND Type = " + "\'unlimited-2\';").size();
		int unlimited3 = jdbcTemplate.queryForList("SELECT * FROM Account WHERE Id = " + Id + " AND Type = " + "\'unlimited-3\';").size();
		
		if (limited == 1) {
			System.out.println("HI");
			System.out.println(currentlyHeld);
			if (currentlyHeld >= 1) {
				Response response = new Response("Error", json);
				return response;
			}
			else {
				int rows = jdbcTemplate
						.update("INSERT INTO `Order` (Id, DateTime, ReturnDate) VALUES (" + OrderId + ", " + "\'"
								+ DateTime + "\', " + "\'" + ReturnDate + "\'" + ")")
						+ jdbcTemplate.update("INSERT INTO Rental(AccountId, CustRepId, OrderId, MovieId) VALUES (" + Id
								+ ", " + CustRepId + ", " + OrderId + ", " + MovieId + ")");
				if (rows >= 2) {
					Response response = new Response("Done", json);
					return response;
				} else {
					Response response = new Response("Error", json);
					return response;
				}
			}
		}
		else if (unlimited1 == 1) {
			if (currentlyHeld >= 1) {
				Response response = new Response("Error", json);
				return response;
			}
			else {
				int rows = jdbcTemplate
						.update("INSERT INTO `Order` (Id, DateTime, ReturnDate) VALUES (" + OrderId + ", " + "\'"
								+ DateTime + "\', " + "\'" + ReturnDate + "\'" + ")")
						+ jdbcTemplate.update("INSERT INTO Rental(AccountId, CustRepId, OrderId, MovieId) VALUES (" + Id
								+ ", " + CustRepId + ", " + OrderId + ", " + MovieId + ")");
				if (rows >= 2) {
					Response response = new Response("Done", json);
					return response;
				} else {
					Response response = new Response("Error", json);
					return response;
				}
			}
		}
		else if (unlimited2 == 1) {
			if (currentlyHeld >= 2) {
				Response response = new Response("Error", json);
				return response;
			}
			else {
				int rows = jdbcTemplate
						.update("INSERT INTO `Order` (Id, DateTime, ReturnDate) VALUES (" + OrderId + ", " + "\'"
								+ DateTime + "\', " + "\'" + ReturnDate + "\'" + ")")
						+ jdbcTemplate.update("INSERT INTO Rental(AccountId, CustRepId, OrderId, MovieId) VALUES (" + Id
								+ ", " + CustRepId + ", " + OrderId + ", " + MovieId + ")");
				if (rows >= 2) {
					Response response = new Response("Done", json);
					return response;
				} else {
					Response response = new Response("Error", json);
					return response;
				}
			}
		}
		else if (unlimited3 == 1) {
			if (currentlyHeld >= 3) {
				Response response = new Response("Error", json);
				return response;
			}
			else {
				int rows = jdbcTemplate
						.update("INSERT INTO `Order` (Id, DateTime, ReturnDate) VALUES (" + OrderId + ", " + "\'"
								+ DateTime + "\', " + "\'" + ReturnDate + "\'" + ")")
						+ jdbcTemplate.update("INSERT INTO Rental(AccountId, CustRepId, OrderId, MovieId) VALUES (" + Id
								+ ", " + CustRepId + ", " + OrderId + ", " + MovieId + ")");
				if (rows >= 2) {
					Response response = new Response("Done", json);
					return response;
				} else {
					Response response = new Response("Error", json);
					return response;
				}
			}
		}
		else {
			Response response = new Response("Error", json);
			return response;
		}

	}

	@RequestMapping(value = "query/10a", method = RequestMethod.POST)
	public Response postCustomer(@RequestBody String json) throws JSONException {
		System.out.println(json);
		JSONObject jsonObj = new JSONObject(json);
		String username = jsonObj.getString("username");
		String email = jsonObj.getString("email");
		String password = jsonObj.getString("password");
		String FirstName = jsonObj.getString("firstname");
		String LastName = jsonObj.getString("lastname");
		String SSN = jsonObj.getString("ssn");
		String creditcardnumber = jsonObj.getString("creditcardnumber");
		String Address = jsonObj.getString("address");
		int ZipCode = Integer.parseInt(jsonObj.getString("zipcode"));
		String Telephone = jsonObj.getString("telephone");
		String city = jsonObj.getString("city");
		String state = jsonObj.getString("state");
		int rating = Integer.parseInt(jsonObj.getString("rating"));
		String accounttype = jsonObj.getString("accounttype");
		
		Date dNow = new Date();
		SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM.dd");
		
		int rowsAffected = jdbcTemplate.queryForList("SELECT * FROM Location WHERE ZipCode = " + ZipCode).size();
		
		int rows = 0;
		
		if (rowsAffected == 1) {	// DUPLICATE EXISTS
			rows = jdbcTemplate.update("INSERT INTO Person (SSN, LastName, FirstName, Address, ZipCode, Telephone) VALUES (" + "\'"
					+ SSN + "\' " + ", " + "\'" + LastName + "\', " + "\'" + FirstName + "\', " + "\'" + Address
					+ "\', " + ZipCode + ", \'" + Telephone + "\'" + ")") +
					jdbcTemplate.update("INSERT INTO Customer (Id, Email, Rating, CreditCardNumber) VALUES (" + "\'" + SSN
					+ "\'" + ", " + "\'" + email + "\', " + rating + ", " + "\'" + creditcardnumber + "\'" + ")") + 
			jdbcTemplate.update("INSERT INTO Account (DateOpened, Type, Customer) VALUES (" + "\'" + ft.format(dNow) + "\', " + "\'" + accounttype + "\', " + "\'" + SSN + "\')") +
			jdbcTemplate.update("INSERT INTO Users(username, password) VALUES (" + "\'" + username + "\', " + "\'" + password + "\')");
		}
		else if (rowsAffected == 0) {	// NO ZipCode IN LOCATION
			rows = 	jdbcTemplate.update("INSERT INTO Location (ZipCode, City, State) VALUES (" + ZipCode + ", \'" + city + "\', " + "\'" + state + "\')") +
					jdbcTemplate.update("INSERT INTO Person (SSN, LastName, FirstName, Address, ZipCode, Telephone) VALUES (" + "\'"
					+ SSN + "\' " + ", " + "\'" + LastName + "\', " + "\'" + FirstName + "\', " + "\'" + Address
					+ "\', " + ZipCode + ", \'" + Telephone + "\'" + ")") +
			jdbcTemplate.update("INSERT INTO Customer (Id, Email, Rating, CreditCardNumber) VALUES (" + "\'" + SSN
					+ "\'" + ", " + "\'" + email + "\', " + rating + ", " + "\'" + creditcardnumber + "\'" + ")") + 
			jdbcTemplate.update("INSERT INTO Account (DateOpened, Type, Customer) VALUES (" + "\'" + ft.format(dNow) + "\', " + "\'" + accounttype + "\', " + "\'" + SSN + "\')") +
			jdbcTemplate.update("INSERT INTO Users(username, password) VALUES (" + "\'" + username + "\', " + "\'" + password + "\')");
		}
		
		if (rows >= 4) {
			Response response = new Response("Done", json);
			return response;
		}
		else {
			Response response = new Response("Error", json);
			return response;
		}

	}

	@RequestMapping(value = "query/10b", method = RequestMethod.POST)
	public Response editCustomer(@RequestBody String json) throws JSONException {
		System.out.println(json);
		JSONObject jsonObj = new JSONObject(json);
		String attr = jsonObj.getString("attr");
		String attrval = jsonObj.getString("attrval");
		String Id = jsonObj.getString("id");
		
		
		// AFTER
		
		if (attr.compareToIgnoreCase("Email") == 0 || attr.compareToIgnoreCase("Rating") == 0 || attr.compareToIgnoreCase("CreditCardNumber") == 0 ) {
			int rows = jdbcTemplate.update(
					"UPDATE Customer SET " + attr + "=" + "\'" + attrval + "\'" + " WHERE Id = " + "\'" + Id + "\'");
			Response response = new Response("Done", json);
			return response;
		}
		else if (attr.compareToIgnoreCase("DateOpened") == 0 || attr.compareToIgnoreCase("Type") == 0) {
			System.out.println("HERE");
			int rows = jdbcTemplate.update(
					"UPDATE Account SET " + attr + "=" + "\'" + attrval + "\'" + " WHERE Customer = " + "\'" + Id + "\'");
			Response response = new Response("Done", json);
			return response;
		}
		else if (attr.compareToIgnoreCase("LastName") == 0 || attr.compareToIgnoreCase("FirstName") == 0 || attr.compareToIgnoreCase("Address") == 0 || attrval.compareToIgnoreCase("Telephone") == 0) {
			int rows = jdbcTemplate.update(
					"UPDATE Person SET " + attr + "=" + "\'" + attrval + "\'" + " WHERE SSN = " + "\'" + Id + "\'");
			Response response = new Response("Done", json);
			return response;
		}
		else {
			Response response = new Response("Error", json);
			return response;
		}

	}

	@RequestMapping(value = "query/10c/{Id}", method = RequestMethod.DELETE)
	public Response deleteCustomer(@PathVariable("Id") String Id) {
		System.out.println(Id);
		int rows = jdbcTemplate.update("DELETE FROM Account WHERE Customer = " + "\'" + Id + "\'")
				+ jdbcTemplate.update("DELETE FROM Customer WHERE Id = " + "\'" + Id + "\'")
				+ jdbcTemplate.update("DELETE FROM Person WHERE SSN = " + "\'" + Id + "\'");
		if (rows >= 2) {
			Response response = new Response("Done", Id);
			return response;
		}
		else {
			Response response = new Response("Error", Id);
			return response;
		}
	}

	@RequestMapping(value = "query/11", method = RequestMethod.GET)
	public List<Map<String, Object>> getMailingList() {
		return jdbcTemplate.queryForList("select P.SSN, P.FirstName, P.LastName, P.Address, C.Email FROM Person P, Customer C WHERE P.SSN = C.Id");
	}
	
	// THIS ONE
	@RequestMapping(value = "/query/12/{Id}", method = RequestMethod.GET)
	public List<Map<String, Object>> getMovieSuggesion(@PathVariable("Id") String Id) {
		System.out.println(Id);
		jdbcTemplate.execute("DROP VIEW IF EXISTS PastOrder");
		jdbcTemplate.execute("CREATE VIEW PastOrder(CustId, MovieId, MovieType) AS SELECT A.Customer, R.MovieId, M.Type FROM Account A, Rental R, Movie M WHERE A.Id = R.AccountId AND R.MovieId = M.Id");
		List<Map<String, Object>> L = jdbcTemplate.queryForList("SELECT M.Id, M.Name, M.Type FROM Movie M WHERE M.Type IN (SELECT O.MovieType FROM PastOrder O" +
				" WHERE O.CustId = " + "\'" + Id + "\')" + 
		" AND M.Id NOT IN (SELECT O.MovieId FROM PastOrder O" +
			     " WHERE O.CustId = " + "\'" + Id + "\')");
		jdbcTemplate.execute("DROP VIEW PastOrder");
		return L;
	}

	@RequestMapping(value = "query/13/{accountid}", method = RequestMethod.GET)
	public List<Map<String, Object>> getCurrentlyHeld(@PathVariable("accountid") int accountid) {
		System.out.println(accountid);
		jdbcTemplate.execute("DROP VIEW IF EXISTS Currently_Held");
		jdbcTemplate.execute(
				"CREATE VIEW Currently_Held AS SELECT R.AccountId, R.MovieId, O.DateTime, O.ReturnDate FROM Rental R, `Order` O WHERE R.OrderId = O.Id AND R.AccountId = "
						+ accountid);
		List<Map<String, Object>> L = jdbcTemplate
				.queryForList("SELECT AccountId, MovieId, DateTime, ReturnDate FROM Currently_Held WHERE CURDATE() < ReturnDate");
		jdbcTemplate.execute("DROP VIEW Currently_Held");
		return L;
	}
	
	// CHANGE TO SSN 
	@RequestMapping(value = "query/14/{accountid}", method = RequestMethod.GET)
	public List<Map<String, Object>> getMovieQueue(@PathVariable("accountid") int accountid) {
		System.out.println(accountid);
		return jdbcTemplate.queryForList("SELECT * FROM MovieQ where AccountId = " + accountid);
	}
	
	@RequestMapping(value = "query/14b", method = RequestMethod.POST)
	public Response postMovieQueue(@RequestBody String json) throws JSONException {
		System.out.println(json);
		JSONObject jsonObj = new JSONObject(json);
		int accountid = Integer.parseInt(jsonObj.getString("accountid"));
		int movieid = Integer.parseInt(jsonObj.getString("movieid"));
		
		int affectedRows = jdbcTemplate.queryForList("SELECT * FROM Movie WHERE Id = " + movieid + " AND NumCopies > 0").size();
		
		if (affectedRows == 1) {
			int rows =	jdbcTemplate.update("INSERT INTO MovieQ (AccountId, MovieId) VALUES (" + accountid + ", " + movieid +")") +
						jdbcTemplate.update("UPDATE Movie SET NumCopies = NumCopies - 1 WHERE Id = " + movieid);
			
			if (rows >= 2) {
				Response response = new Response("Done", json);
				return response;
			}
			else {
				Response response = new Response("Error", json);
				return response;
			}
		}
		else {
			Response response = new Response("Error", json);
			return response;
		}
		
	
	}

	@RequestMapping(value = "query/15/{ssn}", method = RequestMethod.GET)
	public List<Map<String, Object>> getCustomerSettings(@PathVariable("ssn") String ssn) {
		System.out.println(ssn);
		return jdbcTemplate.queryForList("SELECT A.Id, A.DateOpened, A.Type, C.Email, C.CreditCardNumber FROM Customer C, Account A where A.Customer = C.Id AND C.Id = " + "\"" + ssn + "\"");
	}
	
	@RequestMapping(value = "query/16/{accountid}", method = RequestMethod.GET)
	public List<Map<String, Object>> getCustOrders(@PathVariable("accountid") int accountid) {
		System.out.println(accountid);
		return jdbcTemplate.queryForList(
				"SELECT R.AccountId, R.MovieId, O.DateTime, O.ReturnDate FROM Rental R, `Order` O WHERE R.OrderId = O.Id AND R.AccountId = "
						+ accountid);
	}

	@RequestMapping(value = "query/17/{Type}", method = RequestMethod.GET)
	public List<Map<String, Object>> getMovieTypeAvail(@PathVariable("Type") String Type) {
		System.out.println(Type);
		return jdbcTemplate
				.queryForList("SELECT * FROM Movie where Type = " + "\'" + Type + "\'" + " AND NumCopies > 0");
	}

	@RequestMapping(value = "query/18/{keyword}", method = RequestMethod.GET)
	public List<Map<String, Object>> getSearchedMovies(@PathVariable("keyword") String keyword) {
		System.out.println(keyword);
		return jdbcTemplate
				.queryForList("SELECT * FROM Movie where Name REGEXP " + "\'" + keyword + "\'" + " AND NumCopies > 0");
	}

	@RequestMapping(value = "query/19/{actorname}", method = RequestMethod.GET)
	public List<Map<String, Object>> getMoviesStarringActor(@PathVariable("actorname") String actorname) {
		System.out.println(actorname);
		jdbcTemplate.execute("DROP VIEW IF EXISTS ActorId_Movie");
		jdbcTemplate.execute("DROP VIEW IF EXISTS Actors_Movie");
		jdbcTemplate.execute(
				"CREATE VIEW ActorId_Movie AS SELECT M.Name, M.Type, M.DistrFee, M.NumCopies, M.Rating, A.ActorId FROM Movie M, AppearedIn A WHERE M.Id = A.MovieId");
		jdbcTemplate.execute(
				"CREATE VIEW Actors_Movie AS SELECT AM.Name AS MovieName, AM.Type, AM.DistrFee, AM.NumCopies, AM.Rating, A.Name AS ActorName FROM ActorId_Movie AM, Actor A WHERE AM.ActorId = A.Id");
		List<Map<String, Object>> L = jdbcTemplate.queryForList(
				"SELECT * FROM Actors_Movie WHERE NumCopies > 0 AND ActorName = " + "\'" + actorname + "\'");
		jdbcTemplate.execute("DROP VIEW ActorId_Movie");
		jdbcTemplate.execute("DROP VIEW Actors_Movie");
		return L;
	}

	@RequestMapping(value = "query/20", method = RequestMethod.GET)
	public List<Map<String, Object>> getBestSeller() {
//		return jdbcTemplate.queryForList(
//				"SELECT R.MovieId, M.Name, COUNT(*) AS NumRentals FROM Rental R, Movie M WHERE M.Id = R.MovieId GROUP BY R.MovieId ORDER BY NumRentals DESC");
		jdbcTemplate.execute("DROP VIEW IF EXISTS MovieOrder");
		jdbcTemplate.execute("CREATE VIEW MovieOrder(MovieId, NumOrders) AS SELECT R.MovieId, COUNT(R.MovieId) FROM Rental R GROUP BY R.MovieId");

		List<Map<String, Object>> L = jdbcTemplate.queryForList("SELECT M.Id, M.Name, M.Type, M.Rating, N.NumOrders FROM MovieOrder N, Movie M " +
				"WHERE N.MovieId = M.Id ORDER BY N.NumOrders DESC");
		jdbcTemplate.execute("Drop view MovieOrder"); 
		return L;
	}

	// THIS ONE
	@RequestMapping(value = "/query/21/{accountid}", method = RequestMethod.GET)
	public List<Map<String, Object>> getPersonalSuggest(@PathVariable("accountid") String accountid) {
		System.out.println(accountid);
		jdbcTemplate.execute("DROP VIEW IF EXISTS PastOrder");
		jdbcTemplate.execute("CREATE VIEW PastOrder(CustId, MovieId, MovieType) AS SELECT A.Customer, R.MovieId, M.Type FROM Account A, Rental R, Movie M WHERE A.Id = R.AccountId AND R.MovieId = M.Id");
		List<Map<String, Object>> L = jdbcTemplate.queryForList("SELECT M.Id, M.Name, M.Type FROM Movie M WHERE M.Type IN (SELECT O.MovieType FROM PastOrder O" +
				" WHERE O.CustId = " + "\'" + accountid + "\')" + 
		" AND M.Id NOT IN (SELECT O.MovieId FROM PastOrder O" +
			     " WHERE O.CustId = " + "\'" + accountid + "\')");
		jdbcTemplate.execute("DROP VIEW PastOrder");
		return L;
	}

	@RequestMapping(value = "query/22", method = RequestMethod.POST)
	public Response editMovieRating(@RequestBody String json) throws JSONException {
		System.out.println(json);
		JSONObject jsonObj = new JSONObject(json);
		int rating = Integer.parseInt(jsonObj.getString("rating"));
		String moviename = jsonObj.getString("moviename");
		int rows = jdbcTemplate.update("UPDATE Movie SET Rating =" + rating + " WHERE Name = " + "\'" + moviename + "\'");
		

		if (rows >= 1) {
			Response response = new Response("Done", json);
			return response;
		}
		else {
			Response response = new Response("Error", json);
			return response;
		}
		
	}
	
	@RequestMapping(value = "query/23a", method = RequestMethod.POST)	
	public List<Map<String, Object>> postGetQuery(@RequestBody String json) throws JSONException {
		System.out.println(json);
		JSONObject jsonObj = new JSONObject(json);
		String query = jsonObj.getString("query");
		List<Map<String, Object>> L = jdbcTemplate.queryForList(query);
		return L;
	}
	
	@RequestMapping(value = "query/23b", method = RequestMethod.POST)		
	public Response postPostQuery(@RequestBody String json) throws JSONException {
		System.out.println(json);
		JSONObject jsonObj = new JSONObject(json);
		String query = jsonObj.getString("query");
		
		String mainStatement = query.substring(0, query.indexOf(' '));
		
		int rows = 0;
		
		if (mainStatement.compareToIgnoreCase("UPDATE") == 0) {
			rows = jdbcTemplate.update(query);
		}
		else {
			jdbcTemplate.execute(query);
			rows += 1;
		}
		
		//execute, update
		if (rows >= 1) {
			Response response = new Response("Done", json);
			return response;
		}
		else {
			Response response = new Response("Error", json);
			return response;
		}
		
	}
	


}