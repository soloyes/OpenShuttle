package com.shuttle.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.shuttle.game.screens.GameScreen;
import com.shuttle.game.screens.ScreenManager;

public class Shuttle {
    private GameScreen gs;
    private TextureRegion texture;
    private TextureRegion starTex;
    private int width, height;
    private Vector2 position;
    private Vector2 velocity;
    private int score, scoreView;
    private float angle;
    private float fuel, maxFuel;
    private int hp, maxHp;
    private int lives;
    private Vector2 tmp;
    private StringBuilder tmpString;
    private int takeRange;
    private SpeedRang speedRang;
    private float zigzagEffect;
    private String username;

    private int killSeries;
    private float killTimer;
    private String[] killMsgs = {"", "KILLED", "DOUBLE KILL", "MULTI KILL", "ULATRA KILL", "RAMPAGE", "HOLY S**T"};
    private int[] killMsgsScale = {0, 4, 5, 6, 7, 10, 15};

    public Vector2 getVelocity() {
        return velocity;
    }

    public boolean addZigZag(float amount) {
        if (zigzagEffect < 0.1f) {
            zigzagEffect += amount;
            return true;
        }
        return false;
    }

    public void killPlusPlus() {
        killSeries++;
        if (killSeries > 6) {
            killSeries = 6;
        }
        killTimer = 7.0f;
        gs.getGameInfoSystem().setup(position.x, position.y, killMsgs[killSeries], killMsgsScale[killSeries]);
    }

    public void takeDamage(int amount) {
        hp -= amount;
        if (hp <= 0) {
            hp = maxHp;
            lives--;
            gs.getGameInfoSystem().setup(position.x, position.y, "DESTROYED", 8);
        }
    }

    public Shuttle(GameScreen gs, float x, float y) {
        this.gs = gs;
        this.texture = Assets.getInstance().getAtlas().findRegion("rocket");
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(0.0f, 0.0f);
        this.tmp = new Vector2(0.0f, 0.0f);
        this.angle = 0.0f;
        this.starTex = Assets.getInstance().getAtlas().findRegion("star16");
        this.width = texture.getRegionWidth();
        this.height = texture.getRegionHeight();
        this.tmpString = new StringBuilder(200);
        this.maxFuel = 120.0f;
        this.fuel = this.maxFuel;
        this.maxHp = 10;
        this.hp = this.maxHp;
        this.lives = 3;
        this.username = Gdx.files.local("username").readString();
        this.takeRange = 500;
        this.speedRang = SpeedRang.NONE;
    }

    public void addScore(int amount) {
        score += amount;
    }

    public void drawRadar(SpriteBatch batch, float sx, float sy) {
        //batch.setColor(1, 1f, 1, 1);
        // batch.draw(starTex, sx, sy, 8f, 8f, 16, 16, 5, 5, 0);

        for (int i = 0; i < gs.getAlienEmitter().getActiveList().size(); i++) {
            Alien a = gs.getAlienEmitter().getActiveList().get(i);
            float d = Vector2.dst(position.x, position.y, a.getPosition().x, a.getPosition().y);
            if (d < 2000.0f) {
                float sc = (1.0f - d / 2000f) * 2 + 2.0f;
                tmp.set(a.getPosition()).sub(position).nor().scl(d / 2000.0f * 300.0f);
                batch.setColor(1, 0, 0, 1);
                batch.draw(starTex, position.x + tmp.x, position.y + tmp.y, 8f, 8f, 16, 16, sc, sc, 0);
                batch.setColor(1, 1, 1, 1);
            }
        }

        for (int i = 0; i < gs.getConsumableEmitter().getActiveList().size(); i++) {
            Consumable c = gs.getConsumableEmitter().getActiveList().get(i);
            float d = Vector2.dst(position.x, position.y, c.getPosition().x, c.getPosition().y);
            if (d < 2000.0f) {
                float sc = (1.0f - d / 2000.0f) * 2.0f + 2.0f;
                tmp.set(c.getPosition()).sub(position).nor().scl(d / 2000.0f * 300.0f);
                batch.setColor(0, 1, 0, 1);
                batch.draw(starTex, position.x + tmp.x, position.y + tmp.y, 8f, 8f, 16, 16, sc, sc, 0);
                batch.setColor(1, 1, 1, 1);
            }
        }
    }

    public void render(SpriteBatch batch) {
        batch.draw(texture, position.x - width / 2, position.y - height / 2, width / 2, height / 2, width, height, 1, 1, angle);
    }

    public void renderGUI(SpriteBatch batch, BitmapFont font) {
        tmpString.setLength(0);
        tmpString.append("NAME: ").append(username);
        tmpString.append("\nSCORE: ").append(scoreView);
        tmpString.append("\nHP: ").append(hp).append("/").append(maxHp).append(" x").append(lives);
        tmpString.append("\nFUEL: ").append((int) fuel).append("/").append((int) maxFuel);
        // tmpString.append("\nSPEED: ").append(velocity.len());

        font.draw(batch, tmpString, 20, 1900);
    }

    public void slowDown(float amount) {
        velocity.scl(1.0f - amount / velocity.len());
    }

    public void update(float dt) {
        if (killTimer > 0.0f) {
            killTimer -= dt;
            if (killTimer <= 0.0f) {
                killSeries = 0;
            }
        }
        if (zigzagEffect > 0.0f) {
            zigzagEffect -= dt;
            angle += zigzagEffect * 25.0f;
            velocity.add(MathUtils.random(-30 * zigzagEffect, 30 * zigzagEffect), MathUtils.random(-30 * zigzagEffect, 30 * zigzagEffect));
        }
        if (angle < 0.0f) {
            angle += 360.0f;
        }
        if (angle > 360.0f) {
            angle -= 360.0f;
        }
        if (scoreView < score) {
            int scoreDelta = (int) (Math.abs(score - scoreView) * 0.1f);
            if (scoreDelta < 5) {
                scoreDelta = 5;
            }
            if (scoreView < score) {
                scoreView += scoreDelta;
                if (scoreView > score) {
                    scoreView = score;
                }
            }
            if (scoreView > score) {
                scoreView -= scoreDelta;
                if (scoreView < score) {
                    scoreView = score;
                }
            }
        }
        velocity.scl(0.99f);
        position.mulAdd(velocity, dt);
        speedRang = SpeedRang.getRangFromVelocity(velocity.len());
        fuel -= speedRang.fuelPerSec * dt;
        if (fuel < 0.0f) {
            fuel = 0.0f;
            if (velocity.len() > 100) {
                velocity.nor().scl(100);
            }
        }
        createFXs();
        if (Gdx.input.isTouched()) {
            float radialSpeed = 135.0f;
            tmp.set(Gdx.input.getX(), Gdx.input.getY());
            ScreenManager.getInstance().getViewport().unproject(tmp);
            if (zigzagEffect < 0.05f) {
                float angleToTarget = tmp.sub(position).angle();
                if (angle > angleToTarget) {
                    if (Math.abs(angle - angleToTarget) <= 180.0f) {
                        angle -= radialSpeed * dt;
                    } else {
                        angle += radialSpeed * dt;
                    }
                }
                if (angle < angleToTarget) {
                    if (Math.abs(angle - angleToTarget) <= 180.0f) {
                        angle += radialSpeed * dt;
                    } else {
                        angle -= radialSpeed * dt;
                    }
                }
            }
            float acc = speedRang.acceleration * (1.0f - 0.1f * (float) (maxHp - hp) / maxHp);
            velocity.add(acc * (float) Math.cos(Math.toRadians(angle)) * dt, acc * (float) Math.sin(Math.toRadians(angle)) * dt);
        }
    }

    public void createFXs() {
        int dxy = 90;
        if (fuel > 0.1f) {
            if (velocity.len() > 80) {
                gs.getParticleEmitter().setup(
                        position.x - 90 * (float) Math.cos(Math.toRadians(angle)),
                        position.y - 90 * (float) Math.sin(Math.toRadians(angle)),
                        MathUtils.random(-dxy, dxy),
                        MathUtils.random(-dxy, dxy),
                        0.3f + speedRang.level * 0.1f,
                        4.0f + speedRang.level * 1.5f,
                        0.4f,
                        1,
                        0.6f - speedRang.level * 0.15f,
                        0,
                        1,
                        1,
                        0,
                        0,
                        0.5f
                );
                if (MathUtils.random(0, 100) < 6 + speedRang.level * 2) {
                    dxy = 180;
                    for (int i = 0; i < speedRang.level + 1; i++) {
                        gs.getParticleEmitter().setup(
                                position.x - 90 * (float) Math.cos(Math.toRadians(angle)),
                                position.y - 90 * (float) Math.sin(Math.toRadians(angle)),
                                MathUtils.random(-dxy, dxy),
                                MathUtils.random(-dxy, dxy),
                                0.3f,
                                8.0f + speedRang.level * 2,
                                0.0f,
                                1,
                                0.4f,
                                0,
                                1,
                                0,
                                0,
                                0,
                                0.2f
                        );
                    }
                }
            }
        } else {
            gs.getParticleEmitter().setup(
                    position.x - 90 * (float) Math.cos(Math.toRadians(angle)),
                    position.y - 90 * (float) Math.sin(Math.toRadians(angle)),
                    MathUtils.random(-dxy, dxy),
                    MathUtils.random(-dxy, dxy),
                    0.2f,
                    3.0f,
                    2.4f,
                    0.2f,
                    0.2f,
                    1.0f,
                    0.4f,
                    0,
                    0,
                    0,
                    0.2f
            );
        }
        if (velocity.len() > 300) {
            dxy = 20;
            for (int i = -45; i <= 45; i += 3) {
                gs.getParticleEmitter().setup(
                        position.x + 90 * (float) Math.cos(Math.toRadians(angle + i)),
                        position.y + 90 * (float) Math.sin(Math.toRadians(angle + i)),
                        MathUtils.random(-dxy, dxy),
                        MathUtils.random(-dxy, dxy),
                        0.4f,
                        3.0f,
                        2.0f,
                        speedRang.fr,
                        speedRang.fg,
                        speedRang.fb,
                        speedRang.fa,
                        0,
                        0,
                        0,
                        0.0f
                );
            }
            if(velocity.len() > 1200.0f) {
                gs.getParticleEmitter().setup(
                        position.x + 90 * (float) Math.cos(Math.toRadians(angle)),
                        position.y + 90 * (float) Math.sin(Math.toRadians(angle)),
                        MathUtils.random(-dxy, dxy),
                        MathUtils.random(-dxy, dxy),
                        5.9f,
                        16.0f,
                        15.0f,
                        1,
                        0.6f,
                        0,
                        1,
                        0,
                        0,
                        0,
                        0.0f
                );
            }
        }
    }

    public void takeEffect(Consumable.Type type) {
        switch (type) {
            case Astronaut:
                addScore(10000);
                gs.getGameInfoSystem().setup(position.x, position.y, "+10000", 3.0f);
                break;
            case Crate:
                int x = MathUtils.random(0, 5);
                if (x == 0) {
                    hp++;
                    if (hp > maxHp) {
                        hp = maxHp;
                    }
                    gs.getGameInfoSystem().setup(position.x, position.y, "REPAIR +1", 3.0f);
                }
                if (x == 1) {
                    fuel += 15;
                    if (fuel > maxFuel) {
                        fuel = maxFuel;
                    }
                    gs.getGameInfoSystem().setup(position.x, position.y, "FUEL +15", 3.0f);
                }
                if (x == 2) {
                    velocity.add(1500 * (float) Math.cos(Math.toRadians(angle)), 1500 * (float) Math.sin(Math.toRadians(angle)));
                    gs.getGameInfoSystem().setup(position.x, position.y, "TURBO X5", 5.0f);
                }
                if (x > 2) {
                    gs.getGameInfoSystem().setup(position.x, position.y, "TRASH", 2.0f);
                }
                break;
        }
    }

    public void badEffect(Consumable.Type type) {
        switch (type) {
            case Astronaut:
                addScore(-50000);
                gs.getGameInfoSystem().setup(position.x, position.y, "Murder!\n-50000", 8.0f);
                break;
            case Crate:
                gs.getGameInfoSystem().setup(position.x, position.y, "Be easy!", 3.0f);
                break;
        }
    }

    public Vector2 getPosition() {
        return position;
    }

    public SpeedRang getSpeedRang() {
        return speedRang;
    }

    public int getTakeRange() {
        return takeRange;
    }

    public enum SpeedRang {
        NONE(0, 0, 0, 0, 0, 0.5f, 540.0f), WHITE(1, 1, 1, 1, 0.1f, 0.8f, 560.0f), GREEN(2, 0, 1, 0, 0.1f, 1.0f, 580.0f), YELLOW(3, 1, 1, 0, 0.1f, 1.6f, 600.0f), RED(4, 1, 0, 0, 0.1f, 3.0f, 600.0f);
        private int level;
        private float fr;
        private float fg;
        private float fb;
        private float fa;
        private float fuelPerSec;
        private float acceleration;

        SpeedRang(int level, float fr, float fg, float fb, float fa, float fuelPerSec, float acceleration) {
            this.level = level;
            this.fr = fr;
            this.fg = fg;
            this.fb = fb;
            this.fa = fa;
            this.fuelPerSec = fuelPerSec;
            this.acceleration = acceleration;
        }

        private static SpeedRang getRangFromVelocity(float velLength) {
            if (velLength > 960) {
                return RED;
            }
            if (velLength > 900) {
                return YELLOW;
            }
            if (velLength > 800) {
                return GREEN;
            }
            if (velLength > 600) {
                return WHITE;
            }
            return NONE;
        }

        public int getLevel() {
            return level;
        }
    }
}
