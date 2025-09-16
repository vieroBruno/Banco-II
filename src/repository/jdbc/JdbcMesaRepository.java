package repository.jdbc;

import exception.RepositoryException;
import model.Mesa;
import repository.MesaRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class JdbcMesaRepository implements MesaRepository {

	@Override
	public void save(Mesa mesa) {
		String query = "INSERT INTO MESAS";

		try {
			Connection con = new Conexao().getConnection();
			PreparedStatement st  = con.prepareStatement(query);
			st.execute();
			st.close();
			System.out.println("Mesa cadastrado com sucesso!");
		} catch (SQLException e) {
			throw new RepositoryException("Erro ao salvar mesa", e);
		}
	}

	@Override
	public void update(Mesa mesa){
		String query = "UPDATE MESAS SET  WHERE =?";

		try {
			Connection con = new Conexao().getConnection();
			PreparedStatement st  = con.prepareStatement(query);

			st.execute();
			st.close();
			System.out.println("Mesa alterada com sucesso!");
		} catch (SQLException e) {
			throw new RepositoryException("Erro ao alterar mesa", e);
		}
	}

	@Override
	public void delete(int id_mesa){
		String query= "DELETE FROM MESAS WHERE  =?";

		try {
			Connection con = new Conexao().getConnection();
			PreparedStatement st  = con.prepareStatement(query);

			st.execute();
			st.close();
			System.out.println("Mesa excluída com sucesso!");
		} catch (SQLException e) {
			throw new RuntimeException("Erro ao excluir mesa",e);
		}
	}

	@Override
	public Mesa findById(int id_mesa) {
		return null;
	}


	@Override
	public List<Mesa> listAll() {
		String query = "SELECT  FROM MESAS";
		List<Mesa> mesas = new ArrayList<>();

		try {
			Connection con = new Conexao().getConnection();
			Statement st = con.createStatement();
			ResultSet result = st.executeQuery(query);
			while(result.next()) {
				//
				//Mesa.add(new Mesa(
				//	result.getInt(1),
				//
				//));
			}
		} catch (SQLException e) {
			throw  new RuntimeException("Erro ao listar mesas", e);
		}
		return mesas;
	}
}
