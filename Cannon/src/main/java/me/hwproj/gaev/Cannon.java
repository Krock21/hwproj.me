package me.hwproj.gaev;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.input.KeyEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polyline;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Pair;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * JavaFX Application-game "Cannon"
 */
public class Cannon extends Application {
    private final int PROJECTILES_COUNT = 2;
    private final int APPLICATION_WIDTH = 500;
    private final int APPLICATION_HEIGHT = 500;
    private final double ANGLE_STEP = Math.PI / 50;
    private final double FINISH_X = 19 * APPLICATION_WIDTH / 20;
    private double currentX = 0;
    private double currentY = APPLICATION_HEIGHT / 2;
    private double currentAngle = Math.PI / 2;
    private int currentProjectile;
    private final double[] fieldCoordinates = {
            0, APPLICATION_HEIGHT / 2,
            APPLICATION_WIDTH / 10, APPLICATION_HEIGHT / 2,
            2 * APPLICATION_WIDTH / 10, 2 * APPLICATION_HEIGHT / 3,
            4 * APPLICATION_WIDTH / 10, APPLICATION_HEIGHT / 2,
            6 * APPLICATION_WIDTH / 10, APPLICATION_HEIGHT / 4,
            9 * APPLICATION_WIDTH / 10, 6 * APPLICATION_HEIGHT / 11,
            APPLICATION_WIDTH, APPLICATION_HEIGHT / 2
    };
    private ArrayList<Projectile> projectiles = new ArrayList<>();

    abstract private class Projectile {
        protected boolean isDead;
        protected double currentX;
        protected double currentY;
        protected double currentXDirection;
        protected double currentYDirection;

        protected Projectile(double currentX, double currentY) {
            this.currentX = currentX;
            this.currentY = currentY;
        }

        abstract protected Node draw(); // draw

        abstract protected void process(); // move

        abstract protected void boom(); // check for finish

    }

    private class SimpleProjectile extends Projectile {
        private final double GRAVITY_SPEED = 0.001;
        private final double LAUNCH_SPEED = 0.6;
        private final double BOOM_RADIUS = 5;

        private SimpleProjectile(double currentX, double currentY, double currentAngle) {
            super(currentX, currentY);
            currentXDirection = Math.cos(currentAngle) * LAUNCH_SPEED;
            currentYDirection = -Math.sin(currentAngle) * LAUNCH_SPEED;
        }

        @Override
        protected Node draw() {
            Circle answer = new Circle(BOOM_RADIUS / 2, Paint.valueOf("green"));
            answer.setCenterX(currentX);
            answer.setCenterY(currentY);
            return answer;
        }

        @Override
        protected void process() {
            currentX += currentXDirection;
            currentY += currentYDirection;
            currentYDirection += GRAVITY_SPEED;
        }

        @Override
        protected void boom() {
            if (Cannon.this.getYOnFieldByX(currentX) <= currentY) {
                isDead = true;
                if (Math.hypot(currentX - FINISH_X, currentY - Cannon.this.getYOnFieldByX(FINISH_X)) <= BOOM_RADIUS) {
                    System.exit(0);
                }
            }
        }
    }

    private class BigBoomProjectile extends Projectile {
        private final double GRAVITY_SPEED = 0.002;
        private final double LAUNCH_SPEED = 0.98;
        private final double BOOM_RADIUS = 15;

        private BigBoomProjectile(double currentX, double currentY, double currentAngle) {
            super(currentX, currentY);
            currentXDirection = Math.cos(currentAngle) * LAUNCH_SPEED;
            currentYDirection = -Math.sin(currentAngle) * LAUNCH_SPEED;
        }

        @Override
        protected Node draw() {
            Circle answer = new Circle(BOOM_RADIUS / 2, Paint.valueOf("green"));
            answer.setCenterX(currentX);
            answer.setCenterY(currentY);
            return answer;
        }

        @Override
        protected void process() {
            currentX += currentXDirection;
            currentY += currentYDirection;
            currentYDirection += GRAVITY_SPEED;
        }

        @Override
        protected void boom() {
            if (Cannon.this.getYOnFieldByX(currentX) <= currentY) {
                isDead = true;
                if (Math.hypot(currentX - FINISH_X, currentY - Cannon.this.getYOnFieldByX(FINISH_X)) <= BOOM_RADIUS) {
                    System.exit(0);
                }
            }
        }
    }

    private double getYOnFieldByX(double x) {
        for (int i = 2; i < fieldCoordinates.length - 1; i += 2) {
            if (x >= fieldCoordinates[i - 2] && x <= fieldCoordinates[i]) {
                return fieldCoordinates[i - 1] +
                        (x - fieldCoordinates[i - 2])
                                / (fieldCoordinates[i] - fieldCoordinates[i - 2])
                                * (fieldCoordinates[i + 1] - fieldCoordinates[i - 1]);
            }
        }
        return APPLICATION_HEIGHT / 2;
    }

    private void updateCannonCoordinates() {
        currentY = getYOnFieldByX(currentX);
    }

    private void renderAll(Group root) {
        root.getChildren().clear();
        // Info
        Text info = new Text();
        info.setText("Move : arrows. Change angle: arrows.\nChange projectile: Q. Launch projectile: space.");
        info.setX(5);
        info.setY(30);
        root.getChildren().add(info);

        // Current Projectile
        Text projectileInfo = new Text();
        if (currentProjectile == 0) {
            projectileInfo.setText("0 SimpleProjectile");
        }
        if (currentProjectile == 1) {
            projectileInfo.setText("1 BigBoomProjectile");
        }
        projectileInfo.setX(5);
        projectileInfo.setY(15);
        root.getChildren().add(projectileInfo);
        // Cannon
        Canvas cannon = new Canvas(APPLICATION_WIDTH, APPLICATION_HEIGHT);
        GraphicsContext cannonGraphicsContext = cannon.getGraphicsContext2D();
        cannonGraphicsContext.setFill(Color.BLUE);
        double[] xs = new double[4];
        xs[0] = currentX + Math.sin(currentAngle) * 5;
        xs[1] = currentX + Math.cos(currentAngle) * 15 + Math.sin(currentAngle) * 5;
        xs[2] = currentX + Math.cos(currentAngle) * 15 - Math.sin(currentAngle) * 5;
        xs[3] = currentX - Math.sin(currentAngle) * 5;
        double[] ys = new double[4];
        ys[0] = currentY + Math.cos(currentAngle) * 5;
        ys[1] = currentY - Math.sin(currentAngle) * 15 + Math.cos(currentAngle) * 5;
        ys[2] = currentY - Math.sin(currentAngle) * 15 - Math.cos(currentAngle) * 5;
        ys[3] = currentY - Math.cos(currentAngle) * 5;
        cannonGraphicsContext.fillPolygon(xs, ys, 4);
        root.getChildren().add(cannon);

        // Field
        Polyline field = new Polyline();
        for (var v : fieldCoordinates) {
            field.getPoints().add(v);
        }
        root.getChildren().add(field);

        // Finish
        Circle finish = new Circle(5, Paint.valueOf("red"));
        finish.setCenterX(FINISH_X);
        finish.setCenterY(getYOnFieldByX(FINISH_X));
        root.getChildren().add(finish);

        // Projectiles
        for (var projectile : projectiles) {
            if (!projectile.isDead) {
                projectile.process();
                projectile.boom();
                root.getChildren().add(projectile.draw());
            }
        }
        projectiles = (ArrayList<Projectile>) projectiles.stream().filter(p -> !p.isDead).collect(Collectors.toList());
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();
        Scene scene = new Scene(root, APPLICATION_WIDTH, APPLICATION_HEIGHT);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Cannon game");
        scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override
            public void handle(KeyEvent event) {
                var keyCode = event.getCode();
                switch (keyCode) {
                    case UP:
                        // handle up
                        currentAngle += ANGLE_STEP;
                        break;
                    case DOWN:
                        // handle down
                        currentAngle -= ANGLE_STEP;
                        break;
                    case LEFT:
                        // handle left
                        currentX--;
                        updateCannonCoordinates();
                        break;
                    case RIGHT:
                        currentX++;
                        updateCannonCoordinates();
                        // handle right
                        break;
                    case SPACE:
                        if (currentProjectile == 0) {
                            projectiles.add(new SimpleProjectile(currentX, currentY - 2, currentAngle));
                        }
                        if (currentProjectile == 1) {
                            projectiles.add(new BigBoomProjectile(currentX, currentY - 2, currentAngle));
                        }
                        break;
                    case Q:
                        currentProjectile++;
                        currentProjectile %= PROJECTILES_COUNT;
                }
            }
        });
        new AnimationTimer() {
            public void handle(long currentNanoTime) {
                renderAll(root);
            }
        }.start();
        primaryStage.show();
    }

    public static void main(String args[]) {
        launch(args);
    }
}
