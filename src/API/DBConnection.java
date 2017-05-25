package API;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DBConnection {
	private Connection c = null;
	private Statement stmt = null;
	private Statement picRs = null;
	public DBConnection() throws ClassNotFoundException {
		try {
			Class.forName("org.sqlite.JDBC");
			String url = "jdbc:sqlite:C:/Users/alierdem.akin/workspace/WSdemo/WebContent/WEB-INF/data/YazLabIV.db";

			this.c = DriverManager.getConnection(url);
			this.stmt = this.c.createStatement();
			this.picRs = this.c.createStatement();

		} catch (SQLException e) {
		}
	}

	public void closeConnection() throws SQLException {
		this.stmt.close();
		this.picRs.close();
		this.c.close();
	}

	public List<Home> getAllHome() throws SQLException {
		ResultSet rs = this.stmt.executeQuery("SELECT * FROM tblEV ORDER BY evID DESC;");
		List<Home> listHome = new ArrayList<Home>();
		while (rs.next()) {
			Home home = new Home();
			home.setEvID(String.valueOf(rs.getInt("evID")));
			home.setEvIL(rs.getString("evIL"));
			home.setEvEmlakTip(rs.getString("evEmlakTip"));
			home.setEvAlan(String.valueOf(rs.getInt("evAlan")));
			home.setEvOdaSayisi(String.valueOf(rs.getInt("evOdaSayisi")));
			home.setEvBinaYasi(String.valueOf(rs.getInt("evBinaYasi")));
			home.setEvBulKat(String.valueOf(rs.getInt("evBulKat")));
			home.setEvFiyat(String.valueOf(rs.getFloat("evFiyat")));
			home.setEvAciklama(rs.getString("evAciklama"));
			
			
			ResultSet picStmt = picRs.executeQuery("SELECT * FROM tblRESIM WHERE resimEvID = "+String.valueOf(rs.getInt("evID"))+";");
			List<Picture> listPicture = new ArrayList<Picture>();
			while(picStmt.next()){
				Picture picture = new Picture();
				picture.setResimID(String.valueOf(picStmt.getInt("resimID")));
				picture.setResimYol(picStmt.getString("resimYol"));
				picture.setResimEvID(String.valueOf(picStmt.getInt("resimEvID")));
				listPicture.add(picture);
			}
			home.setPictures(listPicture);
			listHome.add(home);
		}
		this.closeConnection();
		return listHome;
	}

	public Home getOneHome(String evID) throws SQLException {
		ResultSet ps = this.picRs.executeQuery("SELECT * FROM tblRESIM WHERE resimEvID = " + evID + ";");

		Home home = new Home();
		List<Picture> homePictures = new ArrayList<Picture>();

		while (ps.next()) {
			Picture pic = new Picture();
			pic.setResimID(String.valueOf(ps.getInt("resimID")));
			pic.setResimYol(ps.getString("resimYol"));
			pic.setResimEvID(String.valueOf(ps.getInt("resimEvID")));
			homePictures.add(pic);
		}
		home.setPictures(homePictures);
		
		ResultSet rs = this.stmt.executeQuery("SELECT * FROM tblEV WHERE evID = " + evID + ";");
		while (rs.next()) {
			home.setEvID(String.valueOf(rs.getInt("evID")));
			home.setEvIL(rs.getString("evIL"));
			home.setEvEmlakTip(rs.getString("evEmlakTip"));
			home.setEvAlan(String.valueOf(rs.getInt("evOdaSayisi")));
			home.setEvOdaSayisi(String.valueOf(rs.getInt("evOdaSayisi")));
			home.setEvBinaYasi(String.valueOf(rs.getInt("evBinaYasi")));
			home.setEvBulKat(String.valueOf(rs.getInt("evBulKat")));
			home.setEvFiyat(String.valueOf(rs.getFloat("evFiyat")));
			home.setEvAciklama(rs.getString("evAciklama"));
		}
		this.closeConnection();
		return home;
	}

	public void insertHome(Home home) throws SQLException {
		String sql = "INSERT INTO tblEV (evIL,evEmlakTip,evAlan,evOdaSayisi,evBinaYasi,evBulKat,evFiyat,evAciklama)"
				+ "Values ('"+ home.getEvIL() + "','" + home.getEvEmlakTip() + "',"
				+ home.getEvAlan() + "," + home.getEvOdaSayisi() + "," + home.getEvBinaYasi() + "," + home.getEvBulKat()
				+ "," + home.getEvFiyat() + ",'" + home.getEvAciklama() + "');";
		this.stmt.executeUpdate(sql);
		ResultSet key = this.stmt.executeQuery("SELECT last_insert_rowid() AS lastID;");
		int evID = 0;
		while (key.next())
		{
			evID = key.getInt("lastID");
		}
		List<Picture> listPic = home.getPictures();
		for (Picture pic : listPic) {
			String picSql = "INSERT INTO tblRESIM (resimYol,resimEvID)" + "Values ( '"
					+ pic.getResimYol() + "'," + evID+ ");";
			this.picRs.executeUpdate(picSql);
			
		}
		this.closeConnection();
	}

	public void updateHome(Home home) throws SQLException {
		try
		{
			String sql = "UPDATE tblEV SET " + " evID = " + home.getEvID() + ", evIL = '" + home.getEvIL() + "', evEmlakTip = '"
					+ home.getEvEmlakTip() + "', evAlan = " + home.getEvAlan() + ", evOdaSayisi = " + home.getEvOdaSayisi()
					+ ", evBinaYasi = " + home.getEvBinaYasi() + ", evBulKat = " + home.getEvBulKat() + ", evFiyat = "
					+ home.getEvFiyat() + ", evAciklama = '" + home.getEvAciklama() + "' WHERE evID = " + home.getEvID() + ";";
			
			this.stmt.executeUpdate(sql);
			
			List<Picture> listPic = home.getPictures();

			for (Picture pic : listPic) {
				String picSql = "UPDATE tblRESIM SET resimID = " + pic.getResimID() + ", resimYol = '" + pic.getResimYol()
						+ "', resimEvID = " + pic.getResimEvID() + " WHERE resimID = " + pic.getResimID() + ";";
				this.picRs.executeUpdate(picSql);
			}
		}
		catch(Exception ex){
			ex.getMessage();
		}

		this.closeConnection();
	}

	public void deleteHome(Home home) throws SQLException {
		try{
			String sql = "DELETE FROM tblEV WHERE evID = " + home.getEvID() + ";";
			this.stmt.executeQuery(sql);

			String picSql = "DELETE FROM tblRESIM WHERE resimEvID = " + home.getEvID() + ";";
			this.picRs.executeUpdate(picSql);
		}catch(Exception ex){
			ex.getMessage();
		}
		

		this.closeConnection();
	}

}
