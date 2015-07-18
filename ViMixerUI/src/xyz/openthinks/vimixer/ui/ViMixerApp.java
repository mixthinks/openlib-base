package xyz.openthinks.vimixer.ui;

import java.io.IOException;

import javafx.application.Application;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import xyz.openthinks.vimixer.resources.ResourceLoader;
import xyz.openthinks.vimixer.ui.controller.ConfigurePaneController;
import xyz.openthinks.vimixer.ui.controller.MainFrameController;
import xyz.openthinks.vimixer.ui.controller.RootLayoutController;
import xyz.openthinks.vimixer.ui.model.ViFile;
import xyz.openthinks.vimixer.ui.model.configure.ViMixerConfigure;

public class ViMixerApp extends Application {

	private BorderPane rootLayout;
	private final TransferData transfer = new TransferData(this);

	@Override
	public void start(Stage primaryStage) {
		primaryStage.getIcons().add(ResourceLoader.APP_ICON);
		primaryStage.setOnCloseRequest((event)->{
			System.exit(0);
		});
		transfer.setPrimaryStage(primaryStage);
		initRootLayout();
		showMainFrame();
	}

	private void initRootLayout() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ResourceLoader.FXML_ROOTLAYOUT);
		try {
			this.rootLayout = loader.load();
			Scene scene = new Scene(this.rootLayout);
			scene.getStylesheets().add(ResourceLoader.CSS_APP.toExternalForm());

			RootLayoutController controller = loader.getController();
			controller.setTransfer(transfer);

			transfer.stage().setScene(scene);
			transfer.stage().show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void showMainFrame() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ResourceLoader.FXML_MAINFRAME);
		try {
			AnchorPane anchorPane = loader.load();
			MainFrameController controller = loader.getController();
			controller.setTransfer(transfer);
			this.rootLayout.setCenter(anchorPane);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void showConfigurePane() {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(ResourceLoader.FXML_CONFIGUREPANE);
		try {
			AnchorPane anchorPane = loader.load();
			ConfigurePaneController controller = loader.getController();
			controller.setTransfer(transfer);
			Stage stage = new Stage();
			stage.getIcons().add(ResourceLoader.APP_ICON);
			stage.initOwner(transfer.stage());
			stage.setTitle("ViMixer Configure");
			stage.initModality(Modality.WINDOW_MODAL);
			stage.setResizable(false);
			Scene scene = new Scene(anchorPane);
			stage.setScene(scene);
			stage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static void main(String[] args) {
		launch(args);
	}

	public static class TransferData {
		private final Application application;

		private final ViMixerConfigure configureXML = new ViMixerConfigure();
		
		private final ObjectProperty<ObservableList<ViFile>> viFiles = new SimpleObjectProperty<ObservableList<ViFile>>(FXCollections.observableArrayList());
		private Stage primaryStage;

		public TransferData(final Application application) {
			this.application = application;
		}

		private void setPrimaryStage(Stage primaryStage) {
			this.primaryStage = primaryStage;
		}

		public Application app() {
			return application;
		}

		public ObjectProperty<ObservableList<ViFile>> listProperty() {
			return viFiles;
		}

		public ViMixerConfigure configure() {
			return configureXML;
		}

		public Stage stage() {
			return primaryStage;
		}
	}

}
