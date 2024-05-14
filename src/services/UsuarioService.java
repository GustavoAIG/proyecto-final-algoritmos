package services;

import core.db.MysqlDBService;
import core.utils.UsuarioThreadLocal;
import java.sql.ResultSet;
import java.sql.SQLException;
import models.Usuario;

public class UsuarioService extends BaseService {

    public UsuarioService() {
        db = new MysqlDBService();
    }

    public void obtenerUsuarioPorRol(String rol, String username, String pwd) {

        Object[] parametrosSQL_1 = new Object[2];

        switch (rol) {
            case "candidato" ->
                querySQL_1 = "SELECT u.*, c.id AS 'id_candidato' FROM usuarios u JOIN candidatos c ON c.id_usuario = u.id AND c.estado = 'activo' WHERE u.username = ? AND u.pwd = ? AND u.estado = 'activo' LIMIT 1; ";
            case "reclutador" ->
                querySQL_1 = "SELECT u.*, r.id AS 'id_reclutador' FROM usuarios u JOIN reclutadores r ON r.id_usuario = u.id AND r.estado = 'activo' WHERE u.username = ? AND u.pwd = ? AND u.estado = 'activo' LIMIT 1; ";
            default ->
                throw new AssertionError();
        }

        parametrosSQL_1[0] = username;
        parametrosSQL_1[1] = pwd;

        ResultSet rs = db.queryConsultar(querySQL_1, parametrosSQL_1);
        Usuario usuario = new Usuario();

        try {

            // Eliminar usuario en sesión local
            UsuarioThreadLocal.unset();

            while (rs.next()) {
                usuario.setIdUsuario(rs.getInt("id"));

                switch (rol) {
                    case "candidato" -> {
                        usuario.setIdCandidato(rs.getInt("id_candidato"));
                    }
                    case "reclutador" -> {
                        usuario.setIdReclutador(rs.getInt("id_reclutador"));
                    }
                    default ->
                        throw new AssertionError();
                }

                usuario.setNombres(rs.getString("nombres"));
                usuario.setApellidos(rs.getString("apellidos"));
                usuario.setFullname(rs.getString("nombres") + " " + rs.getString("apellidos"));
                usuario.setRol(rs.getString("rol"));
                usuario.setUsername(rs.getString("username"));
                usuario.setPassword(rs.getString("pwd"));
                usuario.setEstado(rs.getString("estado"));
                usuario.setFechaCreado(rs.getString("fecha_creado"));
            }

            // Actualizar nuevo usuario en sesión local
            UsuarioThreadLocal.set(usuario);

            System.out.println();

        } catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        db.cerrarConsulta();
    }
}