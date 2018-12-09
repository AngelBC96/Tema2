package tema2;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MainActivity {

    // Atributos
    private Connection connection;

    // Constructor 
    public MainActivity() {
        ConnectionHandler ch = new ConnectionHandler();
        this.connection = ch.doConnection();
        this.createTables();
        this.insertTuple(new Article(1, "test"));
        this.insertTuple(new Article(2, "test 2"));
        this.insertTuple(new Article(3, "test 3"));
        this.updateTuple(new Article(3, "test 4"));
        this.deleteTuple(new Article(3, "test 4"));
        this.getArticle(1);
        this.getArticles("test");
        this.showList();
        this.dropTables();
    }

    public static void main(String[] args) {
        MainActivity activity = new MainActivity();
    }

    // Metodos
    public void executeSQLQuery(String sqlQuery) {
        try {
            Statement statement = this.connection.createStatement();
            statement.executeUpdate(sqlQuery);
            System.out.println("Operació correcta : " + sqlQuery);
        } catch (SQLException e) {
            System.out.println("No s'ha pogut executar : " + sqlQuery);
        }
    }

    public void createTables() {
        String sqlQuery;

        sqlQuery
                = ("create table if not exists zona("
                + "     nif varchar(10),"
                + "     descripcion varchar(200),"
                + "     primary key (nif)"
                + ");");
        this.executeSQLQuery(sqlQuery);

        sqlQuery
                = ("create table if not exists comercial("
                + "     nif varchar(10),"
                + "     nombre varchar(100),"
                + "     zona varchar(10),"
                + "     primary key (nif),"
                + "     constraint comercial_zona foreign key (zona) references zona (nif)"
                + ");");
        this.executeSQLQuery(sqlQuery);

        sqlQuery
                = ("create table if not exists sector("
                + "     nif varchar(10),"
                + "     descripcion varchar(100),"
                + "     primary key (nif)"
                + ");");
        this.executeSQLQuery(sqlQuery);

        sqlQuery
                = ("create table if not exists articulo("
                + "id integer not null,"
                + "descripcion varchar(100),"
                + "primary key (id)"
                + ");");
        this.executeSQLQuery(sqlQuery);

        sqlQuery
                = ("create table if not exists cliente("
                + "     id integer not null,"
                + "     nif varchar(10) not null,"
                + "     nombre varchar(100),"
                + "     sector varchar(10),"
                + "     zona varchar(10),"
                + "     primary key (id),"
                + "     constraint cliente_sector foreign key (sector) references sector (nif),"
                + "     constraint cliente_zona foreign key (zona) references zona (nif),"
                + "     unique (nif)"
                + ");");
        this.executeSQLQuery(sqlQuery);
    }

    public void dropTables() {
        String sqlQuery;
        sqlQuery = "drop table comercial";
        this.executeSQLQuery(sqlQuery);
        sqlQuery = "drop table articulo";
        this.executeSQLQuery(sqlQuery);
        sqlQuery = "drop table cliente";
        this.executeSQLQuery(sqlQuery);
        sqlQuery = "drop table zona";
        this.executeSQLQuery(sqlQuery);
        sqlQuery = "drop table sector";
        this.executeSQLQuery(sqlQuery);
    }

    public void insertTuple(Article articulo) {
        String sqlQuery = "insert into articulo values(?,?)";
        PreparedStatement pState = null;
        try {
            if (articulo.getId() != null && articulo.getDescripcion() != null) {
                pState = this.connection.prepareStatement(sqlQuery);
                pState.setInt(1, articulo.getId());
                pState.setString(2, articulo.getDescripcion());
                pState.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("No s'ha pogut executar : " + sqlQuery);
        } finally {
            this.closeStatement(pState);
        }
    }

    public void updateTuple(Article articulo) {
        String sqlQuery = "update articulo set descripcion = ? where id = ?";
        PreparedStatement pstm = null;
        try {
            if (articulo.getId() != null) {
                pstm = this.connection.prepareStatement(sqlQuery);
                pstm.setString(1, articulo.getDescripcion());
                pstm.setInt(2, articulo.getId());
                pstm.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("No s'ha pogut executar : " + sqlQuery);
        } finally {
            this.closeStatement(pstm);
        }
    }

    public void deleteTuple(Article articulo) {
        String sqlQuery = "delete from articulo where id = ?";
        PreparedStatement pstm = null;
        try {
            if (articulo.getId() != null) {
                pstm = this.connection.prepareStatement(sqlQuery);
                pstm.setInt(1, articulo.getId());
                pstm.executeUpdate();
            }
        } catch (SQLException e) {
            System.out.println("No s'ha pogut executar : " + sqlQuery);
        } finally {
            this.closeStatement(pstm);
        }
    }

    public Article getArticle(Integer id) {
        Article articulo = null;
        String sqlQuery = "select * from articulo where id = ?";
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            pstm = this.connection.prepareStatement(sqlQuery);
            pstm.setInt(1, id);
            rs = pstm.executeQuery();
            if (rs.next()) {
                articulo = new Article(rs.getInt(1), rs.getString(2));
            }
            System.out.println("Operacio correcta : " + sqlQuery);
        } catch (SQLException e) {
            System.out.println("No s'ha pogut executar : " + sqlQuery);
        } finally {
            this.closeResultSet(rs);
            this.closeStatement(pstm);
        }
        return articulo;
    }

    public ArrayList<Article> getArticles(String descripcion) {
        ArrayList<Article> articulos = new ArrayList();
        String sqlQuery = "select * from articulo where descripcion like ?";
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            pstm = this.connection.prepareStatement(sqlQuery);
            pstm.setString(1, descripcion + "%");
            rs = pstm.executeQuery();
            while (rs.next()) {
                articulos.add(new Article(rs.getInt(1), rs.getString(2)));
            }
            System.out.println("Operacio correcta : " + sqlQuery);
        } catch (SQLException e) {
            System.out.println("No s'ha pogut executar : " + sqlQuery);
        } finally {
            this.closeResultSet(rs);
            this.closeStatement(pstm);
        }
        return articulos;
    }

    public void showList() {
        ArrayList<Article> articulos = new ArrayList();
        String sqlQuery = "select * from articulo";
        PreparedStatement pstm = null;
        ResultSet rs = null;
        try {
            pstm = this.connection.prepareStatement(sqlQuery);
            rs = pstm.executeQuery();
            while (rs.next()) {
                articulos.add(new Article(rs.getInt(1), rs.getString(2)));
            }
            System.out.println("Operacio correcta : " + sqlQuery);
        } catch (SQLException e) {
            System.out.println("No s'ha pogut executar : " + sqlQuery);
        } finally {
            this.closeResultSet(rs);
            this.closeStatement(pstm);
        }
    }

    public void closeResultSet(ResultSet rs) {
        try {
            if (rs != null && !rs.isClosed()) {
                rs.close();
            }
        } catch (SQLException e) {
            System.out.println("No s'ha pogut tancar.");
        }
    }

    public void closeStatement(Statement stm) {
        try {
            if (stm != null && !stm.isClosed()) {
                stm.close();
            }
        } catch (SQLException e) {
            System.out.println("No s'ha pogut tancar.");
        }
    }

}
