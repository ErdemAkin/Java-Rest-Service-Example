package API;

import java.sql.SQLException;
import java.util.List;

import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.json.JSONException;

import com.google.gson.Gson;
 
@Path("/homeinfo")
public class HomeInfo {
	
	
	@POST
	@Path("/getAllHome")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getAllHome() throws JSONException, SQLException, ClassNotFoundException
	{
		DBConnection db = new DBConnection();
		Gson gson = new Gson();
		List<Home> homeList = db.getAllHome();
		String result = gson.toJson(homeList);
		return Response.status(200).entity(result).build();
	}
	
	@POST
	@Path("/getOneHome")
	@Produces(MediaType.APPLICATION_JSON)
	public String getOneHome(@HeaderParam("id") String id) throws SQLException, ClassNotFoundException{
		DBConnection db = new DBConnection();
		Gson gson = new Gson();
		Home home = db.getOneHome(id);
		return gson.toJson(home);
	}
	
	@POST
	@Path("/insertHome")
	@Produces(MediaType.TEXT_PLAIN)
	public Response insertHome(@HeaderParam("home") String param) throws ClassNotFoundException, SQLException{
		DBConnection db = new DBConnection();
		Gson gson = new Gson();
		Home home = gson.fromJson(param, Home.class);
		db.insertHome(home);
		return Response.status(200).entity("OK").build();
	}
	
	@POST
	@Path("/updateHome")
	@Produces(MediaType.TEXT_PLAIN)
	public Response updateHome(@HeaderParam("home") String param) throws ClassNotFoundException, SQLException{
		DBConnection db = new DBConnection();
		Gson gson = new Gson();
		Home home = gson.fromJson(param, Home.class);
		db.updateHome(home);
		return Response.status(200).entity("OK").build();
	}
	
	@POST
	@Path("/deleteHome")
	@Produces(MediaType.TEXT_PLAIN)
	public Response deleteHome(@HeaderParam("home") String param) throws ClassNotFoundException, SQLException{
		DBConnection db = new DBConnection();
		Gson gson = new Gson();
		Home home = gson.fromJson(param, Home.class);
		db.deleteHome(home);
		return Response.status(200).entity("OK").build();
	}
}
