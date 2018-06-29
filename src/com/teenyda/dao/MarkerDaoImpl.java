package com.teenyda.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.teenyda.bean.Marker;
import com.teenyda.util.DBUtil;

public class MarkerDaoImpl implements MarkerDao{

	@Override
	public List<Marker> getMarkers() {
		// TODO Auto-generated method stub
		Connection conn = DBUtil.getConnection();
//		String sql = "select * from markers";
		String sql = "select m.id,m.title,m.iconPath,m.latitude,m.longitude,m.width,m.height "
				+ "from look l "
				+ "LEFT JOIN markers m  "
				+ "ON l.lookid = m.id "
				+ "WHERE l.state = 1 OR l.state = 2";
		PreparedStatement prep = null;
		ResultSet rs = null;
		List<Marker> markers = new ArrayList<Marker>();
		try {
			prep = conn.prepareStatement(sql);
			rs = prep.executeQuery();
			while(rs.next()){
				Marker marker = new Marker();
				int id = rs.getInt("id");
				String title = rs.getString("title");
				String iconPath = rs.getString("iconPath");
				String latitude = rs.getString("latitude");
				String longitude = rs.getString("longitude");
				String width = rs.getString("width");
				String height = rs.getString("height");
				marker.setId(id);
				marker.setTitle(title);
				marker.setIconPath(iconPath);
				marker.setLatitude(latitude);
				marker.setLongitude(longitude);
				marker.setWidth(width);
				marker.setHeight(height);
				markers.add(marker);
			}
			return markers;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs, prep, conn);
		}
		return null;
	}

	@Override
	public int markersCount() {
		// TODO Auto-generated method stub
		Connection conn = DBUtil.getConnection();
		String sql = "select count(*) from markers";
		PreparedStatement prep = null;
		int count = -1;
		ResultSet rs = null;
		try {
			prep = conn.prepareStatement(sql);
			rs = prep.executeQuery();
			if (rs.next()){
				count = rs.getInt(1);
			}
			return count;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(rs, prep, conn);
		}
		return count;
	}

	@Override
	public void updateMarker(int state,int lookid) {
		// TODO Auto-generated method stub
		Connection conn = DBUtil.getConnection();
		String sql = "update look set state = ? where lookid = ?";
		PreparedStatement prep = null;
		try {
			prep = conn.prepareStatement(sql);
			prep.setInt(1, state);
			prep.setInt(2, lookid);
			prep.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, prep, conn);
		}
	}

	@Override
	public void updateIcon(int state, int lookid) {
		// TODO Auto-generated method stub
		Connection conn = DBUtil.getConnection();
		String sql = "";
		if (state == 1) {
			sql = "update markers set iconPath = '/images/idle.png' where id = ?";
		}
		if (state == 2) {
			sql = "update markers set iconPath = '/images/not.png' where id = ?";
		}
		PreparedStatement prep = null;
		try {
			prep = conn.prepareStatement(sql);
			prep.setInt(1, lookid);
			prep.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			DBUtil.close(null, prep, conn);
		}
	}

	@Override
	public boolean addMarkers(Marker marker,int state) {
		// TODO Auto-generated method stub
		Connection conn = DBUtil.getConnection();
		String sql = "insert into markers(id,title,iconPath,latitude,longitude,width,height) values(?,?,?,?,?,?,?)";
		PreparedStatement prep = null;
		try {
			prep = conn.prepareStatement(sql);
			prep.setInt(1, marker.getId());
			prep.setString(2, marker.getTitle());
			if (state == 1) {
				prep.setString(3, "/images/idle.png");
			}else {
				prep.setString(3, "/images/not.png");
			}
			prep.setString(4, marker.getLatitude());
			prep.setString(5, marker.getLongitude());
			prep.setString(6, "45");
			prep.setString(7, "50");
			int rs = prep.executeUpdate();
			if (rs > 0) {
				return true;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBUtil.close(null, prep, conn);
		}
		return false;
	}

	@Override
	public void removeMarker(int lookid) {
		// TODO Auto-generated method stub
		Connection conn = DBUtil.getConnection();
		String sql = "delete from markers where id = ?";
		PreparedStatement prep = null;
		try {
			prep = conn.prepareStatement(sql);
			prep.setInt(1, lookid);
			prep.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}  finally {
			DBUtil.close(null, prep, conn);
		}
	}

}
