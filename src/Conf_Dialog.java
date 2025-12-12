import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.image.*;

public class Conf_Dialog extends Dialog<ButtonType> {
    public static final String LINE_SEPARATOR = System.lineSeparator();

    // コンストラクタ
    public Conf_Dialog(Stage ownerStage) {
        setTitle("Check Point");

        // 親ウィンドウを設定
        this.initOwner(ownerStage);

        getDialogPane().setPrefWidth(280);
        getDialogPane().setPrefHeight(100);

        // 画像追加
        Image Image = new Image(getClass().getResource("/ico/95ques_32x32.png").toExternalForm());
        ImageView imageView = new ImageView(Image);
        imageView.setPreserveRatio(true);

        // 画像を表示
        getDialogPane().setGraphic(imageView);

        // ボタン追加
        getDialogPane().getButtonTypes().addAll(ButtonType.YES, ButtonType.NO);

        // 最前面表示（Sceneが作られた後に実行）
        // 表示時に最前面にする処理を組み込む
        showingProperty().addListener((obs, wasShowing, isNowShowing) -> {
            if (isNowShowing) {
                Stage stage = (Stage) getDialogPane().getScene().getWindow();
                stage.setAlwaysOnTop(true);  // 表示直後に最前面
                stage.toFront();             // 念のため明示的に前面
                stage.setAlwaysOnTop(false); // 他のウィンドウにフォーカスが移ると通常挙動に戻す
            }
        });

        // 内容
        setContentText("Time has ended" +  LINE_SEPARATOR + "Would you like to continue?");

    }
}
