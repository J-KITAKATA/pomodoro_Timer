import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.scene.control.*;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.*;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javafx.scene.image.*;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;



public class Main extends Application {

    private static Spinner<Integer> minAct;
    private static Spinner<Integer> minInt;

    private final int Def_Act_Time = 25; // 活動時間のリセットに使用
    private final int Def_Int_Time = 5; // 休憩時間のリセットに使用

    private enum TimeState{Stopped, Running, Paused} // ステータスの種類を設定
    private TimeState state = TimeState.Stopped; // ステータスの初期値を設定

    private enum TimePhase {Idol, Active, Interval} // 状態を用意
    private TimePhase timePhase = TimePhase.Idol; // 状態の初期値：Idol

    // タイムライン変数をまとめる
    private final Timeline[] Act_timeline = new Timeline[1];
    private final Timeline[] Intv_timeline = new Timeline[1];

    // ステータスラベルの初期値設定
    private Label statusLabel = new Label("Status：Ready");

    // その他ラベルの宣言
    private Label TimeLabel;
    private Label T_Name;

    // ボタン系の宣言
    private Button startButton;
    private Button StopButton;
    private Button ResetButton;

    @Override
    public void start(Stage stage) {
        // ここはまだ空（後でラベルやボタンを追加します）
        stage.setTitle("Pomodoro Timer");

        // アイコン用画像の追加
        stage.getIcons().addAll(
                new Image(getClass().getResource("/ico/timer_16x16-32bit.ico").toExternalForm()),
                new Image(getClass().getResource("/ico/timer_32x32-32bit.ico").toExternalForm()),
                new Image(getClass().getResource("/ico/timer_48x48-32bit.ico").toExternalForm()),
                new Image(getClass().getResource("/ico/timer_256x256-32bit.ico").toExternalForm()),
                new Image(getClass().getResource("/ico/timer_256x256-32bit.png").toExternalForm())
        );

        // ウィンドウサイズを設定
        stage.setResizable(false);
        stage.setWidth(400); // 幅 単位：px
        stage.setHeight(300); // 高さ 単位：px

        Pane pane = new Pane();

        // 活動分数の入力欄
        // 関数から
        Group actText = getActiveSpinner();
        pane.getChildren().add(actText);

        // インターバル時間
        Group intvText = getIntervalSpinner();
        pane.getChildren().add(intvText);

        // 入力欄の初期値設定
        minAct.setDisable(false);
        minInt.setDisable(false);

        // ボタンを追加：ラベル「Start」
        startButton = new Button("Start");
        startButton.setLayoutX(70); // 左上から横にXpx
        startButton.setLayoutY(180); // 縦にYpx
        startButton.setPrefWidth(120); // ボタンの幅
        startButton.setPrefHeight(70); // ボタンの高さ
        startButton.setFont(Font.font("System", FontWeight.NORMAL, 25));

        // ボタンを追加：ラベル「Stop」
        StopButton = new Button("Stop");
        StopButton.setLayoutX(200); // 左上から横にXpx
        StopButton.setLayoutY(180); // 縦にYpx
        StopButton.setPrefWidth(60);
        StopButton.setPrefHeight(30);

        // ボタンを追加：ラベル「Reset」
        ResetButton = new Button("Reset");
        ResetButton.setLayoutX(200); // 左上から横にXpx
        ResetButton.setLayoutY(220); // 縦にYpx
        ResetButton.setPrefWidth(60);
        ResetButton.setPrefHeight(30);

        // チェックボックスを追加
        CheckBox cb = new CheckBox("Sound On");
        cb.setLayoutX(285);
        cb.setLayoutY(180);

        ComboBox<String> soundSelector = new ComboBox<>();
        soundSelector.getItems().addAll("Sound1", "Sound2", "Sound3");
        soundSelector.setValue("Sound1");
        soundSelector.setLayoutX(285);
        soundSelector.setLayoutY(220);

        // SoundFileの設定
        String soundfile = switch (soundSelector.getValue()) {
            case "Sound1" -> "Clock-Alarm01-1(Low-Loop).mp3";
            case "Sound2" -> "Clock-Alarm01-2(Mid-Loop).mp3";
            case "Sound3" -> "Clock-Alarm01-3(High-Loop).mp3";
            default -> "";
        };

        Media alarmSound = new Media(
                Objects.requireNonNull(
                    getClass().getResource("/Clock-Alarm01/" + soundfile)
                ).toExternalForm()
        );

        MediaPlayer MP = new MediaPlayer(alarmSound);

        // ステータス表示

        // システムフォント、太字、20px
        statusLabel.setFont(Font.font("System", FontWeight.BOLD, 20));
        statusLabel.setLayoutX(200);
        statusLabel.setLayoutY(135);

        // 時間を表示するためのもの
        TimeLabel = new Label("");

        // 何の時間化を表示するためのもの
        T_Name = new Label("");

        //ボタンを画面上に追加
        pane.getChildren().addAll(startButton,StopButton,ResetButton,cb,soundSelector,statusLabel,TimeLabel,T_Name);

        // ボタンの初期状態を設定
        startButton.setDisable(false); // 初期値：押せる
        StopButton.setDisable(true); // 初期値：押せない
        ResetButton.setDisable(false); // 初期値：押せる

        Scene scene = new Scene(pane);
        stage.setScene(scene);
        // レイアウトを表示するために必要

        // ダイアログを準備
        // -> 別ファイルのクラスの呼び出し準備
        Conf_Dialog dialog = new Conf_Dialog(stage);

        // 状態を準備
        // Optional<ButtonType> result = Optional.empty();

        // timerstate
        AtomicReference<String> T_state = new AtomicReference<>("null");

        // 時間を処理する部分
        // 時間を秒に変換
        // オブジェクトとして扱うから、書き方注意！
        IntegerProperty secAct = new SimpleIntegerProperty();
        IntegerProperty secInt = new SimpleIntegerProperty();

        // 活動時間
        Act_timeline[0] = new Timeline(
          new KeyFrame(Duration.seconds(1),event -> {
              if (secAct.get() > 0) {
                  secAct.set(secAct.get() - 1);
              }
              else{
                  Act_timeline[0].stop();
                  if(cb.isSelected()) {
                      MP.play();
                  }
                  Platform.runLater(() -> {
                      Optional<ButtonType> result = dialog.showAndWait();

                      if (result.isPresent() && result.get() == ButtonType.YES) {
                          timePhase = TimePhase.Interval;
                          secInt.set(minInt.getValue() * 60);
                          Intv_timeline[0].play();
                          T_Name.setText("Interval Time");
                          T_Name.setLayoutX(25);
                          T_Name.setLayoutY(7.5);
                          T_Name.setFont(Font.font("System", FontWeight.BOLD, 25));
                          startButton.setDisable(true);
                          MP.stop();
                      } else {
                          pushStop();
                          MP.stop();
                      }
                  });
              }

              int ATL_min = secAct.get() / 60;
              int ATL_sec = secAct.get() % 60;

              String AmStr = (ATL_min < 10) ? "0" + ATL_min : String.valueOf(ATL_min);
              String AsStr = (ATL_sec < 10) ? "0" + ATL_sec : String.valueOf(ATL_sec);

              TimeLabel.setText(AmStr + ":" + AsStr);
              TimeLabel.setLayoutX(130);
              TimeLabel.setLayoutY(30);
              TimeLabel.setFont(Font.font("System", FontWeight.BOLD, 70));
          })
        );
        Act_timeline[0].setCycleCount(Timeline.INDEFINITE);

        // 休憩時間
        Intv_timeline[0] = new Timeline(
                new KeyFrame(Duration.seconds(1),event -> {
                    if (secInt.get() > 0) {
                        secInt.set(secInt.get() - 1);
                    }
                    else{
                        Intv_timeline[0].stop();
                        if(cb.isSelected()) {
                            MP.play();
                        }
                        Platform.runLater(() -> {
                            Optional<ButtonType> result = dialog.showAndWait();

                            if (result.isPresent() && result.get() == ButtonType.YES) {
                                timePhase = TimePhase.Active;
                                secAct.set(minAct.getValue() * 60);
                                Act_timeline[0].play();
                                T_Name.setText("Active Time");
                                T_Name.setLayoutX(25);
                                T_Name.setLayoutY(7.5);
                                T_Name.setFont(Font.font("System", FontWeight.BOLD, 25));
                                startButton.setDisable(false);
                                MP.stop();
                            } else {
                                pushStop();
                                MP.stop();
                            }
                        });
                    }
                    int ITL_min = secInt.get() / 60;
                    int ITL_sec = secInt.get() % 60;

                    String ImStr = (ITL_min < 10) ? "0" + ITL_min : String.valueOf(ITL_min);
                    String IsStr = (ITL_sec < 10) ? "0" + ITL_sec : String.valueOf(ITL_sec);

                    TimeLabel.setText(ImStr + ":" + IsStr);
                    TimeLabel.setLayoutX(130);
                    TimeLabel.setLayoutY(30);
                    TimeLabel.setFont(Font.font("System", FontWeight.BOLD, 70));
                })
        );
        Intv_timeline[0].setCycleCount(Timeline.INDEFINITE);

        // Startボタンを押したときの挙動
        startButton.setOnAction(e -> {
            if(timePhase == TimePhase.Interval){
                startButton.setDisable(true);
                return;
            }

            switch(state){
                case Stopped: // Startボタンを押した時(Start -> Pauseに表示変更)
                    state = TimeState.Running;
                    startButton.setText("Pause");

                    // ボタンの状態を変更
                    StopButton.setDisable(false);
                    ResetButton.setDisable(true);

                    minAct.setDisable(true);
                    minInt.setDisable(true);

                    statusLabel.setText("Status：Play");

                    secAct.set(minAct.getValue() * 60);
                    secInt.set(minInt.getValue() * 60);

                    Act_timeline[0].play();
                    T_Name.setText("Active Time");
                    T_Name.setLayoutX(25);
                    T_Name.setLayoutY(7.5);
                    T_Name.setFont(Font.font("System", FontWeight.BOLD, 25));

                    timePhase = TimePhase.Active;
                    break;

                case Running: // 一時停止する(Pause -> Restartに表示変更)
                    state = TimeState.Paused;
                    startButton.setText("Restart");

                    if(timePhase == TimePhase.Active && state==TimeState.Paused){
                        minInt.setDisable(false);
                    }
                    else{
                        minInt.setDisable(true);
                    }

                    Act_timeline[0].pause();
                    statusLabel.setText("Status：Pause");
                    break;

                case Paused: // 再開する(Restart -> Pauseに表示変更)
                    state = TimeState.Running;
                    startButton.setText("Pause");

                    minInt.setDisable(true);

                    // Pause中に入力された値を更新
                    secInt.set(minInt.getValue() * 60);

                    Act_timeline[0].play();
                    statusLabel.setText("Status：Play");
                    break;
            }
        });

        // Stopボタンを押したときの挙動
        StopButton.setOnAction(e -> {
            pushStop();
        });

        // Resetボタンを押したときの挙動
        ResetButton.setOnAction(e -> {
            minAct.getValueFactory().setValue(Def_Act_Time);
            minInt.getValueFactory().setValue(Def_Int_Time);
        });

        stage.show(); // 表示(必ず最後！)
    }

    private void pushStop(){
        // ステータスの初期化と、スタートボタンの初期化
        state = TimeState.Stopped;
        startButton.setText("Start");
        startButton.setDisable(false);

        StopButton.setDisable(true);
        ResetButton.setDisable(false);

        minAct.setDisable(false);
        minInt.setDisable(false);

        statusLabel.setText("Status：Ready");

        if(timePhase == TimePhase.Active){
            Act_timeline[0].stop();
        }
        else{
            Intv_timeline[0].stop();
        }

        TimeLabel.setText(""); // 時間表示を消す
        T_Name.setText("");

        timePhase = TimePhase.Idol;
    }

    private static Group getActiveSpinner() {
        // Spinnerの用途を示すラベルの表示
        Label workSpin = new Label("Active (min)");

        workSpin.setLayoutX(0);
        workSpin.setLayoutY(0);

        minAct = new Spinner<>(5, 120, 25, 5);
        // (min,max,Initial,step)

        // テキストの直接入力を許可
        minAct.setEditable(true);
        // 入力された値は getValue() で取得できる

        minAct.setLayoutX(0);
        minAct.setLayoutY(20);
        minAct.setPrefWidth(70);

        Group minTextG =  new Group(workSpin,minAct);

        minTextG.setLayoutX(10);
        minTextG.setLayoutY(120);

        return minTextG;
    }

    private static Group getIntervalSpinner() {
        // Spinnerの用途を示すラベルの表示
        Label intvSpin = new Label("Interval (min)");

        intvSpin.setLayoutX(0);
        intvSpin.setLayoutY(0);

        minInt = new Spinner<>(1, 60, 5, 1);
        // (min,max,Initial,step)

        // テキストの直接入力を許可
        minInt.setEditable(true);
        // 入力された値は getValue() で取得できる

        minInt.setLayoutX(0);
        minInt.setLayoutY(20);
        minInt.setPrefWidth(70);

        Group minTextG =  new Group(intvSpin,minInt);

        minTextG.setLayoutX(100);
        minTextG.setLayoutY(120);

        return minTextG;
    }

    public static void main(String[] args) {
        launch(args);
    }
}