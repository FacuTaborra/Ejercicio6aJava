package UI;

import java.sql.*;
import java.util.LinkedList;
import javax.swing.*;

import java.util.Objects;
import java.util.InputMismatchException;
import com.mysql.cj.xdevapi.Result;

import entitis.*;

public class Principal {

	public static void main(String[] args) {

		// registerConection
		try {
			Class.forName("com.mysql.cj.jdbc.Driver").newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			e.printStackTrace();
		}

		menuPrincipal();
	}

	private static void menuPrincipal() {
		Boolean salir = false;
		while (!salir) {
			JOptionPane.showMessageDialog(null, "1) Listar productos\n2) Buscar producto\n3) Nuevo producto\n"
					+ "4) Eliminar producto\n5) Modificar producto\n0) Salir ");
			String op = JOptionPane.showInputDialog("Ingrese una opcion: ");
			try {
				switch (op) {
				case "0":
					salir = true;
					break;

				case "1":
					todosLosProductos();
					break;

				case "2":
					buscarProducto();
					break;

				case "3":
					altaProducto();
					break;

				case "4":
					bajaProducto();
					break;

				case "5":
					actualizaProducto();
					break;

				default:
					JOptionPane.showMessageDialog(null, "Las opciones son entre 0 y 5", null,
							JOptionPane.ERROR_MESSAGE);
				}

			} catch (NullPointerException e) {
				JOptionPane.showMessageDialog(null,"Debes introducir un valor.", null,
						JOptionPane.ERROR_MESSAGE);
			}

		}
	}

	private static void todosLosProductos() {

		Connection conn = null;

		LinkedList<Product> products = new LinkedList<>();

		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/javaMarket", "root", "facu2323");

			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("select * from Product");

			while (rs.next()) {

				Product unProducto = new Product(rs.getInt("id_product"), rs.getString("name"),
						rs.getString("description"), rs.getFloat("price"), rs.getInt("stock"),
						rs.getBoolean("shippingIncluded"));
				products.add(unProducto);

			}

			if (rs != null) {
				rs.close();
			}
			if (stmt != null) {
				stmt.close();
			}
			conn.close();

		} catch (SQLException ex) {
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}

		System.out.println("Id\tName\tPrice\tStock\tshippingIncluded\tDescription");
		System.out.println("------------------------------------------------------------------------------------------");

		for (Product unProducto : products) {
			System.out.println(unProducto.getId_product()+"\t"+ unProducto.getName()+"\t"+ unProducto.getPrice()+"\t"+unProducto.getStock()+"\t"+unProducto.getShippingIncluded()+ "\t" +unProducto.getDescription());
		}

	}

	private static void buscarProducto(){
		
		Connection conn = null;
		
		try {
			
			conn = DriverManager.getConnection("jdbc:mysql://localhost/javaMarket","root","facu2323"); //levantamos la coneccion
			
			/*PreparedStatement stmt = conn.prepareStatement("select * from Product where nombre like %?% "); // armamos Query con el nombre
			stmt.setString(1, JOptionPane.showInputDialog("Ingrese el nombre de un producto: "));*/
			
			PreparedStatement stmt = conn.prepareStatement("select * from Product where id_Product=?"); // armamos Query con id
			stmt.setInt(1, Integer.parseInt(JOptionPane.showInputDialog("Ingrese el id del producto: ")));
			
			ResultSet rs = stmt.executeQuery(); //ejecuta query
			
			Product unProducto = new Product();
			
			if(rs.next()) {
				unProducto = new Product(rs.getInt("id_product"), rs.getString("name"),rs.getString("description"), rs.getFloat("price"), rs.getInt("stock"),rs.getBoolean("shippingIncluded"));
			}
			
			if(rs!=null){rs.close();}
            if(stmt!=null){stmt.close();}
			conn.close();
			
			if(unProducto.getName() != null) {
				System.out.println("Id \t Name \t Description \t  Price \t  Stock \t shippingIncluded");
				System.out.println("------------------------------------------------------------------------------------------");
				System.out.println(unProducto.getId_product()+"\t"+ unProducto.getName()+"\t \t"+ unProducto.getDescription()+"\t\t"+ unProducto.getPrice()+"\t\t"+unProducto.getStock()+"\t\t"+unProducto.getShippingIncluded());
			}else{
				JOptionPane.showMessageDialog(null, "id no existe");
			}
			
			
		}catch(SQLException ex){
			System.out.println("SQLException: " + ex.getMessage());
		    System.out.println("SQLState: " + ex.getSQLState());
		    System.out.println("VendorError: " + ex.getErrorCode());	
		}
		
	}
	
	private static void altaProducto(){
		Connection conn = null;
		
		Product productoNuevo = new Product();
		productoNuevo.setName(JOptionPane.showInputDialog("Ingrese el nombre del producto: "));
		productoNuevo.setDescription(JOptionPane.showInputDialog("Ingrese la descripcion del producto: "));
		productoNuevo.setPrice(Float.parseFloat(JOptionPane.showInputDialog("Ingrese el precio del producto: ")));
		productoNuevo.setStock(Integer.parseInt(JOptionPane.showInputDialog("Ingrese el precio del producto: ")));
		int shipping = JOptionPane.showConfirmDialog(null, "¿Envío incluido?: ");
		
		if (shipping == 1) {
			productoNuevo.setShippingIncluded(false);
		}else{
			productoNuevo.setShippingIncluded(true);
		}
		
		try {
			
			conn = DriverManager.getConnection("jdbc:mysql://localhost/javaMarket","root","facu2323"); //levantamos la coneccion

			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Product(name,description,price,stock,shippingIncluded) values (?,?,?,?,?)",PreparedStatement.RETURN_GENERATED_KEYS);
			
			pstmt.setString(1, productoNuevo.getName());
			pstmt.setString(2, productoNuevo.getDescription());
			pstmt.setDouble(3, productoNuevo.getPrice());
			pstmt.setInt(4, productoNuevo.getStock());
			pstmt.setBoolean(5, productoNuevo.getShippingIncluded());
			
			pstmt.executeUpdate();
			
			//Obtengo el id autoincremental de la DB
			ResultSet keyResultSet = pstmt.getGeneratedKeys();
			
			if(keyResultSet!=null && keyResultSet.next()){
	        	 int id = keyResultSet.getInt(1);
	        	 productoNuevo.setId_product(id);
	         }

	         // Cerrar conexion
		         if(keyResultSet!=null){keyResultSet.close();}
		         if(pstmt!=null){pstmt.close();}
		         conn.close();
		         
		         JOptionPane.showMessageDialog(null, "Registro agregado con exito");
			
		}catch(SQLException ex){
			 System.out.println("SQLException: " + ex.getMessage());
			 System.out.println("SQLState: " + ex.getSQLState());
			 System.out.println("VendorError: " + ex.getErrorCode());
		}
		
	}
	
	private static void bajaProducto(){
		Connection conn = null;
		
		try {
			conn = DriverManager.getConnection("jdbc:mysql://localhost/javaMarket","root","facu2323"); //levantamos la coneccion
			PreparedStatement stmt = conn.prepareStatement("DELETE FROM Product WHERE idProduct=?", PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, Integer.parseInt(JOptionPane.showInputDialog("Ingrese el id del producto que desea eliminar")));
			stmt.executeUpdate();
			if(stmt!=null){stmt.close();}
			conn.close();
			JOptionPane.showMessageDialog(null, "Registro eliminado con exito");

		}catch(SQLException ex){
			 System.out.println("SQLException: " + ex.getMessage());
			 System.out.println("SQLState: " + ex.getSQLState());
			 System.out.println("VendorError: " + ex.getErrorCode());
		}
		
	}
	
	private static void actualizaProducto(){
		
		Connection conn = null;
		
		try {
			//Buscamos producto
			conn = DriverManager.getConnection("jdbc:mysql://localhost/javaMarket","root","facu2323"); //levantamos la coneccion
			
			PreparedStatement stmt = conn.prepareStatement("select * from Product where id_Product=?"); // armamos Query con id
			stmt.setInt(1, Integer.parseInt(JOptionPane.showInputDialog("Ingrese el id del producto: ")));
			
			ResultSet rs = stmt.executeQuery(); //ejecuta query
			
			Product dbProducto = new Product();
			
			if(rs.next()) {
				dbProducto = new Product(rs.getInt("id_product"), rs.getString("name"),rs.getString("description"), rs.getFloat("price"), rs.getInt("stock"),rs.getBoolean("shippingIncluded"));
			}
			
			if(dbProducto.getName() != null) {
				System.out.println("Id \t Name \t Description \t  Price \t  Stock \t shippingIncluded");
				System.out.println("------------------------------------------------------------------------------------------");
				System.out.println(dbProducto.getId_product()+"\t"+ dbProducto.getName()+"\t \t"+ dbProducto.getDescription()+"\t\t"+ dbProducto.getPrice()+"\t\t"+dbProducto.getStock()+"\t\t"+dbProducto.getShippingIncluded());
			}else{
				JOptionPane.showMessageDialog(null, "id no existe");
			}
			
			//Actualizamos producto
			
			JOptionPane.showInputDialog("Ingrese los datos nuevos del productio: ");
			
			dbProducto.setName(JOptionPane.showInputDialog("Ingrese el nombre del producto: "));
			dbProducto.setDescription(JOptionPane.showInputDialog("Ingrese descripcion: "));
			dbProducto.setPrice(Float.parseFloat(JOptionPane.showInputDialog("Ingrese el precio: ")));
			dbProducto.setStock(Integer.parseInt(JOptionPane.showInputDialog("Ingrese el stock: ")));
			int shipping = JOptionPane.showConfirmDialog(null, "¿Envío incluido?: ");
			if (shipping == 1){
				dbProducto.setShippingIncluded(false);
			}else{
				dbProducto.setShippingIncluded(true);
			}
			
			PreparedStatement pstmUpdate = conn.prepareStatement("UPDATE Product SET name=?, description=?, price=?, stock=?, shippingIncluded=? WHERE id_product=?", PreparedStatement.RETURN_GENERATED_KEYS);
			
			pstmUpdate.setString(1, dbProducto.getName());
			pstmUpdate.setString(2, dbProducto.getDescription());
			pstmUpdate.setDouble(3, dbProducto.getPrice());
			pstmUpdate.setInt(4, dbProducto.getStock());
			pstmUpdate.setBoolean(5, dbProducto.getShippingIncluded());
			pstmUpdate.setInt(6, dbProducto.getId_product());
			 
			pstmUpdate.executeUpdate();
			
			if(rs!=null){rs.close();}
            if(stmt!=null){stmt.close();}
			conn.close();
			
		}catch(SQLException ex){
			System.out.println("SQLException: " + ex.getMessage());
			System.out.println("SQLState: " + ex.getSQLState());
			System.out.println("VendorError: " + ex.getErrorCode());
		}
		
	}
	

};

