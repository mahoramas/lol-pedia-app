package es.mahoramas.lolpedia.controller;

import java.util.ArrayList;
import java.util.List;

import es.mahoramas.lolpedia.PrincipalApplication;
import es.mahoramas.lolpedia.config.ConfigManager;
import es.mahoramas.lolpedia.controller.abstractas.AbstractController;
import es.mahoramas.lolpedia.model.UsuarioEntity;
import es.mahoramas.lolpedia.model.UsuarioServiceModel;
import es.mahoramas.lolpedia.sesion.Sesion;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginController extends AbstractController {

    private final String pathFichero = "src/main/resources/";
    private final String ficheroStr = "idioma-";

    @FXML
    protected TextField textFieldUsuario;

    @FXML
    protected PasswordField textFieldPassword;

    @FXML
    protected Text textFieldMensaje;

    @FXML
    protected Button buttonRegistrar;

    @FXML
    protected Button buttonIniciarSesion;

    @FXML
    protected Text textUsuario;

    @FXML
    protected Text textContrasenia;

    @FXML
    protected ComboBox comboIdioma;

    @FXML
    protected Hyperlink link;

    private UsuarioEntity user;
    UsuarioServiceModel usuarioServiceModel;

    public LoginController() {
        usuarioServiceModel = new UsuarioServiceModel();
    }

    public void setUsuario(UsuarioEntity usuario) {
        this.user = usuario;
    }

    public UsuarioEntity getUsuario() {
        return user;
    }

    /**
     * Metodo que inicializa la clase
     */
    @FXML
    public void initialize() {
        List<String> idiomas = new ArrayList<>();
        idiomas.add("es");
        idiomas.add("en");
        idiomas.add("fr");
        comboIdioma.getItems().addAll(idiomas);
        cargarIdioma("es");
        cambiarIdioma();
    }

    /**
     * Metodo que carga el idioma
     * 
     * @param idioma idioma a cargar
     */
    protected void cargarIdioma(String idioma) {
        ConfigManager.setIdioma(idioma);
    }

    /**
     * Metodo que compruba si los campos son correctos y te lleva a la pantalla
     * principal
     */
    @FXML
    protected void onLoginButtonClick() {

        if (textFieldUsuario == null || textFieldUsuario.getText().isEmpty() ||
                textFieldPassword == null || textFieldPassword.getText().isEmpty()) {
            textFieldMensaje.setText("Credenciales invalidas");
            return;
        }

        user = usuarioServiceModel.obtenerUsuarioPorNombreUsuario(textFieldUsuario.getText(),
                textFieldPassword.getText());

        if (user == null) {
            user = usuarioServiceModel.obtenerUsuarioPorEmail(textFieldUsuario.getText(), textFieldPassword.getText());
            return;
        }

        if (user == null) {
            textFieldMensaje.setText("Credenciales invalidas");
            return;
        }

        Sesion.getInstance().setUsuarioActual(user);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(PrincipalApplication.class.getResource("principal.fxml"));

            Scene scene = new Scene(fxmlLoader.load(), 1280, 800);

            Stage stage = (Stage) buttonIniciarSesion.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("El error es: " + e);
        }

        textFieldMensaje.setText("Usuario validado correctamente");
    }

    /**
     * Metodo que te lleva a la pantalla de registro
     */
    @FXML
    protected void openRegistrarClick() {
        try {

            FXMLLoader fxmlLoader = new FXMLLoader(PrincipalApplication.class.getResource("registro.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 1280, 800);
            Stage stage = (Stage) buttonRegistrar.getScene().getWindow();
            stage.setTitle("Pantalla Registro");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Metodo para cambiar de idioma
     */
    @FXML
    protected void seleccionarIdiomaClick() {
        String idioma = comboIdioma.getValue().toString();
        cargarIdioma(idioma);
        cambiarIdioma();
    }

    /**
     * Metodo para ir a la pantalla de recuperacion de contraseña
     */
    @FXML
    protected void onRecoverButtonClick() {
        try {
            Stage stage = (Stage) link.getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(
                    PrincipalApplication.class.getResource("recuperarConstrasenia.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 435, 371);
            stage.setTitle("Pantalla Recuperar Contraseña");
            stage.setResizable(false);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}